package ru.skypro.coursework.easyauction.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import ru.skypro.coursework.easyauction.model.dto.*;

import java.io.IOException;
import java.util.List;

public interface LotService {

    CreateLot createLot(CreateLot createLot);

    LotDTO startBidding(int id);

    BidDTO bid(int id, CreateBid bid);

    LotDTO stopBidding(int id);

    BidDTO getFirstBidder(int id);

    BidDTO getMostFrequentBidder(int id);

    FullLotDTO getFullLot(int id);

    List<LotDTO> findLots(Integer pageIndex, String status);

    ResponseEntity<Resource> getCSVFile() throws IOException;
}
