package com.fypcdr.app.stream.server;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.http.javadsl.common.EntityStreamingSupport;
import akka.http.javadsl.common.JsonEntityStreamingSupport;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.unmarshalling.StringUnmarshallers;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import akka.util.Timeout;
import com.fypcdr.app.stream.server.CDRTemplates.CDRTemplate1;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Lahiru Kaushalya
 */
public class Routes extends AllDirectives {

    final private LoggingAdapter log;

    final ByteString start = ByteString.fromString("[");
    final ByteString between = ByteString.fromString(",");
    final ByteString end = ByteString.fromString("]");
    
    final Flow<ByteString, ByteString, NotUsed> compactArrayRendering =
      Flow.of(ByteString.class).intersperse(start, between, end);

    final JsonEntityStreamingSupport compactJsonSupport = EntityStreamingSupport.json()
      .withFramingRendererFlow(compactArrayRendering);
    
    public Routes(ActorSystem system) {
        log = Logging.getLogger(system, this);
    }

    Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    public Route routes() {
        return route(
            pathPrefix("cdrRecords", 
                () -> route(
                    getCDRRecords()
                )
            )
        );
    }

    private Route getCDRRecords() {
        return pathEnd(()
            -> route(
                get(() -> 
                    parameter(StringUnmarshallers.INTEGER, "n", n -> {
                    final Source<CDRTemplate1, NotUsed> cdrSource // test soource
                    = Source.repeat(
                            new CDRTemplate1(
                                    "AZ12",
                                    "0771548751",
                                    "MH45",
                                    "0714215741",
                                    "XC47",
                                    "1256",
                                    "20180512"
                            )).take(n);

                    return completeOKWithSource(cdrSource, Jackson.marshaller(), compactJsonSupport);
                })
                )
            )
        );
    }

}
