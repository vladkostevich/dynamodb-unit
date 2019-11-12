package by.vladkostevich.dynamodbunit.manager.bean;

import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;

import java.util.List;
import java.util.Objects;

public class DynamoDbGsi {

    private String indexName;
    private List<KeySchemaElement> keySchema;
    private String projectionType;
    private List<String> projectionAttributes;

    public DynamoDbGsi() {
    }

    public DynamoDbGsi(
            String indexName,
            List<KeySchemaElement> keySchema,
            String projectionType,
            List<String> projectionAttributes) {

        this.indexName = indexName;
        this.keySchema = keySchema;
        this.projectionType = projectionType;
        this.projectionAttributes = projectionAttributes;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public List<KeySchemaElement> getKeySchema() {
        return keySchema;
    }

    public void setKeySchema(List<KeySchemaElement> keySchema) {
        this.keySchema = keySchema;
    }

    public String getProjectionType() {
        return projectionType;
    }

    public void setProjectionType(String projectionType) {
        this.projectionType = projectionType;
    }

    public List<String> getProjectionAttributes() {
        return projectionAttributes;
    }

    public void setProjectionAttributes(List<String> projectionAttributes) {
        this.projectionAttributes = projectionAttributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamoDbGsi that = (DynamoDbGsi) o;
        return Objects.equals(indexName, that.indexName) &&
                Objects.equals(keySchema, that.keySchema) &&
                Objects.equals(projectionType, that.projectionType) &&
                Objects.equals(projectionAttributes, that.projectionAttributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(indexName, keySchema, projectionType, projectionAttributes);
    }
}
