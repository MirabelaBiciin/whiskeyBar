package ro.mirabela.whiskeybar;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.ThymeleafTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WhiskeyVerticle extends AbstractVerticle {

    @Override
    public void start() {
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        ThymeleafTemplateEngine thymeleafTemplateEngine = ThymeleafTemplateEngine.create();
        TemplateHandler handler = TemplateHandler.create(thymeleafTemplateEngine, "templates", "text/html");

        router.route().handler(StaticHandler.create());

        router.route("/templates/*").handler(handler);
        router.post("/templates/form-upload-logo").handler(context->{
            Set<FileUpload> uploads=context.fileUploads();
            if(uploads.size()!=1) {
                throw new IllegalStateException("lkj");
            }
            List<FileUpload> upload=new ArrayList<FileUpload>(uploads);
            File fileUploaded=upload[0];
        });

        server.requestHandler(router::accept).listen(8080);
    }


}
