package com.fypcdr.app.stream.server;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author Lahiru Kaushalya
 */
public class CDRSource {
    
    public Source<JSONObject, NotUsed> getCDRSource(int n)
    {
        Source<JSONObject, NotUsed> cdrSource = Source.from(createCDRs());
        return cdrSource;
    }
    
    private static List<JSONObject> createCDRs()
    {
        List<JSONObject> cdrRecords = new ArrayList<>();
        JSONObject cdr_1 = new JSONObject();
        JSONObject cdr_2 = new JSONObject();

        cdr_1.put("called_num","0771234567");
        cdr_1.put("called_tower","DA2143");
        cdr_1.put("recipient_num","0712435678");
        cdr_1.put("recipient_tower","MT6745");
        cdr_1.put("datetime","2018-09-21T09:01:56.528+05:30");
        cdr_1.put("duration","23");

        cdr_2.put("called_num","0754634129");
        cdr_2.put("called_tower","BA2132");
        cdr_2.put("recipient_num","0778989900");
        cdr_2.put("recipient_tower","DA7823");
        cdr_2.put("datetime","2018-08-23T22:05:23.713+05:30");
        cdr_2.put("duration","15");

        cdrRecords.add(cdr_1);   //test data
        cdrRecords.add(cdr_2);   //test data

        return cdrRecords;
    }
}
