package ru.skypro.coursework.easyauction.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

@Entity
@Table(name = "lots")
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String status = Status.CREATED.getStatus();

    @NotBlank(message = "Please enter the title. This field is mandatory.")
    private String title;

    @NotBlank(message = "Please enter the title. This field is mandatory.")
    private String description;

    @Positive(message = "The price shall be a positive number.")
    private int startPrice;

    @Positive(message = "The bid shall be a positive number.")
    private int bidPrice;

    @OneToMany(mappedBy = "lot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bid> bids;

    public Lot(String title, String description, int startPrice, int bidPrice) {
        this.title = title;
        this.description = description;
        this.startPrice = startPrice;
        this.bidPrice = bidPrice;
    }

}
