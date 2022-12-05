package com.wriesnig.sodumpsconvert;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.function.Consumer;

public class XMLHandler extends DefaultHandler {
    private final Consumer<Attributes> process_row;


    public XMLHandler(Consumer<Attributes> process_row) {
        this.process_row = process_row;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (!qName.equals("row")) return;
        process_row.accept(attributes);
    }
}
