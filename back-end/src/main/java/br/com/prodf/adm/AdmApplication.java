package br.com.prodf.adm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import br.com.prodf.adm.config.AppProperties;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class AdmApplication {

	// private static final Logger log = LoggerFactory.getLogger(AdmApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AdmApplication.class, args);
	}

}
