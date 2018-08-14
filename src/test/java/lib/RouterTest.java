package lib;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class RouterTest {

    // TODO
    @Test
    public void testRoute() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String[] arr = new String[2];
        arr[0] = "A";
        arr[1] = "B";

            new Router()
                    .setClassName("User")
                    .setMethodName("foo")
                    .setParameters(arr)
                    .route();
    }
}