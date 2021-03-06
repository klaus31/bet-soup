package beso.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
class SpringMongoConfig extends AbstractMongoConfiguration {

  @Override
  public String getDatabaseName() {
    return "beso";
  }

  @Override
  @Bean
  public Mongo mongo() throws Exception {
    return new MongoClient("127.0.0.1");
  }
}