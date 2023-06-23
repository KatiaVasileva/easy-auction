package ru.skypro.coursework.easyauction.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skypro.coursework.easyauction.utilities.Utilities;
import ru.skypro.coursework.easyauction.model.Lot;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString

public class FullLotDTO extends CSVLot {

    private int id;
    private String title;
    private String status;
    private String description;
    private int startPrice;
    private int bidPrice;
    private int currentPrice;
    private String lastBidderName;
    private LocalDateTime lastBidDate;

    public static FullLotDTO fromLot(Lot lot) {
        FullLotDTO fullLotDTO = new FullLotDTO();
        fullLotDTO.setId(lot.getId());
        fullLotDTO.setTitle(lot.getTitle());
        fullLotDTO.setStatus(lot.getStatus());
        fullLotDTO.setDescription(lot.getDescription());
        fullLotDTO.setStartPrice(lot.getStartPrice());
        fullLotDTO.setBidPrice(lot.getBidPrice());
        fullLotDTO.setCurrentPrice(Utilities.getCurrentPrice(lot));
        fullLotDTO.setLastBidderName(Utilities.getLastBidderName(lot));
        fullLotDTO.setLastBidDate(Utilities.getLastBidDate(lot));
        return fullLotDTO;
    }
}
