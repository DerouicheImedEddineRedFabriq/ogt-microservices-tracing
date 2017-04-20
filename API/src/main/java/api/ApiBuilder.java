package api;

import api.annotation.Api;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 *
 *
 * @author m.mazigh
 */
public class ApiBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiBuilder.class);
    private Api application;
    private Server server;
    private WebAppContext context;
    private int port = 0;
    private String contextPath = "";
    List<Class<?>> configurationClasses = new ArrayList<>();

    public ApiBuilder(Class<?> applicationClass, Class<?>... configurations) {
        this.application = applicationClass.getDeclaredAnnotation(Api.class);
        Objects.requireNonNull(this.application, "The application cannot be built. It must be annotated with @Api");
        this.configurationClasses.add(applicationClass);
        if (configurations != null) {
            this.configurationClasses.addAll(Arrays.asList(configurations));
        }
    }

    public ApiBuilder port(int port) {
        ApiConfiguration.instance().setPort(port);
        this.port = port;
        return this;
    }

    public ApiBuilder contextPath(String ctx)
    {
        this.contextPath = ctx;
        return this;
    }

    public ApiBuilder build() {
        Objects.requireNonNull(this.contextPath, "The contextPath is null.");
        if (this.port == 0) {
            throw new IllegalArgumentException("The port is 0.");
        }
        this.server = new Server();
        final SocketConnector connector = new SocketConnector();

        connector.setMaxIdleTime(1000 * 60 * 60);
        connector.setPort(this.port);
        this.server.setConnectors(new Connector[]{connector});

        this.context = new WebAppContext();
        this.context.setServer(this.server);
        this.context.setContextPath(this.contextPath);
        this.context.setWar("src/main/webapp");

        this.server.setHandler(this.context);

        try {
            this.server.start();
        } catch (final Exception e) {
            throw new IllegalStateException("Failed to start server.", e);
        }
        return this;
    }

    public void run(String[] args) {
//        to test with spring boot
//        try {
//            Objects.requireNonNull(this.springBootBuilder, "Impossible to run application. Make sure the application has been built (call to build())");
//            this.springBootBuilder.run(args);
//        } catch (Exception var3) {
//            LOGGER.error("Impossible to start application {} with args {}", new Object[]{this.application.name(), Arrays.asList(args), var3});
//        }
        LOGGER.info("Server started....");

    }
}
