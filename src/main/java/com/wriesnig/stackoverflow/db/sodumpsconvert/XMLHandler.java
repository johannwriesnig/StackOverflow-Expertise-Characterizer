package com.wriesnig.stackoverflow.db.sodumpsconvert;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import java.util.function.Consumer;

public class XMLHandler extends DefaultHandler {
    private final Consumer<Attributes> processRow;


    public XMLHandler(Consumer<Attributes> processRow) {
        this.processRow = processRow;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (!qName.equals("row")) return;
        processRow.accept(attributes);
    }
}
