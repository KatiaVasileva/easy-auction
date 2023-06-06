package ru.skypro.coursework.easyauction.model.dto;

import lombok.*;
import ru.skypro.coursework.easyauction.model.Bid;

import java.time.LocalDateTime;

@Getter
@Setter

public class BidDTO {
    private String bidderName;
    private LocalDateTime bidDate;

    public static BidDTO fromBid(Bid bid) {
        BidDTO bidDTO = new BidDTO();
        bidDTO.setBidderName(bid.getBidderName());
        bidDTO.setBidDate(bid.getBidDate());
        return bidDTO;
    }

    public Bid toBid() {
        Bid bid = new Bid();
        bid.setBidderName(this.bidderName);
        bid.setBidDate(this.bidDate);
        return bid;
    }
}
