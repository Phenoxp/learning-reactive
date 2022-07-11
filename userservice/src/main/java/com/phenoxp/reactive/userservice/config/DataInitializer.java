package com.phenoxp.reactive.userservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

@Component
@Profile("!postgres")
public class DataInitializer implements CommandLineRunner {

  @Value("classpath:h2/init.sql")
  private Resource sql;

  @Autowired private R2dbcEntityTemplate entityTemplate;

  @Override
  public void run(String... args) throws Exception {
    String query = StreamUtils.copyToString(sql.getInputStream(), StandardCharsets.UTF_8);
    System.out.println(query);

    entityTemplate.getDatabaseClient()
            .sql(query)
            .then()
            .subscribe();
  }
}
