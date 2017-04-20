package brave.a.webmvc;

import api.ApiConfiguration;
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
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import tracing.MyServletHandlerInterceptor;
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
// import as the interceptors are annotation with javax.inject and not automatically wired
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
//        if (endpointUrl != null && !endpointUrl.isEmpty()) {
//            Sender sender = OkHttpSender.builder().endpoint(endpointUrl).build();
//            reporter = AsyncReporter.builder(sender).messageTimeout(5L, TimeUnit.SECONDS).queuedMaxSpans(100).build();
//            LOGGER.info("Reporting to Zipkin server correctly configured for : {}", endpointUrl);
//        } else {
//            LOGGER.warn("No Zipkin server url has been provided in property : architecture.servers.zipkin.url");
//            LOGGER.info("Logging Zipkin trace info to the logs");
//            reporter = new LoggingReporter();
//        }
        //        return new LoggingReporter();
        // uncomment to actually send to zipkin!
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

    @Bean
    public RequestMappingHandlerAdapter annotationMethodHandlerAdapter() {
        final RequestMappingHandlerAdapter annotationMethodHandlerAdapter = new RequestMappingHandlerAdapter();
        final MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter =
                new MappingJackson2HttpMessageConverter();

        List<HttpMessageConverter<?>> httpMessageConverter = new ArrayList<HttpMessageConverter<?>>();
        httpMessageConverter.add(mappingJacksonHttpMessageConverter);

        String[] supportedHttpMethods = {"POST", "GET", "HEAD"};

        annotationMethodHandlerAdapter.setMessageConverters(httpMessageConverter);
        annotationMethodHandlerAdapter.setSupportedMethods(supportedHttpMethods);

        return annotationMethodHandlerAdapter;
    }
}
