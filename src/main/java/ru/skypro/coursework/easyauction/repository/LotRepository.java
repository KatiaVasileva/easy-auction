package ru.skypro.coursework.easyauction.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.skypro.coursework.easyauction.model.Bid;
import ru.skypro.coursework.easyauction.model.Lot;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public interface LotRepository extends CrudRepository<Lot, Integer> {

    @Query(value = "INSERT INTO bids (bidder_name, bid_date, lot_id) " +
            "VALUES (?1, ?2, ?3) RETURNING (bidder_name, bid_date, lot_id);",
            nativeQuery = true)
    void createBid(String name, LocalDateTime localDateTime, int id);

    @Query(value = "SELECT new ru.skypro.coursework.easyauction.model.Bid(b.bidderName, b.bidDate) " +
            "FROM Bid b WHERE b.lot.id = ?1 GROUP BY b.bidderName, b.bidDate " +
            "HAVING b.bidDate = (SELECT MIN(b.bidDate) FROM Bid b WHERE b.lot.id = ?1)")
    Bid getFirstBidder(int id);

    @Query(value = "SELECT new ru.skypro.coursework.easyauction.model.Bid(b.bidderName, b.bidDate) " +
            "FROM Bid b WHERE b.lot.id = ?1 GROUP BY b.bidderName, b.bidDate " +
            "HAVING b.bidDate = (SELECT MAX(b.bidDate) FROM Bid b WHERE b.lot.id = ?1)")
    Bid getLastBid(int id);

    @Query(value = "SELECT COUNT(bids) FROM bids WHERE lot_id = ?1", nativeQuery = true)
    int getBidNumber(int id);

    @Query(value = "SELECT DISTINCT (?1 * lots.bid_price + lots.start_price) " +
            "FROM lots JOIN bids ON bids.lot_id = lots.id WHERE bids.lot_id = ?2", nativeQuery = true)
    int getCurrentPrice(int bidNumber, int id);

    @Query(value = "SELECT bidder_name, MAX(bid_date) FROM bids WHERE lot_id = ?1 GROUP BY bidder_name " +
            "ORDER BY COUNT(bidder_name) DESC, MAX(bid_date) DESC LIMIT 1", nativeQuery = true)
    Bid getMostFrequentBidder(int id);

    @Query(value = "SELECT * FROM lots WHERE status = ?1", nativeQuery = true)
    List<Lot> findAllByStatus(Pageable lotsOfConcretePage, String status);
}

