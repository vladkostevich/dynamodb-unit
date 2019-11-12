package by.vladkostevich.dynamodbunit.params;

import java.util.Objects;

public class DynamoDbExpectedParams {

    private String expectedStateFilePath;
    private boolean strictAssert;
    private boolean relativeToSetup;
    private boolean noChanges;

    public DynamoDbExpectedParams() {
    }

    public DynamoDbExpectedParams(
            String expectedStateFilePath,
            boolean strictAssert,
            boolean relativeToSetup,
            boolean noChanges) {

        this.expectedStateFilePath = expectedStateFilePath;
        this.strictAssert = strictAssert;
        this.relativeToSetup = relativeToSetup;
        this.noChanges = noChanges;
    }

    public String getExpectedStateFilePath() {
        return expectedStateFilePath;
    }

    public void setExpectedStateFilePath(String expectedStateFilePath) {
        this.expectedStateFilePath = expectedStateFilePath;
    }

    public boolean isStrictAssert() {
        return strictAssert;
    }

    public void setStrictAssert(boolean strictAssert) {
        this.strictAssert = strictAssert;
    }

    public boolean isRelativeToSetup() {
        return relativeToSetup;
    }

    public void setRelativeToSetup(boolean relativeToSetup) {
        this.relativeToSetup = relativeToSetup;
    }

    public boolean isNoChanges() {
        return noChanges;
    }

    public void setNoChanges(boolean noChanges) {
        this.noChanges = noChanges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamoDbExpectedParams that = (DynamoDbExpectedParams) o;
        return strictAssert == that.strictAssert &&
                relativeToSetup == that.relativeToSetup &&
                noChanges == that.noChanges &&
                Objects.equals(expectedStateFilePath, that.expectedStateFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expectedStateFilePath, strictAssert, relativeToSetup, noChanges);
    }
}
