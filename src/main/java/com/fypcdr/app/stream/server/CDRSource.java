package com.fypcdr.app.stream.server;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lahiru Kaushalya
 */
public class CDRSource {
    
    private List<CDRTemplates.CDRRecord> cdrRecords;
    
    public CDRSource()
    {
        this.cdrRecords = new ArrayList<>();
    }
    
    public Source<CDRTemplates.CDRRecord, NotUsed> getCDRSource(int n)
    {
        Source<CDRTemplates.CDRRecord, NotUsed> cdrSource 
            = Source.from(createCDRs(n));
        return cdrSource;
    }
    
    private static List<CDRTemplates.CDRRecord> createCDRs(int n)
    {
        List<CDRTemplates.CDRRecord> cdrRecords = new ArrayList<>();
        
        cdrRecords.add( //test data
                new CDRTemplates.CDRTemplate1(
                        "AZ11",
                        "0771548751",
                        "KC45",
                        "0714215741",
                        "XC47",
                        "0056",
                        "20180512"
                )); 
        
        cdrRecords.add(
                new CDRTemplates.CDRTemplate1(
                        "AZ12",
                        "0771548751",
                        "MH45",
                        "0714215741",
                        "XC47",
                        "1256",
                        "20180512"
                )); 
        
        return cdrRecords;
    }
}
