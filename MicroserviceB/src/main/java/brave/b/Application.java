package brave.b;

import api.ApiBuilder;
import api.annotation.Api;

/**
 * Entry point for the microservice B.
 *
 * @author m.mazigh
 */
@Api(name = "brave-webmvc-B", version = "0.0.1-MICROSERVICE-ZIPKIN-JETTY-SNAPSHOT")
public class Application {

    public static void main(String ...args) {
        new ApiBuilder(Application.class).port("8086").build().run();
    }
}
