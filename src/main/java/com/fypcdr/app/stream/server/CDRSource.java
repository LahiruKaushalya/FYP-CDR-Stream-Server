package com.fypcdr.app.stream.server;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import org.json.simple.JSONObject;

/**
 *
 * @author Lahiru Kaushalya
 */
public class CDRSource {
    

    public Source<JSONObject, NotUsed> getCDRSource(int n)
    {
        Source<JSONObject, NotUsed> cdrSource = Source.from(SparkHash.getCDR());
        return cdrSource;
    }


}
