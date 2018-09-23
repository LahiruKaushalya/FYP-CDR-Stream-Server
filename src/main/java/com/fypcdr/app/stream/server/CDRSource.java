package com.fypcdr.app.stream.server;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import org.json.simple.JSONObject;

/**
 *
 * @author Lahiru Kaushalya
 */
public class CDRSource {
    

    public Source<JSONObject, NotUsed> getCDRSource(int provider, int start, int end)
    {
        Source<JSONObject, NotUsed> cdrSource = Source.from(SparkHash.getCDR().subList(0,start));
        return cdrSource;
    }


}
