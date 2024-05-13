package hu.inf.szte.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Dummy {

    // Primary key
    private Long id;
    // Some string field
    private String text;
    // Some integer field
    private Integer numInt;
    // Some double field
    private Double numDouble;
    // Some boolean field
    private Boolean bool;
}
