package com.example.bookstore.rest.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Marshaller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.example.bookstore.rest.controller" }, useDefaultFilters = false, includeFilters = { @Filter(Controller.class),  @Filter(ControllerAdvice.class) })
public class RestConfig extends WebMvcConfigurerAdapter {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
          .addResourceLocations("classpath:/META-INF/resources/");
     
        registry.addResourceHandler("/webjars/**")
          .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

	@Override
	public void configureContentNegotiation(
			ContentNegotiationConfigurer configurer) {
		configurer
			.useJaf(true)
			.favorPathExtension(true)
			.favorParameter(false)
			.ignoreAcceptHeader(false)
			.defaultContentType(MediaType.APPLICATION_JSON)
			.mediaType("json", MediaType.APPLICATION_JSON)
			.mediaType("xml", MediaType.APPLICATION_XML);
	}

	@Override
	public void configureMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		converters.add(mappingJacksonHttpMessageConverter());
		converters.add(marshallingHttpMessageConverter());
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setPrettyPrint(true);
		return converter;
	}
	
	@Bean 
	public MarshallingHttpMessageConverter marshallingHttpMessageConverter() {
		MarshallingHttpMessageConverter converter = new MarshallingHttpMessageConverter();
		converter.setMarshaller(jaxb2Marshaller());
		converter.setUnmarshaller(jaxb2Marshaller());
		return converter;
	}

	@Bean
	public Jaxb2Marshaller jaxb2Marshaller()  {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan(new String[] {
				"com.example.bookstore.common.domain",
				"com.example.bookstore.rest.domain",
				"org.springframework.hateoas"});
		
		Map<String, Object> marshallerProperties = new HashMap<String, Object>();
		marshallerProperties.put(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setMarshallerProperties(marshallerProperties);

		return marshaller;
	}


//	@Override
//	public void configureHandlerExceptionResolvers(
//			List<HandlerExceptionResolver> exceptionResolvers) {
//		exceptionResolvers.add(exceptionHandlerExceptionResolver());
//	}
//	
//	@Bean
//	public ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver() {
//		ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();
//		List<HttpMessageConverter<?>> converters = resolver.getMessageConverters();
//		converters.add(mappingJacksonHttpMessageConverter());
//		converters.add(marshallingHttpMessageConverter());
//		resolver.setMessageConverters(converters);
//
//		ContentNegotiationManagerFactoryBean contentNegotiationManager = new ContentNegotiationManagerFactoryBean();
//		contentNegotiationManager.addMediaType("json", MediaType.APPLICATION_JSON);
//		contentNegotiationManager.addMediaType("xml", MediaType.APPLICATION_XML);
//		contentNegotiationManager.afterPropertiesSet();
//		resolver.setContentNegotiationManager(contentNegotiationManager.getObject());
//
//		return resolver;
//	}

}
