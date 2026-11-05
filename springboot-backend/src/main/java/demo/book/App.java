package demo.book;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.*;
@SpringBootApplication
public class App {
 public static void main(String[] args){SpringApplication.run(App.class,args);}
 @Bean RestTemplate restTemplate(){return new RestTemplate();}
 @Bean WebMvcConfigurer cors(){return new WebMvcConfigurer(){public void addCorsMappings(CorsRegistry r){r.addMapping("/**").allowedOriginPatterns("*").allowedMethods("*").allowedHeaders("*");}};}
}
