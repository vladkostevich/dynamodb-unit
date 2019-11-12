package by.vladkostevich.dynamodbunit.manager.executor;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import by.vladkostevich.dynamodbunit.manager.bean.DynamoDbGsi;
import by.vladkostevich.dynamodbunit.manager.bean.DynamoDbTableDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class DynamoDbRequestsExecutor {

    private final AmazonDynamoDB amazonDynamoDB;

    @Autowired
    public DynamoDbRequestsExecutor(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
    }

    public void executeCreateTables(List<DynamoDbTableDefinition> tableDefinitions) {
        tableDefinitions.forEach(tableDefinition -> {
            CreateTableRequest createTableRequest = new CreateTableRequest(
                    tableDefinition.getAttributes(),
                    tableDefinition.getTableName(),
                    tableDefinition.getKeySchema(),
                    new ProvisionedThroughput(1L, 1L));
            createTableRequest.withGlobalSecondaryIndexes(covertGsiList(tableDefinition.getGlobalSecondaryIndexes()));

            amazonDynamoDB.createTable(createTableRequest);
        });
    }

    public void executeDropTables(List<String> tableNames) {
        tableNames.forEach(amazonDynamoDB::deleteTable);
    }

    private List<GlobalSecondaryIndex> covertGsiList(List<DynamoDbGsi> gsiBeans) {
        return gsiBeans.stream().map(gsiBean -> new GlobalSecondaryIndex()
                .withIndexName(gsiBean.getIndexName())
                .withKeySchema(gsiBean.getKeySchema())
                .withProjection(new Projection().withProjectionType(gsiBean.getProjectionType()))
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L))
        ).collect(toList());
    }

}
