package ro.mirabela.whiskeybar;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

public class Main {
    public static void main(String[] args) {

        WhiskeyVerticle whiskeyVerticle=new WhiskeyVerticle();
        whiskeyVerticle.start();
    }



}
