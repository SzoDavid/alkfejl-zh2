package hu.inf.szte.adventure.model;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Sight {

    private Long id;
    private String name;
    private BigDecimal price;
    private Integer openingHour;
    private Integer closingHour;
    private String description;
    private Integer popularity;
}
