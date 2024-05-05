package hu.inf.szte.adventure.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateSightReq {

    private String name;
    private BigDecimal price;
    private Integer openingHour;
    private Integer closingHour;
    private String description;
    private Integer popularity;
}
