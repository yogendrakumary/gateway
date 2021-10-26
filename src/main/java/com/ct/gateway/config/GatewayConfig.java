package com.ct.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ct.gateway.filter.JwtRequestFilter;

@Configuration
public class GatewayConfig {

	@Autowired
	private JwtRequestFilter filter;

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("user-service", r -> r.path("/auth/**").filters(f -> f.filter(filter)).uri("lb://user-service"))
				.route("user-service", r -> r.path("/users/**").filters(f -> f.filter(filter)).uri("lb://user-service"))
				.route("user-service", r -> r.path("/roles/**").filters(f -> f.filter(filter)).uri("lb://user-service"))
				.build();
	}
}
