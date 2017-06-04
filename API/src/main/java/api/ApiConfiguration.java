package api;

import com.github.kristofa.brave.Brave;

/**
 * The server configurations
 *
 * @author m.mazigh
 */
public class ApiConfiguration {
    private static final ApiConfiguration instance = new ApiConfiguration();

    private Brave brave;
    private String port;

    public static ApiConfiguration instance() {
        return instance;
    }

    /**
     * The brave instance to set.
     *
     * @param brave The brave to set
     */
    public void setBrave(Brave brave) {
        this.brave = brave;
    }

    /**
     * Return The brave instance.
     *
     * @return The brave
     */
    public Brave getBrave() {
        return this.brave;
    }

    /**
     * Return the used port.
     *
     * @return the used port
     */
    public String getPort() {
        return port;
    }

    /**
     * Sets the port.
     *
     * @param port The port to set.
     */
    public void setPort(String port) {
        this.port = port;
    }
}
