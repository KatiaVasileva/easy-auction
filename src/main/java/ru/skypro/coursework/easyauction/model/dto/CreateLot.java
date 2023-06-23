package ru.skypro.coursework.easyauction.model.dto;

import lombok.*;
import ru.skypro.coursework.easyauction.model.Lot;

@Getter
@Setter
@ToString

public class CreateLot {
    private String title;
    private String description;
    private int startPrice;
    private int bidPrice;

    public Lot toLot() {
        Lot lot = new Lot();
        lot.setTitle(this.getTitle());
        lot.setDescription(this.getDescription());
        lot.setStartPrice(this.getStartPrice());
        lot.setBidPrice(this.getBidPrice());
        return lot;
    }
}
