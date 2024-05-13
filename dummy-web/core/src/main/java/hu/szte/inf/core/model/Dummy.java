package hu.szte.inf.core.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Dummy {

    // Static database related props
    public static final String TABLE = "dummy";
    public static final String ID = "id";
    public static final String SOME_STRING = "some_string";
    public static final String ANOTHER_STRING = "another_string";
    public static final String SOME_BOOL = "some_bool";
    public static final String SOME_INT = "some_int";
    public static final String OTHER_INT = "other_int";

    // Model values
    private Long id;
    private String someString;
    private String anotherString;
    private Boolean someBool;
    private Integer someInt;
    private Integer otherInt;
}
