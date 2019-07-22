package patterns;

import api.APIVKFunctions;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public class PostFacade {
    private APIVKFunctions functions = new APIVKFunctions();

    public String post(String text) throws URISyntaxException, IOException {
        URIBuilder builder = functions.postMessage(text);
        HttpResponse response = functions.execute(builder);
        String postId = getPostId(getAnswerString(response));
        builder = functions.getAllPosts();
        response = functions.execute(builder);
        return getAnswerString(response);
    }

    private String getPostId(String answer) {
        System.out.println(answer.substring(answer.indexOf("id") + 4, answer.indexOf("}")));
        return answer.substring(answer.indexOf("id") + 4, answer.indexOf("}"));
    }

    private String getAnswerString(HttpResponse response) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent());
        String str = "";
        int a = -1;
        while ((a = inputStreamReader.read()) != -1) {
            str += (char) a;
        }
        System.out.println(str);
        return str;
    }

    public String editPost(String text, String edit_text_on) throws URISyntaxException, IOException {
        URIBuilder builder = functions.postMessage(text);
        HttpResponse response = functions.execute(builder);
        String postId = getPostId(getAnswerString(response));
        builder = functions.editMessage(postId, edit_text_on);
        response = functions.execute(builder);
        postId = getPostId(getAnswerString(response));
        builder = functions.getAllPosts();
        response = functions.execute(builder);
        return getAnswerString(response);
    }

    public String deletePost(String text) throws URISyntaxException, IOException {
        URIBuilder builder = functions.postMessage(text);
        HttpResponse response = functions.execute(builder);
        String postId = getPostId(getAnswerString(response));
        builder = functions.deleteMessage(postId);
        response = functions.execute(builder);
        builder = functions.getAllPosts();
        response = functions.execute(builder);
        return getAnswerString(response);
    }
}
