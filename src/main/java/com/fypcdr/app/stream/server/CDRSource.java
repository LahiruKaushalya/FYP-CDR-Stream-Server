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
        Source<JSONObject, NotUsed> cdrSource = Source.from(SparkHash.getCDR().subList(start,end));
        return cdrSource;
    }


}
