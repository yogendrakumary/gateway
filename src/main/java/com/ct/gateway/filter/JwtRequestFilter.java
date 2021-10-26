package com.ct.gateway.filter;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.ct.gateway.exception.JwtTokenMalformedException;
import com.ct.gateway.exception.JwtTokenMissingException;
import com.ct.gateway.util.JwtTokenUtil;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class JwtRequestFilter implements GatewayFilter {

	@Autowired
	private JwtTokenUtil jwtUtil;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();

		final List<String> apiEndpoints = List.of("/roles","/auth/verify", "/users/patients/signup");

		Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
				.noneMatch(uri -> r.getURI().getPath().contains(uri));

		if (isApiSecured.test(request)) {
			if (!request.getHeaders().containsKey("Authorization")) {
				ServerHttpResponse response = exchange.getResponse();
				response.setStatusCode(HttpStatus.UNAUTHORIZED);

				return response.setComplete();
			}

			final String requestTokenHeader = request.getHeaders().getOrEmpty("Authorization").get(0);
			String token = "";
			if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
				try {
					token = requestTokenHeader.substring(7);
					jwtUtil.validateToken(token);
				} catch (JwtTokenMalformedException | JwtTokenMissingException e) {
					// e.printStackTrace();

					ServerHttpResponse response = exchange.getResponse();
					response.setStatusCode(HttpStatus.BAD_REQUEST);

					return response.setComplete();
				}

				Claims claims = jwtUtil.getClaims(token);
				exchange.getRequest().mutate().header("id", String.valueOf(claims.get("id"))).build();
			}

//			final String tokenWholeString = request.getHeaders().getOrEmpty("Authorization").get(0);
//			System.out.println("Authorization " + tokenWholeString);
//			String token = tokenWholeString.substring(7);
//			// String username = jwtUtil.extractUsername(token);
//
//			try {
//				jwtUtil.validateToken(token);
//			} catch (JwtTokenMalformedException | JwtTokenMissingException e) {
//				// e.printStackTrace();
//
//				ServerHttpResponse response = exchange.getResponse();
//				response.setStatusCode(HttpStatus.BAD_REQUEST);
//
//				return response.setComplete();
//			}
//			Claims claims = jwtUtil.getClaims(token);
//			exchange.getRequest().mutate().header("id", String.valueOf(claims.get("id"))).build();

		}
		System.out.println(chain.filter(exchange));

		return chain.filter(exchange);
	}

}