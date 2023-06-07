package ru.skypro.coursework.easyauction.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

@Entity
@Table(name = "bids")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "bidder_name")
    @NotBlank(message = "Please enter the name of the bidder. This field is mandatory.")
    private String bidderName;

    @Column(name = "bid_date")
    private LocalDateTime bidDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private Lot lot;

    public Bid(String bidderName) {
        this.bidderName = bidderName;
    }

    public Bid(String bidderName, LocalDateTime bidDate) {
        this.bidderName = bidderName;
        this.bidDate = bidDate;
    }
}
