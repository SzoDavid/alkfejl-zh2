package hu.inf.szte.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStockReq {
    private String name;
    private String simpleName;
    private Double price;
    private Integer shares;
}
