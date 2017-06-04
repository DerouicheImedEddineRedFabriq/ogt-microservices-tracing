package brave.b.webmvc;

import entities.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Random;

@RestController
@EnableWebMvc
@Configuration
@PropertySource(value = "classpath:conf.properties")

public class ControllerB {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerB.class);


    @Value("${server.a.url}")
    private String url;

    @Bean
    RestTemplate template() {
        return new RestTemplate();
    }

    @Autowired
    RestTemplate template;

    @RequestMapping(path = "/b", method = RequestMethod.GET)
    public String b() throws InterruptedException {

        Random random = new Random();
        Thread.sleep(random.nextInt(1000));

        LOGGER.info("Get b...");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Person> request = new HttpEntity<>(new Person("Mazigh", "Belhassen"), headers);
        Person p = template.postForObject(url + "/upper", request, Person.class);
        LOGGER.info(p.toString());
        return p.toString();
    }
}
