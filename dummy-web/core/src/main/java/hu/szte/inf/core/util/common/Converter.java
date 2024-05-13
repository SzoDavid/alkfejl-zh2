package hu.szte.inf.core.util.common;

import java.util.List;
import java.util.stream.StreamSupport;

public final class Converter {

    /**
     * Does what it says.
     *
     * @param iterable Any iterable object
     * @return List of elements in the iterable object.
     * Order of elements depends on the implementation of the iterable.
     * @param <T> Type param, that should be deduced automatically.
     */
    public static <T> List<T> iterableToList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).toList();
    }
}
