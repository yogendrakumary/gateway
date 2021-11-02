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
				.route("admin-service",
						r -> r.path("/admin/**").filters(f -> f.filter(filter)).uri("lb://admin-service"))
				.route("inbox-service",
						r -> r.path("/inbox/**").filters(f -> f.filter(filter)).uri("lb://inbox-service"))
				.route("inbox-service",
						r -> r.path("/notes/**").filters(f -> f.filter(filter)).uri("lb://inbox-service"))
				.route("schedulling-service",
						r -> r.path("/api/**").filters(f -> f.filter(filter)).uri("lb://schedulling-service"))
				.route("patient-service",
						r -> r.path("/patient/**").filters(f -> f.filter(filter)).uri("lb://patient-service"))
				.route("patient-service",
						r -> r.path("/allergy/**").filters(f -> f.filter(filter)).uri("lb://patient-service"))
				.route("patient-service",
						r -> r.path("/diagnosis/**").filters(f -> f.filter(filter)).uri("lb://patient-service"))
				.route("patient-service",
						r -> r.path("/drug/**").filters(f -> f.filter(filter)).uri("lb://patient-service"))
				.route("patient-service",
						r -> r.path("/procedure/**").filters(f -> f.filter(filter)).uri("lb://patient-service"))
				.route("patient-service",
						r -> r.path("/vital/**").filters(f -> f.filter(filter)).uri("lb://patient-service"))
				.build();
	}
}
