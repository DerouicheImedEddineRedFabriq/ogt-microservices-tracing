package brave.a.webmvc;

import entities.Person;
import exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Random;


@RestController
@EnableWebMvc
@Configuration
@PropertySource(value = "classpath:conf.properties")
public class ControllerA {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerA.class);


    @Value("${server.b.url}")
    private String url;

    @Bean
    RestTemplate template() {
        return new RestTemplate();
    }

    @Autowired
    RestTemplate template;

    @RequestMapping(path = "/a", method = RequestMethod.GET)
    public String a() throws InterruptedException {
        LOGGER.info("Get a...");
        Random random = new Random();
        Thread.sleep(random.nextInt(1000));

        return template.getForObject(url + "/b", String.class);
    }

    @RequestMapping(path = "/c", method = RequestMethod.GET)
    public String c() throws InterruptedException, MyException {
        LOGGER.info("Get c...");
        throw new MyException("Test exception tracing");
    }

    @RequestMapping(path = "/upper",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Person printUpper(@RequestBody Person person) throws MyException {
        LOGGER.info("POST upper...");
        if (person == null) {
            throw new MyException("person is null");
        } else if (StringUtils.isEmpty(person.getLastName())) {
            throw new MyException("LastName is null or empty");
        } else if (StringUtils.isEmpty(person.getFirstName())) {
            throw new MyException("FirstName is null or empty");
        } else {
            return new Person(person.getLastName().toUpperCase(), person.getFirstName().toUpperCase());
        }
    }
}