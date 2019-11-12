package by.vladkostevich.dynamodbunit.state;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class DynamoDbActualStateLoader {

    private final DynamoDB dynamoDB;

    @Autowired
    public DynamoDbActualStateLoader(DynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }

    public String loadActualState(List<String> tableNames) {
        if (isEmpty(tableNames)) {
            return null;
        }

        StringBuilder actualDbStateBuilder = new StringBuilder("{");
        for (String tableName : tableNames) {
            Table table = dynamoDB.getTable(tableName);
            notNull(table, "Table " + tableName + " is not found");

            actualDbStateBuilder.append("\"").append(tableName).append("\": {\"items\": [");
            for (Item item : table.scan(new ScanSpec())) {
                actualDbStateBuilder.append(item.toJSONPretty()).append(",");
            }
            actualDbStateBuilder.deleteCharAt(actualDbStateBuilder.length() - 1);
            actualDbStateBuilder.append("]},");
        }
        actualDbStateBuilder.deleteCharAt(actualDbStateBuilder.length() - 1);
        actualDbStateBuilder.append("}");

        return actualDbStateBuilder.toString();
    }
}
