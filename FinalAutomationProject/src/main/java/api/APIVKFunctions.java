package api;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URISyntaxException;

public class APIVKFunctions {
    private URIBuilder builder;
    private String token = "7272addc2b4916109214e9e04daa51bfba546272c12ca0033882312802df2775d575fdd7e64d400164a65";
    private String owner = "244271034";
    private String version = "5.101";

    public URIBuilder createBasicURL(String methodName) throws URISyntaxException {
        String urlName = String.format("https://api.vk.com/method/wall.%s?", methodName);
        builder = new URIBuilder(urlName);
        builder.setParameter("access_token", token)
                .setParameter("owner_id", owner)
                .setParameter("v", version);
        return builder;
    }

    public URIBuilder postMessage(String message) throws URISyntaxException {
        URIBuilder builder = createBasicURL("post");
        builder.setParameter("message", message);
        return builder;
    }

    public URIBuilder deleteMessage(String post_id) throws URISyntaxException {
        URIBuilder builder = createBasicURL("delete");
        builder.setParameter("post_id", post_id);
        return builder;
    }

    public URIBuilder editMessage(String post_id, String message) throws URISyntaxException {
        URIBuilder builder = createBasicURL("edit");
        builder.setParameter("post_id", post_id);
        builder.setParameter("message", message);
        return builder;
    }

    public URIBuilder getAllPosts() throws URISyntaxException {
        URIBuilder builder = createBasicURL("get");
        return builder;
    }

    public HttpResponse execute(URIBuilder builder) throws URISyntaxException, IOException {
        HttpGet request = new HttpGet(builder.build());
        HttpClient client = HttpClientBuilder.create().build();
        return client.execute(request);
    }
}
