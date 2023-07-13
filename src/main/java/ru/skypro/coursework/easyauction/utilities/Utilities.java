package ru.skypro.coursework.easyauction.utilities;

import ru.skypro.coursework.easyauction.model.Bid;
import ru.skypro.coursework.easyauction.model.BidSortingByDate;
import ru.skypro.coursework.easyauction.model.Lot;

import java.time.LocalDateTime;
import java.util.List;

public class Utilities {

    public static int getCurrentPrice(Lot lot) {
        List<Bid> bids = lot.getBids();
        return bids.isEmpty() ? lot.getStartPrice() : bids.size() * lot.getBidPrice() + lot.getStartPrice();
    }

    public static String getLastBidderName(Lot lot) {
        List<Bid> bids = lot.getBids();
        return bids.stream()
                .sorted(new BidSortingByDate())
                .map(Bid::getBidderName)
                .findFirst().orElse("No bids");
    }

    public static LocalDateTime getLastBidDate(Lot lot) {
        List<Bid> bids = lot.getBids();
        return bids.stream()
                .sorted(new BidSortingByDate())
                .map(Bid::getBidDate)
                .findFirst().orElse(null);
    }
}
