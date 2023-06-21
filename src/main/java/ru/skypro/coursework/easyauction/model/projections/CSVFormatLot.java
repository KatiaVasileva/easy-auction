package ru.skypro.coursework.easyauction.model.projections;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class CSVFormatLot {
    private int id;
    private String title;
    private String status;
    private String lastBidderName;
    private int currentPrice;

    public CSVFormatLot(int id, String title, String status, String lastBidderName, int currentPrice) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.lastBidderName = lastBidderName;
        this.currentPrice = currentPrice;
    }
}
