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

    private final String file_name;
    private final SAXParser parser;
    private final DataInfo data_info;

    private double start;
    private int counter = 0;
    private int mill_counter = 1;

    public ConvertJob(DataInfo data_info, String file_name, SAXParser parser) throws IOException {
        this.data_info = data_info;
        this.file_name = file_name;
        this.parser = parser;

        File csv = new File("output/" + data_info.getDataName() + ".csv");
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
            for(int i=0; i<data_info.getData_attributes().size(); i++){
                writer.write((data_info.getData_attributes().get(i).getKey()));
                if(i!=data_info.getData_attributes().size()-1)writer.write(",");
            }
            writer.write("\n");
            XMLHandler xml_handler = new XMLHandler(this::processRow);
            parser.parse(new File(file_name), xml_handler);

        } catch (SAXException | IOException e) {
            System.out.println("Running parser failed...");
        }

    }

    void processRow(Attributes attributes) {
        writeToCSV(attributes);
        measureTime();
    }

    public void writeToCSV(Attributes attributes){
        ArrayList<Pair<String, AttributeType>> table_attributes = data_info.getData_attributes();
        for (int i = 0; i < table_attributes.size(); i++) {
            Pair<String, AttributeType> current_attribute = table_attributes.get(i);
            try {
                String to_write = "";
                String attribute_value = attributes.getValue(current_attribute.getKey());
                switch (current_attribute.getValue()) {
                    case INTEGER:
                        to_write = attribute_value == null ? "0" : attribute_value;
                        break;
                    case STRING:
                        to_write = attribute_value == null ? "" : attribute_value;
                        break;
                }

                if(to_write.contains("\r")) to_write = to_write.replace("\r", " ");
                if(to_write.contains("\n")) to_write = to_write.replace("\n", " ");
                if (to_write.contains(",") || to_write.contains("\"") || to_write.contains("'")) {
                    to_write = to_write.replace("\"", "\"\"");
                    to_write = "\"" + to_write + "\"";
                }

                writer.write(to_write);
                if(i!=table_attributes.size()-1){
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
        System.out.println("Needed " + String.format("%.2f", stop) + "sec to insert " + (mill_counter++) + "mill entries into " + data_info.getDataName() + ".");
        counter = 0;
    }
}
