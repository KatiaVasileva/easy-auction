package ru.skypro.coursework.easyauction.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skypro.coursework.easyauction.exceptions.BidNotFoundException;
import ru.skypro.coursework.easyauction.exceptions.LotNotFoundException;
import ru.skypro.coursework.easyauction.exceptions.LotStatusException;
import ru.skypro.coursework.easyauction.model.Bid;
import ru.skypro.coursework.easyauction.model.Lot;
import ru.skypro.coursework.easyauction.model.dto.*;
import ru.skypro.coursework.easyauction.repository.BidRepository;
import ru.skypro.coursework.easyauction.repository.LotRepository;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LotServiceImpl implements LotService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LotServiceImpl.class);

    private final LotRepository lotRepository;

    private final BidRepository bidRepository;

    public LotServiceImpl(LotRepository lotRepository, BidRepository bidRepository) {
        this.lotRepository = lotRepository;
        this.bidRepository = bidRepository;
    }

    @Override
    public void createLot(CreateLot createLot) {
        Lot lot = createLot.toLot();
        lotRepository.save(lot);
        LOGGER.info("Lot was created: {}", lot);
        LOGGER.debug("Database was updated");
    }

    @Override
    public void startBidding(int id) {
        Lot lot = getLotById(id);
        try {
            if (lot.getStatus().equals("Stopped")) {
                throw new LotStatusException();
            }
        } catch (LotStatusException e) {
            LOGGER.error("Lot with id = {} has been stopped.", id);
            throw new LotStatusException();
        }
        lot.setStatus("Started");
        lotRepository.save(lot);
        LOGGER.info("Lot with id = {} was started, lot status was changed to {}: {}", id, lot.getStatus(), lot);
    }

    @Override
    public BidDTO bid(int id, CreateBid createBid) {
        Bid bid = createBid.toBid();
        Lot lot = getLotById(id);
        try {
            if (!lot.getStatus().equals("Started")) {
                throw new LotStatusException();
            }
        } catch (LotStatusException e) {
            LOGGER.error("Lot with id = {} hasn't been started or stopped.", id);
            throw new LotStatusException();
        }
        String name = bid.getBidderName();
        LocalDateTime localDateTime = bid.getBidDate();
        bidRepository.createBid(name, localDateTime, id);
        LOGGER.info("Bid was created: {}", BidDTO.fromBid(bid));
        LOGGER.debug("Database was updated");
        return BidDTO.fromBid(bid);
    }

    @Override
    public void stopBidding(int id) {
        Lot lot = getLotById(id);
        try {
            if (lot.getStatus().equals("Created")) {
                throw new LotStatusException();
            }
        } catch (LotStatusException e) {
            LOGGER.error("Lot with id = {} hasn't been started: {}", id, lot);
            throw new LotStatusException();
        }
        lot.setStatus("Stopped");
        lotRepository.save(lot);
        LOGGER.info("Lot with id = {} was stopped", id);
        LOGGER.debug("Database was updated");
    }

    @Override
    public BidDTO getFirstBidder(int id) {
        checkLotExistenceById(id);
        Bid bid = bidRepository.getFirstBidder(id);
        checkBidExistence(bid);
        BidDTO bidDTO = BidDTO.fromBid(bid);
        LOGGER.info("First bidder for lot id = {} was found: {}", id, bidDTO);
        return bidDTO;
    }

    @Override
    public BidDTO getMostFrequentBidder(int id) {
        checkLotExistenceById(id);
        Bid bid = bidRepository.getMostFrequentBidder(id);
        checkBidExistence(bid);
        BidDTO bidDTO = BidDTO.fromBid(bid);
        LOGGER.info("Most frequent bidder for lot id = {} was found: {}", id, bidDTO);
        return bidDTO;
    }

    @Override
    public FullLotDTO getFullLot(int id) {
        Lot lot = getLotById(id);
        FullLotDTO fullLotDTO = FullLotDTO.fromLot(lot);
        LOGGER.info("Lot with id = {} was found: {}", id, fullLotDTO);
        return fullLotDTO;
    }

    @Override
    public List<LotDTO> findLots(Integer pageIndex, String status) {
        Pageable lotsOfConcretePage = PageRequest.of(Objects.requireNonNullElse(pageIndex, 0), 10);
        List<LotDTO> lotDTOs = lotRepository.findAllByStatus(lotsOfConcretePage, status).stream()
                .map(LotDTO::fromLot).collect(Collectors.toList());
        LOGGER.info("Page {} with employees was found: {}", pageIndex, lotDTOs);
        return lotDTOs;
    }

    @Override
    public ResponseEntity<Resource> getCSVFile() throws IOException {
        List<CSVLot> csvLots = lotRepository.findAllLots().stream()
                .map(CSVLot::fromLot)
                .toList();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String[] HEADERS = {"id", "title", "status", "lastBidderName", "currentPrice"};
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(HEADERS)
                .build();
        CSVPrinter printer = new CSVPrinter(new PrintWriter(out), csvFormat);
        csvLots.forEach(csvLot -> {
            try {
                printer.printRecord(csvLot.getId(), csvLot.getTitle(), csvLot.getStatus(),
                        csvLot.getLastBidderName(), csvLot.getCurrentPrice());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        printer.flush();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        InputStreamResource file = new InputStreamResource(in);
        ResponseEntity<Resource> response = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"lots.csv\"")
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
        LOGGER.info("File was downloaded: {}", response);
        return response;
    }

    public Lot getLotById(int id) {
        try {
            return lotRepository.findById(id).orElseThrow(LotNotFoundException::new);
        } catch (LotNotFoundException e) {
            LOGGER.error("Lot with id = {} is not found", id, e);
            throw new LotNotFoundException();
        }
    }

    public void checkLotExistenceById(int id) {
        try {
            if (!lotRepository.existsById(id)) {
                throw new LotNotFoundException();
            }
        } catch (LotNotFoundException e) {
            LOGGER.error("Lot with id = {} is not found", id, e);
            throw new LotNotFoundException();
        }
    }

    public void checkBidExistence(Bid bid) {
        try {
            if (bid == null) {
                throw new BidNotFoundException();
            }
        } catch (BidNotFoundException e) {
            LOGGER.error("Bids are not found", e);
            throw new BidNotFoundException();
        }
    }
}
