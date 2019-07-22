import api.APIVKFunctions;
import model.SettingsData;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;
import parser.Parser;
import parser.SettingParameter;
import parser.UseParser;
import patterns.PostFacade;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public class VKPostTest {
    private APIVKFunctions functions = new APIVKFunctions();
    private static String POST;
    private static String EDIT_POST_ON;
    private static final Logger logger = Logger.getLogger(VKPostTest.class);

    @BeforeClass
    public void setUp() throws ParserConfigurationException, IOException, SAXException, XMLStreamException {
        SettingsData settingsData = Parser.getSettingData(getClass().getName(), UseParser.SAX);
        POST = (String) SettingParameter.getParameter("POST", settingsData);
        logger.info("Get parameter POST: " + POST);
        EDIT_POST_ON = (String) SettingParameter.getParameter("EDIT_POST_ON", settingsData);
        logger.info("Get parameter EDIT_POST_ON: " + EDIT_POST_ON);
    }

    @Test
    public void addPostTest() throws URISyntaxException, IOException {
        PostFacade postFacade = new PostFacade();
        String responseString = postFacade.post(POST);
        Assert.assertTrue(responseString.indexOf(POST) > -1);
    }

    @Test
    public void editPostTest() throws URISyntaxException, IOException {
        PostFacade postFacade = new PostFacade();
        String responseString = postFacade.editPost(POST, EDIT_POST_ON);
        Assert.assertTrue(responseString.indexOf(EDIT_POST_ON) > -1);
    }

    @Test
    public void deletePostTest() throws URISyntaxException, IOException {
        PostFacade postFacade = new PostFacade();
        String responseString = postFacade.deletePost(POST);
        Assert.assertTrue(responseString.indexOf(POST) == -1);
    }
}
