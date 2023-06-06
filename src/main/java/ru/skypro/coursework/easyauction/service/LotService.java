package ru.skypro.coursework.easyauction.service;

import ru.skypro.coursework.easyauction.model.dto.BidDTO;
import ru.skypro.coursework.easyauction.model.dto.CreateBid;
import ru.skypro.coursework.easyauction.model.dto.CreateLot;
import ru.skypro.coursework.easyauction.model.projections.FullLot;

public interface LotService {

    void createLot(CreateLot createLot);

    void startBidding(int id);

    void bid(int id, CreateBid bid);

    void stopBidding(int id);

    BidDTO getFirstBidder(int id);

    BidDTO getMostFrequentBidder(int id);

    FullLot getFullLot(int id);
}
