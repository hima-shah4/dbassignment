package db.com.transactionservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import db.com.transactionservice.client.AccountServiceClient;
import lombok.Data;


@Configuration
@Data
@ConfigurationProperties(prefix = "client.accountservice")
public class AccountServiceConfig {
  @Value("${client.accountservice.baseurl}")
  private String baseurl;
  @Value("${client.accountservice.creditservice}")
  private String creditservice;
  @Value("${client.accountservice.debitservice}")
  private String debitservice;

  @Bean
  public WebClient accountServiceWebClient() {
      return WebClient.builder().baseUrl(baseurl).build();
  }
  
  @Bean
  public AccountServiceClient accountSerivceClient() {
	  @SuppressWarnings("removal")
	HttpServiceProxyFactory httpServiceProxyFactory =
			  HttpServiceProxyFactory.builder(WebClientAdapter.forClient(accountServiceWebClient()))
					.build();
	  return (AccountServiceClient) httpServiceProxyFactory.createClient(AccountServiceClient.class);		  
  }
  
}