package com.db.securityservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class AppConfig  {
	
	    @Bean
	    public UserDetailsService userDetailsService() {
	        UserDetails accUser = User.builder().
	                username("accountservice")
	                .password(passwordEncoder().encode("password")).roles("ADMIN").
	                build();
	        UserDetails txnUser = User.builder().
	                username("transactionservice")
	                .password(passwordEncoder().encode("password")).roles("ADMIN").
	                build(); 
	        UserDetails emailUser = User.builder().
	                username("emailservice")
	                .password(passwordEncoder().encode("password")).roles("ADMIN").
	                build();
	        return new InMemoryUserDetailsManager(accUser, txnUser, emailUser);
	    }

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
	        return builder.getAuthenticationManager();
	    }
   
}
