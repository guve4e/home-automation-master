package lib.http;

import jdk.nashorn.api.scripting.JSObject;

import java.util.HashMap;

public abstract class AHttpRequest implements IHttpRequest{

    protected String url = "";
    protected String method = null;
    protected String contentType;
    protected boolean isIdempotent;
    protected HashMap<String, String> headers;
    protected HashMap<String, String> queryParameters;
    protected HttpResponse response;
    protected String responseString = null;
    protected JSObject data = null;
    protected String dataRaw = null;

    /**
     * Sets the method
     * and decides if data is needed
     * for the request.
     * @param method type of method
     */
    @Override
    public AHttpRequest setMethod(String method) throws Exception {
//        if (this.netUrl == null)
//            throw new Exception("Set URL first!");

        this.method = method;
        // id GET is idempotent (changes state)
        this.isIdempotent = method.equals("GET");
        return this;
    }
}
