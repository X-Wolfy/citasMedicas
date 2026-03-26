package com.luis.gateway.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SecurityConfig {
	
	@Bean
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		
		http
	        .csrf(csrf -> csrf.disable()) // Se desactiva CSRF para API REST
	        .cors(cors -> cors.configurationSource(request -> {
	            CorsConfiguration corsConfiguration = new CorsConfiguration();
	            corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
	            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
	            corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
	            corsConfiguration.setAllowCredentials(true);
	            return corsConfiguration;
	        }))
	        .authorizeExchange((authorize) -> authorize
		            /*.pathMatchers(HttpMethod.OPTIONS ,"/**").permitAll()
		            .pathMatchers(HttpMethod.GET ,"/**").hasAnyRole("ADMIN", "USER")
		            .pathMatchers(HttpMethod.POST ,"/**").hasAnyRole("ADMIN", "USER")
		            .pathMatchers(HttpMethod.PUT ,"/**").hasAnyRole("ADMIN", "USER")
		            .pathMatchers(HttpMethod.PATCH ,"/**").hasAnyRole("ADMIN", "USER")
		            .pathMatchers(HttpMethod.DELETE ,"/**").hasRole("ADMIN")
		            .anyExchange().authenticated()*/
	        		.anyExchange().permitAll()
		        )
	        // Configura la app como Resource Server que valida JWT
	        .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt ->
	            jwt.jwtAuthenticationConverter(reactiveJwtAuthenticationConverterAdapter())));
	
	    return http.build();
	}
	
	@Bean
	ReactiveJwtAuthenticationConverterAdapter reactiveJwtAuthenticationConverterAdapter() {
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
	}

}
