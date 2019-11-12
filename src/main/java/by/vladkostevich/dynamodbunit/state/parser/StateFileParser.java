package by.vladkostevich.dynamodbunit.state.parser;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

@Component
public class StateFileParser {

    private final ObjectMapper objectMapper;

    public StateFileParser() {
        objectMapper = new ObjectMapper();
    }

    public List<String> parseTableNames(String fileContent) {
        Iterator<Map.Entry<String, JsonNode>> fields = getJsonFields(fileContent);

        List<String> tableNames = new ArrayList<>();
        while (fields.hasNext()) {
            tableNames.add(fields.next().getKey());
        }

        return tableNames;
    }

    public Map<String, List<Item>> parseTableNamesWithItems(String fileContent) {
        Iterator<Map.Entry<String, JsonNode>> fields = getJsonFields(fileContent);

        Map<String, List<Item>> tableNamesWithItems = new HashMap<>();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();

            String tableName = field.getKey();
            JsonNode tableContentNode = field.getValue();
            JsonNode tableItemsNode = tableContentNode.get("items");
            notNull(tableItemsNode, "Items Node can't be null");
            isTrue(tableItemsNode.isArray(), "Items Node should be an array");

            List<Item> tableItems = new ArrayList<>(tableItemsNode.size());
            for (JsonNode itemNode : tableItemsNode) {
                tableItems.add(Item.fromJSON(itemNode.toString()));
            }

            tableNamesWithItems.put(tableName, tableItems);
        }

        return tableNamesWithItems;
    }

    private Iterator<Map.Entry<String, JsonNode>> getJsonFields(String fileContent) {
        try {
            JsonNode jsonTree = objectMapper.readTree(fileContent);
            return jsonTree.fields();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
