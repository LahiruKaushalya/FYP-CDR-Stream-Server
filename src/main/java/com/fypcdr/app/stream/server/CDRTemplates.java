package com.fypcdr.app.stream.server;

/**
 *
 * @author Lahiru Kaushalya
 */
public interface CDRTemplates {
    
    public static class CDRTemplate1{
        
        private final String cdrId;
        private final String callingNumber;
        private final String callingTower;
        private final String calledNumber;
        private final String calledTower;
        private final String duration;
        private final String dateTime;

        public CDRTemplate1() {
            this.cdrId = "";
            this.callingTower = "";
            this.callingNumber = "";
            this.calledNumber = "";
            this.calledTower = "";
            this.duration = "";
            this.dateTime = "";
        }

        public CDRTemplate1(
                String cdrId,
                String callingNumber,
                String callingTower,
                String calledNumber,
                String calledTower,
                String duration,
                String dateTime
        ) {
            this.cdrId = cdrId;
            this.callingNumber = callingNumber;
            this.callingTower = callingTower;
            this.calledNumber = calledNumber;
            this.calledTower = calledTower;
            this.duration = duration;
            this.dateTime = dateTime;
        }
        
        public String getCdrId() {
            return cdrId;
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

        public String getDuration() {
            return duration;
        }

        public String getDateTime() {
            return dateTime;
        }

    }
    
    public static class CDRTemplate2 {
        
    }
}
