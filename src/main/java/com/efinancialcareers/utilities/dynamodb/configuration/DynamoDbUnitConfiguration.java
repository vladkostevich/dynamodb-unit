package com.efinancialcareers.utilities.dynamodb.configuration;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Configuration
@ComponentScan({
        "com.efinancialcareers.utilities.dynamodb.manager",
        "com.efinancialcareers.utilities.dynamodb.params.resolver",
        "com.efinancialcareers.utilities.dynamodb.state",
        "com.efinancialcareers.utilities.dynamodb.util"
})
public class DynamoDbUnitConfiguration {

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        URL dynamoDbResource = this.getClass().getClassLoader().getResource("dynamodb");
        if (dynamoDbResource == null) {
            throw new RuntimeException("DynamoDB is not found");
        }

        System.setProperty("sqlite4java.library.path", dynamoDbResource.getPath());
        return DynamoDBEmbedded.create().amazonDynamoDB();
    }

    @Bean
    public DynamoDB dynamoDB(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDB(amazonDynamoDB);
    }

}
