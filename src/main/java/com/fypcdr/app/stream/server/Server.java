package com.fypcdr.app.stream.server;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.util.concurrent.CompletionStage;
/**
 *
 * @author Lahiru Kaushalya
 */
public class Server extends AllDirectives {

    private final Routes routes;
    private static String ipAddress;
    private static int port;

    public Server(ActorSystem system) {
        this.routes = new Routes(system);
        Server.ipAddress = "localhost";
        Server.port = 8080;
    }
    
    public static void main(String[] args) throws Exception {
        
        ActorSystem system = ActorSystem.create("CDRHttpServer");

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        Server app = new Server(system);

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
                ConnectHttp.toHost(ipAddress, port), materializer);

        System.out.println("Server online at http://"+ipAddress+":"+port+"/\nPress RETURN to stop...");
        System.in.read(); 

        binding
            .thenCompose(ServerBinding::unbind) 
            .thenAccept(unbound -> system.terminate()); 
    }

    protected Route createRoute() {
        return routes.routes();
    }
}


