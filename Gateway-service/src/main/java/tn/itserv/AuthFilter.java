package tn.itserv;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;


import tn.itserv.model.UserDTO;
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
 
	private final WebClient.Builder webClientBuilder;
	
	public AuthFilter(Builder webClientBuilder) {
		super(Config.class);
		this.webClientBuilder = webClientBuilder;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			System.out.println(exchange.getRequest().getHeaders());
			
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("Missing authorization information");
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            String[] parts = authHeader.split(" ");

            if (parts.length != 2 || !"Bearer".equals(parts[0])) {
                throw new RuntimeException("Incorrect authorization structure");
            }

            return webClientBuilder.build()
                    .post()
                    .uri("http://cms-auth/validateToken?token=" + parts[1])
                    .retrieve().bodyToMono(UserDTO.class)
                    .map(userDto -> {
                        exchange.getRequest()
                                .mutate()
                                .header("X-auth-user-id", String.valueOf(userDto.getUsername()));
                        System.out.println(userDto);
                        return exchange;
                    }).flatMap(chain::filter);
        };
	}
	  
	
	public static class Config {
        // ...
    }
}
