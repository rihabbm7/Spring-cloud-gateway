package tn.itserv;


import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;
import tn.itserv.AuthFilter.Config;

@Configuration

public class CorsConfig {

	  @Bean
	  public WebFilter corsFilter() {
	    return (ServerWebExchange ctx, WebFilterChain chain) -> {
	      ServerHttpRequest request = ctx.getRequest();
	      if (CorsUtils.isCorsRequest(request)) {
	        ServerHttpResponse response = ctx.getResponse();
	        HttpHeaders headers = response.getHeaders();

	        headers.add("Access-Control-Allow-Origin", "*");
	        headers.add("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type,"
					+ " Access-Control-Request-Method, Access-Control-Request-Headers,Authorization");
	        headers.add("Access-Control-Expose-Headers","Access-Control-Allow-Origin, Access-Control-Allow-Credentials, Authorization");
	        headers.add("Access-Control-Allow-Methods", "POST, PUT,PATCH, GET, OPTIONS, DELETE");
			
	       System.out.println(headers.toSingleValueMap());
	        if (request.getMethod()==HttpMethod.OPTIONS) {
	          response.setStatusCode(HttpStatus.OK);
	          return Mono.empty();
	        }
	        
	      }
	      return chain.filter(ctx);
	    };
	  }
	  
	  @Bean
	  public RouteLocator myRoutes(RouteLocatorBuilder builder,AuthFilter authFilter) {
		  
	      return builder.routes()
	          .route(p -> p.path("/cms-dao/**").filters(f->f.rewritePath("/cms-dao/(?<remains>.*)","/${remains}")).uri("lb://cms-dao/"))
	          .route(p1->p1.path("/cms-sms/**").filters(f->f.rewritePath("/cms-sms/(?<remains>.*)","/${remains}")).uri("lb://cms-sms/"))
	          .route(p2->p2.path("/cms-auth/**").filters(f->f.rewritePath("/cms-auth/(?<remains>.*)","/${remains}")).uri("lb://cms-auth/"))
	          .route(p3->p3.path("/cms-automat/**")
	        		  .filters(f->f.rewritePath("/cms-automat/(?<remains>.*)","/${remains}")
	        		  .filter(authFilter.apply(new Config()))
	        		  )
	        		  .uri("lb://cms-automat/"))
	          .build();
	  }
	  
//	  @Bean
//	  DiscoveryClientRouteDefinitionLocator dynamicRoutes(ReactiveDiscoveryClient discoveryClient,DiscoveryLocatorProperties locatorProperties){
//		  return new DiscoveryClientRouteDefinitionLocator(discoveryClient, locatorProperties);
//	  }
	  @Bean
	    @LoadBalanced
	    public WebClient.Builder loadBalancedWebClientBuilder() {
	        return WebClient.builder();
	    }
}
