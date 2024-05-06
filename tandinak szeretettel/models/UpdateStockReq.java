package hu.inf.szte.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStockReq {
    private Long id;
    private String name;
    private String simpleName;
    private Double price;
    private Integer shares;
}
