package brave.b.webmvc;

import api.ApiConfiguration;
import tracing.MyServletHandlerInterceptor;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.http.DefaultSpanNameProvider;
import com.github.kristofa.brave.http.SpanNameProvider;
import com.github.kristofa.brave.spring.BraveClientHttpRequestInterceptor;
import com.github.kristofa.brave.spring.ServletHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import zipkin.Span;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.Reporter;
import zipkin.reporter.Sender;
import zipkin.reporter.okhttp3.OkHttpSender;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * This adds tracing configuration to any web mvc controllers or rest template clients. This should
 * be configured last.
 */
@Configuration
@Import({BraveClientHttpRequestInterceptor.class, ServletHandlerInterceptor.class})
@PropertySource(value = "classpath:conf.properties")
public class WebTracingConfiguration extends WebMvcConfigurerAdapter {

    @Value("${zipkin.address}")
    private String address;
    @Value("${application.name}")
    private String application;

    /**
     * Configuration for how to send spans to Zipkin
     */
    @Bean
    Sender sender() {
        return OkHttpSender.create(address);
    }

    /**
     * Configuration for how to buffer spans into messages for Zipkin
     */
    @Bean
    Reporter<Span> reporter() {
        return AsyncReporter.builder(sender()).build();
    }

    @Bean
    Brave brave() {
        Brave brave = new Brave.Builder(application).reporter(reporter()).build();
        ApiConfiguration.instance().setBrave(brave);
        return brave;
    }

    // decide how to name spans. By default they are named the same as the http method.
    @Bean
    SpanNameProvider spanNameProvider() {
        return new DefaultSpanNameProvider();
    }

    @Autowired
    private ServletHandlerInterceptor serverInterceptor;

    @Autowired
    private BraveClientHttpRequestInterceptor clientInterceptor;

    @Autowired
    private RestTemplate restTemplate;

    // adds tracing to the application-defined rest template
    @PostConstruct
    public void init() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>(restTemplate.getInterceptors());
        interceptors.add(clientInterceptor);
        restTemplate.setInterceptors(interceptors);
    }

    // adds tracing to the application-defined web controllers
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(serverInterceptor);
        registry.addInterceptor(new MyServletHandlerInterceptor());
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
