package com.next.dynamo;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.lightadmin.api.config.LightAdmin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
public class App extends SpringBootServletInitializer {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(App.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(App.class);
	}

	@Bean
	public DateTimeProvider dateTimeProvider() {
		return CurrentDateTimeProvider.INSTANCE;
	}

	@Bean
	public LocalValidatorFactoryBean localValidatorFactoryBean(MessageSource messageSource) {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(messageSource);
		return localValidatorFactoryBean;
	}

	// Light Admin
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		LightAdmin.configure(servletContext).basePackage("com.next.dynamo.admin").baseUrl("/admin")
				.security(false).backToSiteUrl("http://lightadmin.org");

		super.onStartup(servletContext);
	}

}
