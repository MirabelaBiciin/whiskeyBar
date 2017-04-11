package ro.mirabela.whiskeybar;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.templ.ThymeleafTemplateEngine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class WhiskeyVerticle extends AbstractVerticle {

    @Override
    public void start() {
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        ThymeleafTemplateEngine thymeleafTemplateEngine = ThymeleafTemplateEngine.create();
        TemplateHandler handler = TemplateHandler.create(thymeleafTemplateEngine, "templates", "image/*");


        router.route().handler(StaticHandler.create());
        router.route("/templates/*").handler(handler);
        router.route().handler(BodyHandler.create());
        router.post("/webroot/templates/form-upload").handler(context -> {
            try {
                Set<FileUpload> uploads = context.fileUploads();
                if (uploads.size() != 1) {
                    throw new IllegalStateException("Incarcati o imagine");
                }

                List<FileUpload> uploadedFiles = new ArrayList<>(uploads);
                FileUpload fileUploaded = uploadedFiles.get(0);
                if (fileUploaded.contentType().startsWith("image/*")) {
                    Path uploadedFile = Paths.get(fileUploaded.uploadedFileName());
                    String whiskeyName = context.request().getFormAttribute("label");
                    Path savedFile = Paths.get("/home/mira/GreatPeopleInsideProjects/whiskeyBar/labels/" + whiskeyName + ".png");
                    Files.move(uploadedFile, savedFile, StandardCopyOption.REPLACE_EXISTING);

                    log.println("lol");
                    Files.deleteIfExists(uploadedFile);
                    context.response()
                            .putHeader(HttpHeaders.LOCATION, "/templates/form-upload-label.html")
                            .setStatusCode(302)
                            .end();
                }else{
                    context.response()
                            .putHeader(HttpHeaders.LOCATION, "/error-upload-image.html")
                            .setStatusCode(302)
                            .end();
                    throw new IllegalStateException("Incarcati o imagine, nu alt tip de fisier");

                }
            } catch (IOException e) {
                System.out.println("Upload an image");
                e.getMessage();
            }
        });
        server.requestHandler(router::accept).listen(8080);
    }
}
