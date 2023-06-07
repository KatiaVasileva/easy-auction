package ru.skypro.coursework.easyauction.model.dto;

import lombok.*;
import ru.skypro.coursework.easyauction.model.Lot;

@Getter
@Setter

public class LotDTO {

    private int id;
    private String status;
    private String title;
    private String description;
    private int startPrice;
    private int bidPrice;

    public static LotDTO fromLot(Lot lot) {
        LotDTO lotDTO = new LotDTO();
        lotDTO.setId(lot.getId());
        lotDTO.setStatus(lot.getStatus());
        lotDTO.setTitle(lot.getTitle());
        lotDTO.setDescription(lot.getDescription());
        lotDTO.setStartPrice(lot.getStartPrice());
        lotDTO.setBidPrice(lot.getBidPrice());
        return lotDTO;
    }
}
