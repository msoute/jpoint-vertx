package nl.jpoint.vertx;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

public class HttpClusterVerticle extends Verticle {

    public static final String JPOINT_EVENTBUS_ADDRESS = "jpoint.eventbus.example";
    private static final String USER_NAME_PROPERTY = "user.name";

    public void start() {


        vertx.eventBus().registerHandler(JPOINT_EVENTBUS_ADDRESS, (Handler <Message<JsonObject>>) message -> {
            container.logger().info("Received request from : " + message.body().getString("from"));
            message.reply(System.getProperty(USER_NAME_PROPERTY));
            container.logger().info("Responded with " + System.getProperty(USER_NAME_PROPERTY) + " to " + message.body().getString("from"));
        });

        HttpServer http = getVertx().createHttpServer();
        final JsonObject message = new JsonObject();
        message.putString("from", System.getProperty(USER_NAME_PROPERTY));

        http.requestHandler(event -> getVertx().eventBus().send(JPOINT_EVENTBUS_ADDRESS,
                message, (Message<String> response) -> {
                    container.logger().info("Received response from " + response.body());
                    event.response().end(response.body());
                }));
        http.listen(8080);

        container.logger().info("HTTP verticle started");

    }
}
