package hu.inf.szte.adventure.model;

import java.io.Serializable;
import java.math.BigDecimal;

public record Preferences(
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Integer minPopularity) implements Serializable {
}
