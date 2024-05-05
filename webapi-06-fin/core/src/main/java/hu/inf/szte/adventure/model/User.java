package hu.inf.szte.adventure.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {

    private Long id;
    private String username;
    private String email;
    private String password;
}
