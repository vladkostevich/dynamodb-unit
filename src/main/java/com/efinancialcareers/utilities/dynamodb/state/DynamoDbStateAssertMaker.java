package com.efinancialcareers.utilities.dynamodb.state;

import com.efinancialcareers.utilities.dynamodb.params.DynamoDbExpectedParams;
import com.efinancialcareers.utilities.dynamodb.state.parser.StateFileParser;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.util.Assert.notNull;

@Component
public class DynamoDbStateAssertMaker {

    private final DynamoDbExceptedStateResolver exceptedStateResolver;

    private final StateFileParser stateFileParser;
    private final DynamoDbActualStateLoader actualStateLoader;

    @Autowired
    public DynamoDbStateAssertMaker(
            DynamoDbExceptedStateResolver exceptedStateResolver,
            StateFileParser stateFileParser,
            DynamoDbActualStateLoader actualStateLoader) {

        this.exceptedStateResolver = exceptedStateResolver;
        this.stateFileParser = stateFileParser;
        this.actualStateLoader = actualStateLoader;
    }

    public void assertState(DynamoDbExpectedParams expectedParams) {
        notNull(expectedParams, "ExpectedParams can't be null");

        String expectedState = exceptedStateResolver.resolveExceptedState(expectedParams);

        List<String> tableNames = stateFileParser.parseTableNames(expectedState);
        String actualState = actualStateLoader.loadActualState(tableNames);

        try {
            assertEquals(expectedState, actualState, expectedParams.isStrictAssert());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
