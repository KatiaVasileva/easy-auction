package ru.skypro.coursework.easyauction.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.skypro.coursework.easyauction.model.Bid;
import ru.skypro.coursework.easyauction.model.Lot;

import java.time.LocalDateTime;
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

    @Query(value = "SELECT new ru.skypro.coursework.easyauction.model.Bid(b.bidderName, MAX(b.bidDate)) " +
            "FROM Bid b WHERE b.lot.id = ?1 GROUP BY b.bidderName " +
            "ORDER BY COUNT(b.bidderName) DESC, MAX(b.bidDate) DESC LIMIT 1")
    Bid getMostFrequentBidder(int id);

    @Query(value = "SELECT l FROM Lot l WHERE l.status = ?1")
    List<Lot> findAllByStatus(Pageable lotsOfConcretePage, String status);

    @Query(value = "SELECT l FROM Lot l")
    List<Lot> findAllLots();
}

