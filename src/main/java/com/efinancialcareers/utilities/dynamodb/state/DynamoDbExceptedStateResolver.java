package com.efinancialcareers.utilities.dynamodb.state;

import com.efinancialcareers.utilities.dynamodb.params.DynamoDbExpectedParams;
import com.efinancialcareers.utilities.dynamodb.util.FileContentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.springframework.util.Assert.hasText;

@Component
public class DynamoDbExceptedStateResolver {

    private final StateContextHolder stateContextHolder;
    private final FileContentResolver fileContentResolver;

    @Autowired
    public DynamoDbExceptedStateResolver(
            StateContextHolder stateContextHolder,
            FileContentResolver fileContentResolver) {

        this.stateContextHolder = stateContextHolder;
        this.fileContentResolver = fileContentResolver;
    }

    public String resolveExceptedState(DynamoDbExpectedParams expectedParams) {
        if (expectedParams.isNoChanges()) {
            return stateContextHolder.getSetupDynamoDbFileContent();
        }

        String expectedStateFilePath = expectedParams.getExpectedStateFilePath();
        hasText(expectedStateFilePath, "Expected state file path can't be blank");

        String expectedState = fileContentResolver.resolveFileContent(expectedStateFilePath);

        if (expectedParams.isRelativeToSetup()) {

        }

        return null;
    }
}
