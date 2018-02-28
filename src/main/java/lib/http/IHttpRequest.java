package lib.http;

import org.json.JSONObject;

public interface IHttpRequest {

    /**
     * Sets the request URL.
     * @param url
     */
    AHttpRequest setUrl(String url) throws Exception;

    /**
     * Adds a query parameter to the URL
     * @param key
     * @param value
     * @return
     */
    AHttpRequest addQueryParameter(String key, String value) throws Exception;

    /**
     * Sets request method.
     * @param method
     */
    AHttpRequest setMethod(String method) throws Exception;

    /**
     * Adds content-type header
     * to the list of headers.
     * @param contentType
     */
    AHttpRequest setContentType(String contentType);

    /**
     * Adds a header.
     * @param fieldName the name of the header field
     * @param fieldValue the value od the header field
     */
    AHttpRequest addHeader(String fieldName, String fieldValue) throws Exception;

    /**
     * Adds data to be sent
     * @param data
     * @return
     */
    AHttpRequest addBody(JSONObject data);

    /**
     * Sends the request
     */
    void send() throws Exception;

    /**
     * Gives the response
     * as raw string.
     * @return
     */
    String getResponseRaw();

    /**
     * Gives the response encoded
     * in JSON object.
     * @return
     */
    JSONObject getResponseAsJson();

    /**
     * Gives back json object wrap with
     * HttpResponse object, containing
     * information about the request.
     * @return
     */
    HttpResponse getResponseWithInfo();
}
