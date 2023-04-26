package egovframework;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Import;

import egovframework.com.config.EgovWebApplicationInitializer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ServletComponentScan
@SpringBootApplication
@Import({EgovWebApplicationInitializer.class})
public class EgovBootApplication {
	public static void main(String[] args) {
		log.debug("##### EgovBootApplication Start #####");

		SpringApplication springApplication = new SpringApplication(EgovBootApplication.class);
		springApplication.setBannerMode(Banner.Mode.OFF);
		//springApplication.setLogStartupInfo(false);
		springApplication.run(args);

		log.debug("##### EgovBootApplication End #####");
	}

}
