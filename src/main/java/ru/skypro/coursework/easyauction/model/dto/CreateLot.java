package ru.skypro.coursework.easyauction.model.dto;

import lombok.*;
import ru.skypro.coursework.easyauction.model.Lot;

@Getter
@Setter
@NoArgsConstructor
@ToString

public class CreateLot {
    private String title;
    private String description;
    private int startPrice;
    private int bidPrice;

    public CreateLot(String title, String description, int startPrice, int bidPrice) {
        this.title = title;
        this.description = description;
        this.startPrice = startPrice;
        this.bidPrice = bidPrice;
    }

    public static CreateLot fromLot(Lot lot) {
        CreateLot createLot = new CreateLot();
        createLot.setTitle(lot.getTitle());
        createLot.setDescription(lot.getDescription());
        createLot.setStartPrice(lot.getStartPrice());
        createLot.setBidPrice(lot.getBidPrice());
        return createLot;
    }

    public Lot toLot() {
        Lot lot = new Lot();
        lot.setTitle(this.getTitle());
        lot.setDescription(this.getDescription());
        lot.setStartPrice(this.getStartPrice());
        lot.setBidPrice(this.getBidPrice());
        return lot;
    }
}
