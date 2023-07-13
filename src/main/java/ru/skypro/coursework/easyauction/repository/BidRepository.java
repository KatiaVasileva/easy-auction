package ru.skypro.coursework.easyauction.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.skypro.coursework.easyauction.model.Bid;

import java.time.LocalDateTime;

public interface BidRepository extends CrudRepository<Bid, Integer> {

    @Query(value = "INSERT INTO bids (bidder_name, bid_date, lot_id) " +
            "VALUES (?1, ?2, ?3) RETURNING (bidder_name, bid_date, lot_id);",
            nativeQuery = true)
    void createBid(String name, LocalDateTime localDateTime, int id);

    @Query(value = "SELECT new ru.skypro.coursework.easyauction.model.Bid(b.bidderName, b.bidDate) " +
            "FROM Bid b WHERE b.lot.id = ?1 GROUP BY b.bidderName, b.bidDate " +
            "HAVING b.bidDate = (SELECT MIN(b.bidDate) FROM Bid b WHERE b.lot.id = ?1)")
    Bid getFirstBidder(int id);

    @Query(value = "SELECT new ru.skypro.coursework.easyauction.model.Bid(b.bidderName, MAX(b.bidDate)) " +
            "FROM Bid b WHERE b.lot.id = ?1 GROUP BY b.bidderName " +
            "ORDER BY COUNT(b.bidderName) DESC, MAX(b.bidDate) DESC LIMIT 1")
    Bid getMostFrequentBidder(int id);
}
