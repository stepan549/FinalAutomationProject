package parser.stax;

import model.SettingsData;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaxParser {
    private static final String DESCRIPTION = "Description";
    private static final String VALUE = "value";
    private static final String PARAM = "param";
    private static final String NAME = "name";
    private static final String TEST = "test";
    private Map<String, Object> paramData;
    private SettingsData settingsData;
    List<SettingsData> settingsDataList = new ArrayList<>();

    public List<SettingsData> parse(XMLEventReader xmlEventReader) throws FileNotFoundException, XMLStreamException {
        while (xmlEventReader.hasNext()) {
            XMLEvent xmlEvent = xmlEventReader.nextEvent();
            proceedStartElement(xmlEvent, xmlEventReader);
            proceedEndElement(xmlEvent);
        }
        return settingsDataList;
    }

    private void proceedStartElement(XMLEvent xmlEvent, XMLEventReader xmlEventReader) throws XMLStreamException {
        if (xmlEvent.isStartElement()) {
            StartElement startElement = xmlEvent.asStartElement();
            if (isTagNameEqual(startElement, TEST)) {
                settingsData = new SettingsData();
                Attribute attribute = startElement.getAttributeByName(new QName(NAME));
                if (attribute != null) {
                    settingsData.setName(attribute.getValue());
                    paramData = new HashMap<>();
                }
            } else if (isTagNameEqual(startElement, PARAM)) {
                String name = startElement.getAttributeByName(new QName(NAME)).getValue();
                String value = startElement.getAttributeByName(new QName(VALUE)).getValue();
                paramData.put(name, value);
            }
        }
    }

    private void proceedEndElement(XMLEvent xmlEvent) {
        if (xmlEvent.isEndElement()) {
            EndElement endElement = xmlEvent.asEndElement();
            if (endElement.getName().getLocalPart().equals(TEST)) {
                settingsData.setData(paramData);
                settingsDataList.add(settingsData);
            }
        }
    }

    private boolean isTagNameEqual(StartElement startElement, String tagName) {
        return startElement.getName().getLocalPart().equals(tagName);
    }
}
