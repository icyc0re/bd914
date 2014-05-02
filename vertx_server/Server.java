import org.vertx.java.core.*;
import org.vertx.java.core.buffer.*;
import org.vertx.java.core.net.*;
import org.vertx.java.core.http.*;
import org.vertx.java.platform.Verticle;
import java.util.*;

public class Server extends Verticle {

    public void start() {

        vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
            public void handle(final HttpServerRequest request) {
                StringBuilder sb = new StringBuilder("Expect success!");

                request.expectMultiPart(true);
                // upload the user.json and checkins.json files
                request.uploadHandler(new Handler<HttpServerFileUpload>() {
                    public void handle(HttpServerFileUpload upload) {
                        upload.streamToFileSystem("/home/azureuser/uploads/" + upload.filename());
                    }
                });

                // handle the other parts of the request
                request.endHandler(new VoidHandler() {
                    public void handle() {
                        // The request has been all ready so now we can look at the form attributes
                        MultiMap attrs = request.formAttributes();

                        // read all of the values
                        String userId = attrs.get("userId");
                        String time = attrs.get("time");
                        String lat = attrs.get("lat");
                        String lng = attrs.get("lng");

                        // submit new job
                        try {
                            Runtime.getRuntime().exec("java -cp /opt/spark/spark-0.9.1-bin-hadoop2/assembly/target/scala-2.10/spark-assembly_2.10-0.9.1-hadoop2.2.0.jar:/home/azureuser/bd914/cluster/target/scala-2.10/simple-project_2.10-1.0.jar:/home/azureuser/.ivy2/cache/org.scalaj/scalaj-http_2.10/jars/scalaj-http_2.10-0.3.14.jar MainApp");
                         	System.out.println("Started job...");
                        } catch (Exception e) {}

                        System.out.println("Req with userId time lat lng = " + userId + " " + time + " " + lat + " " + lng);
                    }
                });

                // visible in browser
                request.response().end(sb.toString());
            }
        }).listen(8090, "100.88.178.21");
    }
}