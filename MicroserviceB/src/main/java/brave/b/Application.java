package brave.b;

import api.ApiBuilder;
import api.annotation.Api;

/**
 * Created by m.mazigh on 10/03/2017.
 */
@Api(name = "brave-webmvc-B", version = "0.0.1-MICROSERVICE-ZIPKIN-JETTY-SNAPSHOT")
//@Configuration
//@PropertySource(value = "classpath:conf.properties")
public class Application {

    public static void main(String ...args) {
        new ApiBuilder(Application.class).port(8084).build().run(args);
    }
}
