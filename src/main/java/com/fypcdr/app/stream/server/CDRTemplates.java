package com.fypcdr.app.stream.server;

/**
 *
 * @author Lahiru Kaushalya
 */
public interface CDRTemplates {
    
    abstract class CDRRecord // Base CDR template
    {
        private final String cdrId;
        private final String duration;
        private final String dateTime;
        
        public CDRRecord(
                String cdrId,
                String duration,
                String dateTime
        ) {
            this.cdrId = cdrId;
            this.duration = duration;
            this.dateTime = dateTime;
        }
        
        public String getCdrId() {
            return cdrId;
        }
        
        public String getDuration() {
            return duration;
        }

        public String getDateTime() {
            return dateTime;
        }
        
    }
    
    class CDRTemplate1 extends CDRRecord{
        
        private final String callingNumber;
        private final String callingTower;
        private final String calledNumber;
        private final String calledTower;

        public CDRTemplate1(
            String cdrId,
            String callingNumber,
            String callingTower,
            String calledNumber,
            String calledTower,
            String duration,
            String dateTime
        ) {
            super(cdrId, duration, dateTime);
            this.callingNumber = callingNumber;
            this.callingTower = callingTower;
            this.calledNumber = calledNumber;
            this.calledTower = calledTower;
        }
        
        public String getCallingNumber() {
            return callingNumber;
        }

        public String getCallingTower() {
            return callingTower;
        }
        
        public String getCalledNumber() {
            return calledNumber;
        }
        
        public String getCalledTower() {
            return calledTower;
        }
    } // Template 1
    
     class CDRTemplate2 extends CDRRecord{
        
        private final String field1;

        private final String field2;
        
        public CDRTemplate2(
            String cdrId,
            String duration,
            String dateTime,
            String field1,
            String field2
        ) {
            super(cdrId, duration, dateTime);
            this.field1 = field1;
            this.field2 = field2;
        }
        
        public String getField1() {
            return field1;
        }

        public String getField2() {
            return field2;
        }
    }
}
