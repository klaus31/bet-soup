package beso.base;

import java.io.PrintStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan({ "beso" })
@Configuration
public class SpringBesoConfig {

  @Bean
  public PrintStream getDefaultOutptPrintStream() {
    return System.out;
  }
}