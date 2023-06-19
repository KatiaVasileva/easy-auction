package ru.skypro.coursework.easyauction.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skypro.coursework.easyauction.exceptions.BidNotFoundException;
import ru.skypro.coursework.easyauction.exceptions.LotNotFoundException;
import ru.skypro.coursework.easyauction.exceptions.LotStatusException;
import ru.skypro.coursework.easyauction.model.Bid;
import ru.skypro.coursework.easyauction.model.BidSortingByDate;
import ru.skypro.coursework.easyauction.model.Lot;
import ru.skypro.coursework.easyauction.model.dto.BidDTO;
import ru.skypro.coursework.easyauction.model.dto.CreateBid;
import ru.skypro.coursework.easyauction.model.dto.CreateLot;
import ru.skypro.coursework.easyauction.model.dto.LotDTO;
import ru.skypro.coursework.easyauction.model.projections.FullLot;
import ru.skypro.coursework.easyauction.repository.LotRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LotServiceImpl implements LotService {

    private final LotRepository lotRepository;

    public LotServiceImpl(LotRepository lotRepository) {
        this.lotRepository = lotRepository;
    }

    @Override
    public void createLot(CreateLot createLot) {
        Lot lot = createLot.toLot();
        lotRepository.save(lot);
    }

    @Override
    public void startBidding(int id) {
        Lot lot = getLotByID(id);
        if (lot.getStatus().equals("Stopped")) {
            throw new LotStatusException();
        }
        lot.setStatus("Started");
        lotRepository.save(lot);
    }

    private Lot getLotByID(int id) {
        return lotRepository.findById(id).orElseThrow(LotNotFoundException::new);
    }

    @Override
    public void bid(int id, CreateBid createBid) {
        Bid bid = createBid.toBid();
        Lot lot = getLotByID(id);
        if (!lot.getStatus().equals("Started")) {
            throw new LotStatusException();
        }
        String name = bid.getBidderName();
        LocalDateTime localDateTime = bid.getBidDate();
        lotRepository.createBid(name, localDateTime, id);
    }

    @Override
    public void stopBidding(int id) {
        Lot lot = getLotByID(id);
        if (lot.getStatus().equals("Created")) {
            throw new LotStatusException();
        }
        lot.setStatus("Stopped");
        lotRepository.save(lot);
    }

    @Override
    public BidDTO getFirstBidder(int id) {
        lotRepository.findById(id).orElseThrow(LotNotFoundException::new);
        Bid bid = lotRepository.getFirstBidder(id);
        if (bid == null) {
            throw new BidNotFoundException();
        }
        return BidDTO.fromBid(bid);
    }

    @Override
    public BidDTO getMostFrequentBidder(int id) {
        lotRepository.findById(id).orElseThrow(LotNotFoundException::new);
        Bid bid = lotRepository.getMostFrequentBidder(id);
        if (bid == null) {
            throw new BidNotFoundException();
        }
        return BidDTO.fromBid(bid);
    }

    @Override
    public FullLot getFullLot(int id) {
        FullLot fullLot = new FullLot();
        Lot lot = getLotByID(id);
        fullLot.setId(lot.getId());
        fullLot.setStatus(lot.getStatus());
        fullLot.setTitle(lot.getTitle());
        fullLot.setDescription(lot.getDescription());
        fullLot.setStartPrice(lot.getStartPrice());
        fullLot.setBidPrice(lot.getBidPrice());
        int bidNumber = lotRepository.getBidNumber(id);
        int currentPrice = bidNumber == 0 ? lot.getStartPrice() : lotRepository.getCurrentPrice(bidNumber, id);
        fullLot.setCurrentPrice(currentPrice);
        Bid bid = lotRepository.getLastBid(id);
        if (bid == null) {
            fullLot.setLastBidderName(null);
            fullLot.setLastBidDate(null);
        } else {
            fullLot.setLastBidderName(bid.getBidderName());
            fullLot.setLastBidDate(bid.getBidDate());
        }
        return fullLot;
    }

    @Override
    public List<LotDTO> findLots(Integer pageIndex, String status) {
        Pageable lotsOfConcretePage = PageRequest.of(Objects.requireNonNullElse(pageIndex, 0), 10);
        return lotRepository.findAllByStatus(lotsOfConcretePage, status).stream()
                .map(LotDTO::fromLot).collect(Collectors.toList());
    }
}
