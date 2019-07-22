package parser;

import model.SettingsData;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import parser.dom.DomParser;
import parser.sax.SettingsHandler;
import parser.stax.StaxParser;

import javax.xml.parsers.*;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Parser {
    private static String XML_FILE = "testparams.xml";

    private static List<SettingsData> getSettingsByDom() throws ParserConfigurationException, IOException, SAXException, XMLStreamException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(XML_FILE);
        return new DomParser().parse(document);
    }

    private static List<SettingsData> getSettingsBySax() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        SettingsHandler settingsHandler = new SettingsHandler();
        saxParser.parse(new File(XML_FILE), settingsHandler);
        return settingsHandler.getSettingsData();
    }

    private static List<SettingsData> getSettingsByStax() throws FileNotFoundException, XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(XML_FILE));
        return new StaxParser().parse(xmlEventReader);
    }

    public static SettingsData getSettingData(String testName, UseParser parser) throws ParserConfigurationException, XMLStreamException, SAXException, IOException {
        List<SettingsData> list;
        switch (parser) {
            case DOM:
                list = getSettingsByDom();
                break;
            case SAX:
                list = getSettingsBySax();
                break;
            case STAX:
                list = getSettingsByStax();
                break;
            default:
                list = getSettingsByDom();
                break;
        }
        for (SettingsData settingsData : list) {
            if (settingsData.getName().equalsIgnoreCase(testName)) {
                return settingsData;
            }
        }
        return null;
    }
}
