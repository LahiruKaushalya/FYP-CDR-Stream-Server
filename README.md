# FYP-CDR-Stream-Server

Akka Http json streaming server project to stream hashed CDR records to cloud end point.

ipAddress -> localhost/CDR server ip address

port -> 8080

GET request parameters 

provider(integer), start(integer), end(integer)

http://ipAddress:port/cdrRecords?provider={provider_value}&start={start_value}&end={end_value}



