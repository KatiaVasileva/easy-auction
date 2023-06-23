package ru.skypro.coursework.easyauction.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString

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

    public Bid(String bidderName, LocalDateTime bidDate) {
        this.bidderName = bidderName;
        this.bidDate = bidDate;
    }
}
