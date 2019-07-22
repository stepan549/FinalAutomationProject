package parser.sax;

import model.SettingsData;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsHandler extends DefaultHandler {
    private List<SettingsData> settingsDataList;
    private Map<String, Object> paramData;
    private SettingsData settingsData;
    boolean bParam = false;

    public List<SettingsData> getSettingsData() {
        return settingsDataList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("test")) {
            String name = attributes.getValue("name");
            settingsData = new SettingsData();
            settingsData.setName(name);
            paramData = new HashMap<>();
            if (settingsDataList == null) {
                settingsDataList = new ArrayList<>();
            }
        } else if (qName.equalsIgnoreCase("param")) {
            String paramName = attributes.getValue("name");
            String paramValue = attributes.getValue("value");
            paramData.put(paramName, paramValue);
            settingsData.setData(paramData);
            bParam = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("test")) {
            settingsDataList.add(settingsData);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

        if (bParam) {
            bParam = false;
        }
    }
}
