package com.devsuperior.bds03.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	@Autowired
	private Environment env;
	
	@Autowired
	private JwtTokenStore tokenStore; 
	
	private static final String[] PUBLIC = { "/oauth/token", "/h2-console/**" };
	
	private static final String[] OPERATOR_GET = { "/departments/**", "/employees/**" };	
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore);
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		// Liberar o H2
		if (Arrays.asList(env.getActiveProfiles()).contains("test")){
			http.headers().frameOptions().disable();
			
		}
		
		http.authorizeRequests() //iniciar as configurações de autorizações
		.antMatchers(PUBLIC).permitAll() //Define autoriza~çoes de rota PUBLIC, permitindo tudo
		.antMatchers(HttpMethod.GET, OPERATOR_GET).hasAnyRole("OPERATOR","ADMIN") //Liberar o metodo GET para o vetor OPERATOR_GET
		.anyRequest().hasAnyRole("ADMIN");
	}
}
