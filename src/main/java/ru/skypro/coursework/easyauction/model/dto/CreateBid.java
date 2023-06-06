package ru.skypro.coursework.easyauction.model.dto;

import lombok.Getter;
import lombok.Setter;
import ru.skypro.coursework.easyauction.model.Bid;

@Getter
@Setter

public class CreateBid {
    private String bidderName;

    public Bid toBid() {
        Bid bid = new Bid();
        bid.setBidderName(this.bidderName);
        return bid;
    }
}
