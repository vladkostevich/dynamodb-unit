package com.efinancialcareers.utilities.dynamodb.state;

import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.efinancialcareers.utilities.dynamodb.manager.bean.DynamoDbTableDefinition;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StateContextHolder {

    private Map<String, String> tableToPrimaryKeyMap = new HashMap<>();
    private String setupDynamoDbFileContent;

    public String getSetupDynamoDbFileContent() {
        return setupDynamoDbFileContent;
    }

    public void setSetupDynamoDbFileContent(String setupDynamoDbFileContent) {
        this.setupDynamoDbFileContent = setupDynamoDbFileContent;
    }

    public Map<String, String> getTableToPrimaryKeyMap() {
        return tableToPrimaryKeyMap;
    }

    public void initTableToPrimaryKeyMap(List<DynamoDbTableDefinition> tableDefinitions) {
        tableDefinitions.forEach(dynamoDbTableDefinition -> {
            String tableName = dynamoDbTableDefinition.getTableName();
            KeySchemaElement hashKey = dynamoDbTableDefinition.getKeySchema().stream()
                    .filter(keySchemaElement -> "HASH".equals(keySchemaElement.getKeyType()))
                    .findFirst().orElse(null);
            if (hashKey == null) {
                throw new IllegalStateException();
            }

            tableToPrimaryKeyMap.put(tableName, hashKey.getAttributeName());
        });
    }
}
