package by.vladkostevich.dynamodbunit.manager.bean;

import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;

import java.util.List;
import java.util.Objects;

public class DynamoDbTableDefinition {

    private String tableName;
    private List<AttributeDefinition> attributes;
    private List<KeySchemaElement> keySchema;
    private List<DynamoDbGsi> globalSecondaryIndexes;

    public DynamoDbTableDefinition() {
    }

    public DynamoDbTableDefinition(
            String tableName,
            List<AttributeDefinition> attributes,
            List<KeySchemaElement> keySchema,
            List<DynamoDbGsi> globalSecondaryIndexes) {

        this.tableName = tableName;
        this.attributes = attributes;
        this.keySchema = keySchema;
        this.globalSecondaryIndexes = globalSecondaryIndexes;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<AttributeDefinition> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeDefinition> attributes) {
        this.attributes = attributes;
    }

    public List<KeySchemaElement> getKeySchema() {
        return keySchema;
    }

    public void setKeySchema(List<KeySchemaElement> keySchema) {
        this.keySchema = keySchema;
    }

    public List<DynamoDbGsi> getGlobalSecondaryIndexes() {
        return globalSecondaryIndexes;
    }

    public void setGlobalSecondaryIndexes(List<DynamoDbGsi> globalSecondaryIndexes) {
        this.globalSecondaryIndexes = globalSecondaryIndexes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamoDbTableDefinition that = (DynamoDbTableDefinition) o;
        return Objects.equals(tableName, that.tableName) &&
                Objects.equals(attributes, that.attributes) &&
                Objects.equals(keySchema, that.keySchema) &&
                Objects.equals(globalSecondaryIndexes, that.globalSecondaryIndexes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, attributes, keySchema, globalSecondaryIndexes);
    }

}
