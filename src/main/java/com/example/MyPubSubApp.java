package com.example;

import com.google.api.gax.core.ExecutorProvider;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class MyPubSubApp {



  @Bean(name = "pubSubAsynchronousPullExecutor")
  public ExecutorService virtualThreadPullExecutor() {
    return Executors.newVirtualThreadPerTaskExecutor();
  }

  @Bean(name="subscriberExecutorProvider")
  public ExecutorProvider virtualThreadExecutorProvider() {
    return () -> Executors.newVirtualThreadPerTaskExecutor();
  }

  @Bean
  public CommandLineRunner commandLineRunner(PubSubTemplate pubSubTemplate) {

    return args -> {
      String subscriptionName = "projects/mpeddada-test/subscriptions/exampleSubscription";
      Subscriber subscriber = pubSubTemplate.subscribe(subscriptionName,
          message -> {
            System.out.println(
                "Message received from "
                    + subscriptionName
                    + " subscription: "
                    + message.getPubsubMessage().getData().toStringUtf8());
            message.ack();
          });
    };
  }

  public static void main(String[] args) {
    SpringApplication.run(MyPubSubApp.class, args);
  }
}