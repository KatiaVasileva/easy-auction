package ru.skypro.coursework.easyauction.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.skypro.coursework.easyauction.model.Bid;

@Getter
@Setter

public class CreateBid {

    @NotBlank(message = "Please enter the name of the bidder. This field is mandatory.")
    private String bidderName;

    public Bid toBid() {
        Bid bid = new Bid();
        bid.setBidderName(this.bidderName);
        return bid;
    }
}
