package lib.http;

import org.json.JSONObject;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class HttpRequest extends AHttpRequest{

    /**
     *
     */
    private HttpURLConnection connection = null;

    /**
     *
     */
    private boolean test = false;

    /**
     *
     */
    private int queryParametersCount = 0;

    private void sendData() throws Exception {
        if(dataRaw.isEmpty())
            throw new Exception("Data to be sent has to be set first!");

        if (!this.test)
        {
            // send data
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
            wr.writeBytes(dataRaw);
            wr.close();
        }
    }

    private void setUpConnection() throws Exception {
        if (connection == null)
            throw new Exception("Null connection!");

        connection.setUseCaches(false);
        connection.setDoOutput(true);
    }

    /**
     * Creates java.net.url object
     * and assigns connection to it
     * @param url: string representing the url
     * @throws IOException If connection can not be established
     */
    private void setNetUrl(String url) throws IOException {
        URL netUrl = new URL(url);
        this.connection = (HttpURLConnection) netUrl.openConnection();
    }

    /**
     * Constructor
     */
    public HttpRequest() {
        this.queryParameters = new HashMap<>();
        this.headers = new HashMap<>();
    }

    /**
     * Testing constructor
     * for dependency injection.
     * @param connection: HttpURLConnection
     */
    public HttpRequest(HttpURLConnection connection) {
        this.connection = connection;
        this.test = true;
    }

    @Override
    public AHttpRequest setUrl(String urlString) throws Exception {
        if (urlString.equals(""))
            throw new Exception("Bad parameters in setUrl!");

        this.url = urlString;
        setNetUrl(urlString);
        return this;
    }

    @Override
    public AHttpRequest addQueryParameter(String key, String value) throws Exception {
        if (key.equals("") || value.equals(""))
            throw new Exception("Bad parameters in addQueryParameter");

        this.queryParameters.put(key, value);

        addQueryParameterToUrl(key, value);
        setNetUrl(this.url);

        return this;
    }

    private void addQueryParameterToUrl(String key, String value) throws Exception {
        if(key.equals("") || value.equals(""))
            throw new Exception("Bad parameters in addQueryParameterToUrl");

        if (this.queryParametersCount > 0)
            this.url += "&" + key + "=" + value;
        else
            this.url += key + "=" + value;
        this.queryParametersCount++;
    }

    @Override
    public AHttpRequest setContentType(String contentType) {
        this.connection.setRequestProperty("Content-Type", contentType);
        return this;
    }

    @Override
    public AHttpRequest addHeader(String fieldName, String fieldValue) throws Exception {
        if(fieldName.equals("") || fieldValue.equals(""))
            throw new Exception("Bad parameters in addHeader()!");

        this.headers.put(fieldName, fieldValue);
        this.connection.setRequestProperty(fieldName, fieldValue);
        return this;
    }

    @Override
    public AHttpRequest addBody(JSONObject data) {
        this.dataRaw = data.toString();
        return this;
    }

    @Override
    public void send() throws Exception {

        // set up the connection first
        this.setUpConnection();

        // send that if POST, PUT, DELETE, etc
        if (!this.isIdempotent)
            this.sendData();

        // get the response
        InputStream inputStream = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+

        // collect each line
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }

        rd.close();
        this.responseString = response.toString();
    }

    @Override
    public String getResponseRaw() {
        return this.responseString;
    }

    @Override
    public JSONObject getResponseAsJson() {
        return new JSONObject(this.responseString);
    }

    @Override
    public HttpResponse getResponseWithInfo() {
        return null;
    }

    public static void main(String[] args) throws Exception {

        HttpRequest request = new HttpRequest();
        request.setUrl("https://marketdata.websol.barchart.com/getQuote.json?")
                .addQueryParameter("apikey", "d65bc398f28ba7f582f1f874a180c110")
                .addQueryParameter("symbols", "ZC*1,IBM,GOOGL,%5EEURUSD")
                .setContentType("application/json")
                .setMethod("GET")
                .send();

        // get the response as raw string
        String s = request.getResponseRaw();
        System.out.println("Raw request-> " + s);

        // or get it as json
        JSONObject jsonObject = request.getResponseAsJson();

    }
}
