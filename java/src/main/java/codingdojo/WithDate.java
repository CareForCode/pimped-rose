package codingdojo;

import java.util.Date;
import java.util.function.Function;

public class WithDate<T> {
    public final Date timestamp;
    public final T value;

    public WithDate(Date timestamp, T value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public static <T, U> Function<WithDate<T>, WithDate<U>> lift(Function<T, U> f) {
        return (WithDate<T> value) -> {
            return new WithDate<>(value.timestamp, f.apply(value.value));
        };
    }
}
