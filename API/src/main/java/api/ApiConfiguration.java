package api;

import com.github.kristofa.brave.Brave;

/**
 *
 *
 * @author m.mazigh
 */
public class ApiConfiguration {
    private static final ApiConfiguration instance = new ApiConfiguration();

    private Brave brave;
    private int port;

    public static ApiConfiguration instance() {
        return instance;
    }

    public void setBrave(Brave brave) {
        this.brave = brave;
    }

    public Brave getBrave() {
        return this.brave;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
