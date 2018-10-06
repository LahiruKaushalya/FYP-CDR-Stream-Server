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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lahiru Kaushalya
 */
public class Server extends AllDirectives {

    private final Routes routes;
    private static String ipAddress;
    private static int port;
    private static Properties prop;

    public static Properties getProp() {
        return prop;
    }

    public Server(ActorSystem system){
        this.routes = new Routes(system);
        Server.prop = new Properties();
    }
    
    public static void main(String[] args) {
        
        ActorSystem system = ActorSystem.create("CDRHttpServer");
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        Server app = new Server(system);
        
        InputStream input;
        try {
            input = new FileInputStream("conf.properties");
            // load properties file
            prop.load(input);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        ipAddress = prop.getProperty("ipAddress");
        port = Integer.parseInt(prop.getProperty("port"));
        
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
                ConnectHttp.toHost(ipAddress, port), materializer);

        System.out.println(
                "Server online at http://"
                + ipAddress
                + ":"
                + port
                + "/\nPress RETURN to stop..."
        );
        
        try {
            System.in.read();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        binding
            .thenCompose(ServerBinding::unbind) 
            .thenAccept(unbound -> system.terminate()); 
    }

    protected Route createRoute() {
        return routes.routes();
    }
}


