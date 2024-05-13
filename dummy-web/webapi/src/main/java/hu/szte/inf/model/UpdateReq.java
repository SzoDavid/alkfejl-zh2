package hu.szte.inf.model;

/**
 * Specialized model for an update request.
 *
 * @param id
 * @param someString
 * @param anotherString
 * @param someBool
 * @param someInt
 * @param otherInt
 */
public record UpdateReq(
        Long id,
        String someString,
        String anotherString,
        Boolean someBool,
        Integer someInt,
        Integer otherInt
) {
}
