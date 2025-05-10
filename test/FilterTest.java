import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilterTest {

    @Test
    void filtersOutNullValues() {
        List<String> input = Stream.of("A", null, "B", null, "C").collect(Collectors.toList());
        List<String> result = input.stream().filter(value -> value != null).collect(Collectors.toList());

        assertEquals(List.of("A", "B", "C"), result);
    }

    @Test
    void filtersOutEmptyStrings() {
        List<String> input = Stream.of("A", "", "B", "", "C").collect(Collectors.toList());
        List<String> result = input.stream().filter(value -> !value.isEmpty()).collect(Collectors.toList());

        assertEquals(List.of("A", "B", "C"), result);
    }

    @Test
    void filtersOutValuesBelowThreshold() {
        List<Integer> input = Stream.of(1, 5, 10, 15, 20).collect(Collectors.toList());
        List<Integer> result = input.stream().filter(value -> value >= 10).collect(Collectors.toList());

        assertEquals(List.of(10, 15, 20), result);
    }

    @Test
    void filtersOutNegativeNumbers() {
        List<Integer> input = Stream.of(-10, -5, 0, 5, 10).collect(Collectors.toList());
        List<Integer> result = input.stream().filter(value -> value >= 0).collect(Collectors.toList());

        assertEquals(List.of(0, 5, 10), result);
    }
}