package api;

import api.annotation.Api;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The boot server.
 *
 * @author m.mazigh
 */
public class ApiBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiBuilder.class);
    private Api application;
    private Server server;
    private WebAppContext context;
    private String port = "8080";
    private String contextPath = "";
    List<Class<?>> configurationClasses = new ArrayList<>();

    /**
     * The constructor.
     *
     * @param applicationClass The entries points
     * @param configurations Configurations classes
     */
    public ApiBuilder(Class<?> applicationClass, Class<?>... configurations) {
        this.application = applicationClass.getDeclaredAnnotation(Api.class);
        Objects.requireNonNull(this.application, "The application cannot be built. It must be annotated with @Api");
        this.configurationClasses.add(applicationClass);
        if (configurations != null) {
            this.configurationClasses.addAll(Arrays.asList(configurations));
        }
    }

    /**
     *
     * @param port The port to set
     * @return The ApiBuilder
     */
    public ApiBuilder port(String port) {
        ApiConfiguration.instance().setPort(port);
        this.port = port;
        return this;
    }

    /**
     *
     * @param ctx The contextPath to set
     * @return The ApiBuilder
     */
    public ApiBuilder contextPath(String ctx)
    {
        this.contextPath = ctx;
        return this;
    }

    /**
     *
     * @return The ApiBuilder
     */
    public ApiBuilder build() {
        Objects.requireNonNull(this.contextPath, "The contextPath is null.");
        if (StringUtils.isEmpty(port)) {
            throw new IllegalArgumentException("The port is empty.");
        }
        this.server = new Server();
        final SocketConnector connector = new SocketConnector();

        connector.setMaxIdleTime(1000 * 60 * 60);
        try {
            connector.setPort(Integer.parseInt(this.port));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The port is not an int.");
        }
        this.server.setConnectors(new Connector[]{connector});

        this.context = new WebAppContext();
        this.context.setServer(this.server);
        this.context.setContextPath(this.contextPath);
        this.context.setWar("src/main/webapp");

        this.server.setHandler(this.context);


        return this;
    }

    /**
     * Run the server.
     */
    public void run() {
        try {
            this.server.start();
            LOGGER.info("Server started....");
        } catch (final Exception e) {
            throw new IllegalStateException("Failed to start server.", e);
        }
    }
}
