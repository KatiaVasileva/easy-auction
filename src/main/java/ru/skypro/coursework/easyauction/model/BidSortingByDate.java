package ru.skypro.coursework.easyauction.model;

import java.util.Comparator;

public class BidSortingByDate implements Comparator<Bid> {

    @Override
    public int compare(Bid o1, Bid o2) {
        if (o1.getBidDate().isAfter(o2.getBidDate())) {
            return 1;
        } else if (o1.getBidDate().isBefore(o2.getBidDate())) {
            return -1;
        }
        return 0;
    }
}
