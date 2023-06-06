package ru.skypro.coursework.easyauction.service;

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
import ru.skypro.coursework.easyauction.model.projections.FullLot;
import ru.skypro.coursework.easyauction.repository.LotRepository;

import java.time.LocalDateTime;
import java.util.List;

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
        Lot lot = lotRepository.findById(id).orElseThrow(LotNotFoundException::new);
        if (lot.getStatus().equals("Created") || lot.getStatus().equals("Started")) {
            lot.setStatus("Started");
        } else {
            throw new LotStatusException();
        }
        lotRepository.save(lot);
    }

    @Override
    public void bid(int id, CreateBid createBid) {
        Bid bid = createBid.toBid();
        Lot lot = lotRepository.findById(id).orElseThrow(LotNotFoundException::new);
        if (lot.getStatus().equals("Created") || lot.getStatus().equals("Stopped")) {
            throw new LotStatusException();
        } else {
            String name = bid.getBidderName();
            LocalDateTime localDateTime = bid.getBidDate();
            lotRepository.createBid(name, localDateTime, id);
        }
    }

    @Override
    public void stopBidding(int id) {
        Lot lot = lotRepository.findById(id).orElseThrow(LotNotFoundException::new);
        if (lot.getStatus().equals("Stopped") || lot.getStatus().equals("Started")) {
            lot.setStatus("Stopped");
        } else {
            throw new LotStatusException();
        }
        lotRepository.save(lot);
    }

    @Override
    public BidDTO getFirstBidder(int id) {
        List<Bid> bidList = lotRepository.getBidListByLotId(id);
        return bidList.stream().sorted(new BidSortingByDate())
                .map(BidDTO::fromBid)
                .findFirst().orElseThrow(BidNotFoundException::new);
    }

    @Override
    public BidDTO getMostFrequentBidder(int id) {
        Bid bid = lotRepository.getMostFrequentBidder(id);
        return BidDTO.fromBid(bid);
    }

    @Override
    public FullLot getFullLot(int id) {
        FullLot fullLot = new FullLot();
        Lot lot = lotRepository.findById(id).orElseThrow(LotNotFoundException::new);
        fullLot.setId(lot.getId());
        fullLot.setStatus(lot.getStatus());
        fullLot.setTitle(lot.getTitle());
        fullLot.setDescription(lot.getDescription());
        fullLot.setStartPrice(lot.getStartPrice());
        fullLot.setBidPrice(lot.getBidPrice());
        int bidNumber = lotRepository.getBidNumber(id);
        int currentPrice = bidNumber == 0 ? lot.getStartPrice() : lotRepository.getCurrentPrice(bidNumber, id);
        fullLot.setCurrentPrice(currentPrice);
        List<Bid> bidList = lotRepository.getBidListByLotId(id);
        Bid bid = bidList.stream().max(new BidSortingByDate()).orElse(null);
        if (bid == null) {
            fullLot.setLastBidderName(null);
            fullLot.setLastBidDate(null);
        } else {
            fullLot.setLastBidderName(bid.getBidderName());
            fullLot.setLastBidDate(bid.getBidDate());
        }
        return fullLot;
    }
}
