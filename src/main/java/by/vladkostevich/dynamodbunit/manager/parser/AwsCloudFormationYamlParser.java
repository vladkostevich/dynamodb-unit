package by.vladkostevich.dynamodbunit.manager.parser;

import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import by.vladkostevich.dynamodbunit.manager.bean.DynamoDbGsi;
import by.vladkostevich.dynamodbunit.manager.bean.DynamoDbTableDefinition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class AwsCloudFormationYamlParser {

    private final ObjectMapper objectMapper;

    public AwsCloudFormationYamlParser() {
        this.objectMapper = new ObjectMapper(new YAMLFactory());
    }

    public List<DynamoDbTableDefinition> parseTableDefinitions(String fileContent) {
        List<JsonNode> tablePropertiesNodes = getTablePropertiesNodes(fileContent);
        if (isEmpty(tablePropertiesNodes)) {
            return emptyList();
        }

        return tablePropertiesNodes.stream().map(tablePropertiesNode -> {
            String tableName = getTableName(tablePropertiesNode);
            List<AttributeDefinition> attributes = getAttributes(tablePropertiesNode);
            List<KeySchemaElement> keySchema = getKeySchema(tablePropertiesNode);
            List<DynamoDbGsi> gsiList = getGlobalSecondaryIndexes(tablePropertiesNode);
            return new DynamoDbTableDefinition(tableName, attributes, keySchema, gsiList);
        }).collect(toList());
    }

    public List<String> parseTableNames(String fileContent) {
        List<JsonNode> tablePropertiesNodes = getTablePropertiesNodes(fileContent);
        if (isEmpty(tablePropertiesNodes)) {
            return emptyList();
        }

        return tablePropertiesNodes.stream().map(this::getTableName).collect(toList());
    }

    private List<JsonNode> getTablePropertiesNodes(String fileContent) {
        hasText(fileContent, "Yaml File Content can't be blank");

        List<JsonNode> tablePropertiesNodes = new ArrayList<>();
        try {
            JsonNode yamlFileTree = objectMapper.readTree(fileContent);
            JsonNode resourcesNode = yamlFileTree.get("Resources");
            notNull(resourcesNode, "Resource Node is not found in Yaml file");

            Iterator<Map.Entry<String, JsonNode>> resourceNodes = resourcesNode.fields();
            while (resourceNodes.hasNext()) {
                Map.Entry<String, JsonNode> resourceNode = resourceNodes.next();
                JsonNode resourceBodyNode = resourceNode.getValue();
                if (!"AWS::DynamoDB::Table".equals(resourceBodyNode.get("Type").asText())) {
                    continue;
                }

                JsonNode tablePropertiesNode = resourceBodyNode.get("Properties");
                notNull(tablePropertiesNode, "Table Resource doesn't have Properties specified");

                tablePropertiesNodes.add(tablePropertiesNode);
            }

            return tablePropertiesNodes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getTableName(JsonNode tablePropertiesNode) {
        String tableName = tablePropertiesNode.get("TableName").asText();
        hasText(tableName, "TableName can't be blank");
        return tableName;
    }

    private List<AttributeDefinition> getAttributes(JsonNode tablePropertiesNode) {
        JsonNode tableAttributesNode = tablePropertiesNode.get("AttributeDefinitions");
        notNull(tableAttributesNode, "AttributeDefinitions can't be null");
        isTrue(tableAttributesNode.isArray(), "AttributeDefinitions should be an array");

        List<AttributeDefinition> attributes = new ArrayList<>();
        for (JsonNode tableAttributeNode : tableAttributesNode) {
            String attributeName = tableAttributeNode.get("AttributeName").asText();
            String attributeType = tableAttributeNode.get("AttributeType").asText();
            attributes.add(new AttributeDefinition(attributeName, attributeType));
        }

        return attributes;
    }

    private List<KeySchemaElement> getKeySchema(JsonNode tablePropertiesNode) {
        JsonNode keySchemaNode = tablePropertiesNode.get("KeySchema");
        notNull(keySchemaNode, "KeySchema can't be null");
        isTrue(keySchemaNode.isArray(), "KeySchema should be an array");

        List<KeySchemaElement> keySchema = new ArrayList<>();
        for (JsonNode keyNode : keySchemaNode) {
            String attributeName = keyNode.get("AttributeName").asText();
            String keyType = keyNode.get("KeyType").asText();
            keySchema.add(new KeySchemaElement(attributeName, keyType));
        }

        return keySchema;
    }

    private List<DynamoDbGsi> getGlobalSecondaryIndexes(JsonNode tablePropertiesNode) {
        JsonNode gsiListNode = tablePropertiesNode.get("GlobalSecondaryIndexes");
        if (gsiListNode == null) {
            return emptyList();
        }
        isTrue(gsiListNode.isArray(), "GlobalSecondaryIndexes should be an array");

        List<DynamoDbGsi> gsiList = new ArrayList<>();
        for (JsonNode gsiNode : gsiListNode) {
            String indexName = gsiNode.get("IndexName").asText();
            List<KeySchemaElement> keySchema = getKeySchema(gsiNode);

            JsonNode projectionNode = gsiNode.get("Projection");
            notNull(projectionNode, "Projection can't be null");
            String projectionType = projectionNode.get("ProjectionType").asText();
            List<String> projectionAttributes = new ArrayList<>();
            JsonNode projectionNonKeyAttributesNode = projectionNode.get("NonKeyAttributes");
            if (projectionNonKeyAttributesNode != null) {
                isTrue(projectionNonKeyAttributesNode.isArray(), "NonKeyAttributes should be an array");
                for (JsonNode projectionNonKeyAttribute : projectionNonKeyAttributesNode) {
                    projectionAttributes.add(projectionNonKeyAttribute.asText());
                }
            }

            gsiList.add(new DynamoDbGsi(indexName, keySchema, projectionType, projectionAttributes));
        }

        return gsiList;
    }
}
