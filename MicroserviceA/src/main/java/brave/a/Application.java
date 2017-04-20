package brave.a;

import api.ApiBuilder;
import api.annotation.Api;

/**
 *
 *
 * @author m.mazigh
 */

@Api(name = "brave-webmvc-A", version = "1.0-MICROSERVICE-ZIPKIN-JETTY-SNAPSHOT")
public class Application {

    public static void main(String ...args) {
        new ApiBuilder(Application.class).port(8085).contextPath("").build().run(args);
    }
}
