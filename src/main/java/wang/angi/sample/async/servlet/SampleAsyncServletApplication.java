package wang.angi.sample.async.servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SampleAsyncServletApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleAsyncServletApplication.class, args);
    }
}
