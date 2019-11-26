package com.everis.springboot.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.everis.springboot.app.handlers.StudentHandler;

@Configuration
public class RouterConfig {
	
	@Bean
	public RouterFunction<ServerResponse> rutas(StudentHandler handler){
		return RouterFunctions.route(RequestPredicates.GET("/apihandler/student"), handler::findAll)
				.andRoute(RequestPredicates.POST("/apihandler/student"), handler::create)
				.andRoute(RequestPredicates.PUT("/apihandler/student/{id}"), handler::edit)
				.andRoute(RequestPredicates.GET("/apihandler/student/findFamily/{id}"), handler::findFamily)
				.andRoute(RequestPredicates.DELETE("/apihandler/student/{id}"), handler::delete);
	}
}
