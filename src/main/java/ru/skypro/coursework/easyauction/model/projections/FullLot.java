package ru.skypro.coursework.easyauction.model.projections;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class FullLot {

    private int id;
    private String status;
    private String title;
    private String description;
    private int startPrice;
    private int bidPrice;
    private int currentPrice;
    private String lastBidderName;
    private LocalDateTime lastBidDate;

    public FullLot(int id,
                   String status,
                   String title,
                   String description,
                   int startPrice,
                   int bidPrice,
                   int currentPrice,
                   String lastBidderName,
                   LocalDateTime lastBidDate) {
        this.id = id;
        this.status = status;
        this.title = title;
        this.description = description;
        this.startPrice = startPrice;
        this.bidPrice = bidPrice;
        this.currentPrice = currentPrice;
        this.lastBidderName = lastBidderName;
        this.lastBidDate = lastBidDate;
    }
}
