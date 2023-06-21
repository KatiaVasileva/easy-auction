package ru.skypro.coursework.easyauction.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import ru.skypro.coursework.easyauction.model.dto.BidDTO;
import ru.skypro.coursework.easyauction.model.dto.CreateBid;
import ru.skypro.coursework.easyauction.model.dto.CreateLot;
import ru.skypro.coursework.easyauction.model.dto.LotDTO;
import ru.skypro.coursework.easyauction.model.projections.FullLot;

import java.io.IOException;
import java.util.List;

public interface LotService {

    void createLot(CreateLot createLot);

    void startBidding(int id);

    void bid(int id, CreateBid bid);

    void stopBidding(int id);

    BidDTO getFirstBidder(int id);

    BidDTO getMostFrequentBidder(int id);

    FullLot getFullLot(int id);

    List<LotDTO> findLots(Integer pageIndex, String status);

    ResponseEntity<Resource> getCSVFile() throws IOException;
}
