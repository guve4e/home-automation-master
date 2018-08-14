package lib.utility;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TupleTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testFindByXGivenXExpectingY() {
        // Arrange
        Tuple<String, Integer> tuple = new Tuple<>("Some String", 78777);
        List<Tuple<String, Integer>> list = new ArrayList<Tuple<String, Integer>>() {
            {
                add(new Tuple<>("key1", 1));
                add(new Tuple<>("key2", 2));
                add(new Tuple<>("key3", 3));
                add(new Tuple<>("key4", 4));
                add(new Tuple<>("key5", 5));
                add(new Tuple<>("key6", 6));
                add(new Tuple<>("key7", 7));
                add(new Tuple<>("key8", 8));
            }
        };
        String key = "key7";
        Integer expectedValue = 7;

        // Act
        Integer actualValue = (Integer) tuple.findByX(list, key);

        // Assert
        assertEquals(actualValue, expectedValue);
    }
}