package com.wriesnig.sodumpsconvert;


import com.wriesnig.sodumpsconvert.datainfo.AttributeType;
import com.wriesnig.sodumpsconvert.datainfo.DataInfo;
import javafx.util.Pair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ConvertJob{
    private final FileWriter writer;

    private final String fileName;
    private final SAXParser parser;
    private final DataInfo dataInfo;

    private double start;
    private int counter = 0;
    private int mill_counter = 1;

    public ConvertJob(DataInfo dataInfo, String fileName, SAXParser parser) throws IOException {
        this.dataInfo = dataInfo;
        this.fileName = fileName;
        this.parser = parser;

        File csv = new File("output/" + dataInfo.getDataName() + ".csv");
        writer = new FileWriter(csv);
    }

    public void convert() {
        start = System.nanoTime();
        startParsing();
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void startParsing() {
        try {
            for(int i = 0; i< dataInfo.getDataAttributes().size(); i++){
                writer.write((dataInfo.getDataAttributes().get(i).getKey()));
                if(i!= dataInfo.getDataAttributes().size()-1)writer.write(",");
            }
            writer.write("\n");
            XMLHandler xml_handler = new XMLHandler(this::processRow);
            parser.parse(new File(fileName), xml_handler);

        } catch (SAXException | IOException e) {
            System.out.println("Running parser failed...");
        }

    }

    void processRow(Attributes attributes) {
        writeToCSV(attributes);
        measureTime();
    }

    public void writeToCSV(Attributes attributes){
        ArrayList<Pair<String, AttributeType>> dataAttributes = dataInfo.getDataAttributes();
        for (int i = 0; i < dataAttributes.size(); i++) {
            Pair<String, AttributeType> current_attribute = dataAttributes.get(i);
            try {
                String toWrite = "";
                String attributeValue = attributes.getValue(current_attribute.getKey());
                switch (current_attribute.getValue()) {
                    case INTEGER:
                        toWrite = attributeValue == null ? "0" : attributeValue;
                        break;
                    case STRING:
                        toWrite = attributeValue == null ? "" : attributeValue;
                        break;
                }

                if(toWrite.contains("\r")) toWrite = toWrite.replace("\r", " ");
                if(toWrite.contains("\n")) toWrite = toWrite.replace("\n", " ");
                if (toWrite.contains(",") || toWrite.contains("\"") || toWrite.contains("'")) {
                    toWrite = toWrite.replace("\"", "\"\"");
                    toWrite = "\"" + toWrite + "\"";
                }

                writer.write(toWrite);
                if(i!=dataAttributes.size()-1){
                    writer.write(",");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            writer.write("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    void measureTime() {
        if (counter++ != 1000000) return;

        double stop = System.nanoTime() - start;
        stop /= 1000000000.0;
        System.out.println("Needed " + String.format("%.2f", stop) + "sec to insert " + (mill_counter++) + "mill entries into " + dataInfo.getDataName() + ".");
        counter = 0;
    }
}
