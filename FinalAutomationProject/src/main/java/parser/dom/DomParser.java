package parser.dom;

import model.SettingsData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomParser {
    public List<SettingsData> parse(Document document) throws FileNotFoundException, XMLStreamException {
        NodeList nodeList = document.getElementsByTagName("test");

        List<SettingsData> settingsDataList = new ArrayList<>();
        for (int j = 0; j < nodeList.getLength(); j++) {
            SettingsData settingsData = new SettingsData();
            settingsData.setName(((Element) nodeList.item(j)).getAttribute("name"));
            NodeList dataItem = nodeList.item(j).getChildNodes();
            Map<String, Object> dataParse = new HashMap<>();
            for (int i = 0; i < dataItem.getLength(); i++) {
                if (dataItem.item(i).getNodeName().equals("param")) {
                    dataParse.put(((Element) dataItem.item(i)).getAttribute("name"), ((Element) dataItem.item(i)).getAttribute("value"));
                }
            }
            settingsData.setData(dataParse);
            settingsDataList.add(settingsData);
        }
        return settingsDataList;
    }
}
