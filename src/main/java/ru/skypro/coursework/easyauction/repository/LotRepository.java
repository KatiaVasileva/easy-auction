package ru.skypro.coursework.easyauction.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.skypro.coursework.easyauction.model.Lot;

import java.util.List;

public interface LotRepository extends CrudRepository<Lot, Integer> {

    @Query(value = "SELECT l FROM Lot l WHERE l.status = ?1")
    List<Lot> findAllByStatus(Pageable lotsOfConcretePage, String status);

    @Query(value = "SELECT l FROM Lot l")
    List<Lot> findAllLots();
}

