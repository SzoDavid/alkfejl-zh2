package hu.szte.inf.model;

/**
 * Specialized model for a create request.
 *
 * @param someString
 * @param anotherString
 * @param someBool
 * @param someInt
 * @param otherInt
 */
public record CreateReq(
        String someString,
        String anotherString,
        Boolean someBool,
        Integer someInt,
        Integer otherInt
) {
}
