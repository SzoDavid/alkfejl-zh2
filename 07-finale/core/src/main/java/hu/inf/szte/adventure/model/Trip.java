package hu.inf.szte.adventure.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Trip {

    private Long id;
    private String name;
    private Boolean halfBoard;
    private Integer numGuests;
    private Integer numNights;
    private String description;
}
