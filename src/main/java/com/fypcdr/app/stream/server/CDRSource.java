package com.fypcdr.app.stream.server;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import org.json.simple.JSONObject;

/**
 *
 * @author Lahiru Kaushalya
 */
public class CDRSource {
    

    public Source<JSONObject, NotUsed> getCDRSource(int start, int end)
    {
        Source<JSONObject, NotUsed> cdrSource = Source.from(SparkHash.getCDR(start,end));
        return cdrSource;
    }
    
    //Test data source
    /*
    private ArrayList<JSONObject> getCDR(int start, int end){
        ArrayList<JSONObject> cdrs = new ArrayList<>();
        
        int i = 0;
        while (i < end - start) {
            JSONObject cdr_json1 = new JSONObject();
            cdr_json1.put("called_num", "+94**645128");
            cdr_json1.put("called_tower", "MH457");
            cdr_json1.put("recipient_num", "+94**2321456");
            cdr_json1.put("recipient_tower", "MV412");
            cdr_json1.put("datetime", "20180512");
            cdr_json1.put("duration", "2457");
            cdrs.add(cdr_json1);
            i++;
        }

        return cdrs;
    }
    
    */
}
