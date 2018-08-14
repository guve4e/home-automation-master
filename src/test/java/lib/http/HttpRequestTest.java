package lib.http;

import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;

import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class HttpRequestTest {

    @Mock
    private HttpURLConnection mockHttpURLConnection;

    @Mock
    private InputStream mockInputStream;

    @Before
    public void setUp() throws Exception {
        // check if class is wrapped with Runner
        assertNotNull(mockHttpURLConnection);

        this.mockInputStream = IOUtils.toInputStream(
                "{\r \"data\":{\r\"  \"controller\":\"Test\",\r  \"method\":\"GET\",\r  \"id\":\"1001\"\r },\r   \"key\":\"value\"\r}\r",
                "UTF-8");
        when(mockHttpURLConnection.getInputStream()).thenReturn(mockInputStream);
    }

    @Test
    public void testIdempotent() throws Exception {
        // Arrange
        String expectedResponse = "{\r \"data\":{\r\"  \"controller\":\"Test\",\r  \"method\":\"GET\",\r  \"id\":\"1001\"\r },\r   \"key\":\"value\"\r}\r";

        // Act
        HttpRequest request = new HttpRequest(mockHttpURLConnection);
        request.setContentType("application/json") // missing setting netUrl, because the connection is mocked
                .setMethod("GET")
                .send();

        String actualResponse = request.getResponseRaw();

        // Assert
        TestCase.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testNotIdempotent() throws Exception {
        // Arrange

        JSONObject objectToSend = new JSONObject();

        objectToSend.put("name", "foo");
        objectToSend.put("num", Integer.valueOf(100));
        objectToSend.put("balance", new Double(1000.21));
        objectToSend.put("is_vip", new Boolean(true));

        // Act
        HttpRequest request = new HttpRequest();
        request.setUrl("lib.http://webapi.ddns.net/index.php/mockcontroller/1001")
                .setContentType("application/json")
                .setMethod("POST")
                .addBody(objectToSend)
                .send();

        String expectedStringToSend = objectToSend.toString();

        Field field = AHttpRequest.class.getDeclaredField("dataRaw");
        field.setAccessible(true);
        String actualStringToSend = (String) field.get(request);

        // Assert
        TestCase.assertEquals(expectedStringToSend, actualStringToSend);
    }

    @Test
    public void testProperSettingOfQueryParameter() throws Exception {
        // Arrange
        String expectedUrl = "https://example.com/page?name=ferret&color=purple";

        // Act
        HttpRequest request = new HttpRequest();
        request.setUrl("https://example.com/page?")
                .addQueryParameter("name", "ferret")
                .addQueryParameter("color", "purple");

        Field field = request.getClass().getSuperclass().getDeclaredField("url");
        field.setAccessible(true);
        String actualUrl = (String) field.get(request);
        // Assert
        TestCase.assertEquals(expectedUrl, actualUrl);
    }

    @Test(expected = Exception.class)
    public void testWhenSetMethodsAreCalledBeforeSetUrl() throws Exception {
        IHttpRequest request = new HttpRequest();
        request.addQueryParameter("key", "value")
                .setMethod("POST");
    }
}