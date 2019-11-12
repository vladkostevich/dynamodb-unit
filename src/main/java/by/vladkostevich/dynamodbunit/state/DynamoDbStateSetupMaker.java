package by.vladkostevich.dynamodbunit.state;

import by.vladkostevich.dynamodbunit.params.DynamoDbSetupParams;
import by.vladkostevich.dynamodbunit.state.parser.StateFileParser;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import by.vladkostevich.dynamodbunit.util.FileContentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class DynamoDbStateSetupMaker {

    private final FileContentResolver fileContentResolver;
    private final StateFileParser stateFileParser;
    private final StateContextHolder stateContextHolder;
    private final DynamoDB dynamoDB;

    @Autowired
    public DynamoDbStateSetupMaker(
            FileContentResolver fileContentResolver,
            StateFileParser stateFileParser,
            StateContextHolder stateContextHolder,
            DynamoDB dynamoDB) {

        this.fileContentResolver = fileContentResolver;
        this.stateFileParser = stateFileParser;
        this.stateContextHolder = stateContextHolder;
        this.dynamoDB = dynamoDB;
    }

    public void setupDynamoDbState(DynamoDbSetupParams expectedParams) {
        String stateFilePath = expectedParams.getDynamoDbStateFilePath();
        hasText(stateFilePath, "State File Path can't be blank");

        String stateFileContent = fileContentResolver.resolveFileContent(stateFilePath);
        hasText(stateFileContent, "State File Content can't be blank");
        stateContextHolder.setSetupDynamoDbFileContent(stateFileContent);

        Map<String, List<Item>> tableNamesWithItems =
                stateFileParser.parseTableNamesWithItems(stateFileContent);
        if (isEmpty(tableNamesWithItems)) {
            return;
        }

        for (Map.Entry<String, List<Item>> tableWithItems : tableNamesWithItems.entrySet()) {
            Table table = dynamoDB.getTable(tableWithItems.getKey());
            notNull(table, "Wrong table name specified " + tableWithItems.getKey());

            for (Item item : tableWithItems.getValue()) {
                table.putItem(item);
            }
        }
    }
}
