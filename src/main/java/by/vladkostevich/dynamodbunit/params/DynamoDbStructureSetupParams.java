package by.vladkostevich.dynamodbunit.params;

import by.vladkostevich.dynamodbunit.manager.EmbeddedDynamoDbManager;
import by.vladkostevich.dynamodbunit.enums.DynamoDbStructureFileType;

import java.util.Objects;

public class DynamoDbStructureSetupParams {

    private String structureSetupFile;
    private DynamoDbStructureFileType structureFileType;
    private Class<? extends EmbeddedDynamoDbManager> customManagerClass;

    public DynamoDbStructureSetupParams() {
    }

    public DynamoDbStructureSetupParams(
            String structureSetupFile,
            DynamoDbStructureFileType structureFileType,
            Class<? extends EmbeddedDynamoDbManager> customManagerClass) {

        this.structureSetupFile = structureSetupFile;
        this.structureFileType = structureFileType;
        this.customManagerClass = customManagerClass;
    }

    public String getStructureSetupFile() {
        return structureSetupFile;
    }

    public void setStructureSetupFile(String structureSetupFile) {
        this.structureSetupFile = structureSetupFile;
    }

    public DynamoDbStructureFileType getStructureFileType() {
        return structureFileType;
    }

    public void setStructureFileType(DynamoDbStructureFileType structureFileType) {
        this.structureFileType = structureFileType;
    }

    public Class<? extends EmbeddedDynamoDbManager> getCustomManagerClass() {
        return customManagerClass;
    }

    public void setCustomManagerClass(Class<? extends EmbeddedDynamoDbManager> customManagerClass) {
        this.customManagerClass = customManagerClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamoDbStructureSetupParams that = (DynamoDbStructureSetupParams) o;
        return Objects.equals(structureSetupFile, that.structureSetupFile) &&
                structureFileType == that.structureFileType &&
                Objects.equals(customManagerClass, that.customManagerClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(structureSetupFile, structureFileType, customManagerClass);
    }

}
