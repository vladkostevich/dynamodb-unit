package by.vladkostevich.dynamodbunit.params;

import java.util.Objects;

public class DynamoDbSetupParams {

    private String setupStateFilePath;

    public DynamoDbSetupParams() {
    }

    public DynamoDbSetupParams(String setupStateFilePath) {
        this.setupStateFilePath = setupStateFilePath;
    }

    public String getDynamoDbStateFilePath() {
        return setupStateFilePath;
    }

    public void setDynamoDbStateFilePath(String dynamoDbStateFilePath) {
        this.setupStateFilePath = dynamoDbStateFilePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamoDbSetupParams that = (DynamoDbSetupParams) o;
        return Objects.equals(setupStateFilePath, that.setupStateFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(setupStateFilePath);
    }

}
