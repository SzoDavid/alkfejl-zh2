package hu.inf.szte.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ReadStockReq implements Serializable {
    private Long id;
    private String name;
    private String simpleName;
    private Double price;
    private Integer shares;
    private Double minValue;
    private Double maxValue;
}
