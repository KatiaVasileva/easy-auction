package ru.skypro.coursework.easyauction.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.skypro.coursework.easyauction.utilities.Utilities;
import ru.skypro.coursework.easyauction.model.Lot;

@Getter
@Setter
@ToString
@RequiredArgsConstructor

public class CSVLot {
    private int id;
    private String title;
    private String status;
    private String lastBidderName;
    private int currentPrice;

    public static CSVLot fromLot(Lot lot) {
        CSVLot csvLot = new CSVLot();
        csvLot.setId(lot.getId());
        csvLot.setTitle(lot.getTitle());
        csvLot.setStatus(lot.getStatus());
        csvLot.setCurrentPrice(Utilities.getCurrentPrice(lot));
        csvLot.setLastBidderName(Utilities.getLastBidderName(lot));
        return csvLot;
    }
}
