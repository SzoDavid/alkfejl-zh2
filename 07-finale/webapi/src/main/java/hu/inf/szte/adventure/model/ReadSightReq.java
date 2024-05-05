package hu.inf.szte.adventure.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class ReadSightReq implements Serializable {

    private Long id;
    private String name;
    private BigDecimal price;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer openingHour;
    private Integer closingHour;
    private String description;
    private Integer popularity;
}
