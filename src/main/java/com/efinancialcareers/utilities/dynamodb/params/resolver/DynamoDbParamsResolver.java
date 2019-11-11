package com.efinancialcareers.utilities.dynamodb.params.resolver;

import com.efinancialcareers.utilities.dynamodb.annotations.DynamoDbExpected;
import com.efinancialcareers.utilities.dynamodb.annotations.DynamoDbSetup;
import com.efinancialcareers.utilities.dynamodb.annotations.DynamoDbStructureSetup;
import com.efinancialcareers.utilities.dynamodb.enums.DynamoDbStructureFileType;
import com.efinancialcareers.utilities.dynamodb.manager.EmbeddedDynamoDbManager;
import com.efinancialcareers.utilities.dynamodb.params.DynamoDbExpectedParams;
import com.efinancialcareers.utilities.dynamodb.params.DynamoDbSetupParams;
import com.efinancialcareers.utilities.dynamodb.params.DynamoDbStructureSetupParams;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class DynamoDbParamsResolver {

    public DynamoDbStructureSetupParams resolveStructureSetupParams(Method method) {
        DynamoDbStructureSetup dynamoDbStructureSetup;
        if (method.getAnnotation(DynamoDbStructureSetup.class) != null) {
            dynamoDbStructureSetup = method.getAnnotation(DynamoDbStructureSetup.class);
        } else if (method.getDeclaringClass().getAnnotation(DynamoDbStructureSetup.class) != null) {
            dynamoDbStructureSetup = method.getDeclaringClass().getAnnotation(DynamoDbStructureSetup.class);
        } else {
            return null;
        }

        String setupStructureFilePath = dynamoDbStructureSetup.structureFile();
        DynamoDbStructureFileType setupStructureFileType = dynamoDbStructureSetup.structureFileType();
        Class<? extends EmbeddedDynamoDbManager> customManagerClass =
                dynamoDbStructureSetup.customManager().equals(EmbeddedDynamoDbManager.class)
                        ? null : dynamoDbStructureSetup.customManager();

        return new DynamoDbStructureSetupParams(setupStructureFilePath, setupStructureFileType, customManagerClass);
    }

    public DynamoDbSetupParams resolveSetupParams(Method method) {
        DynamoDbSetup dynamoDbSetup;
        if (method.getAnnotation(DynamoDbSetup.class) != null) {
            dynamoDbSetup = method.getAnnotation(DynamoDbSetup.class);
        } else if (method.getDeclaringClass().getAnnotation(DynamoDbSetup.class) != null) {
            dynamoDbSetup = method.getDeclaringClass().getAnnotation(DynamoDbSetup.class);
        } else {
            return null;
        }

        String setupStateFilePath = dynamoDbSetup.value();

        return new DynamoDbSetupParams(setupStateFilePath);
    }

    public DynamoDbExpectedParams resolveExpectedParams(Method method) {
        DynamoDbExpected dynamoDbExpected = method.getAnnotation(DynamoDbExpected.class);
        if (dynamoDbExpected == null) {
            return null;
        }

        String expectedStateFilePath = dynamoDbExpected.value();
        boolean strictAssert = dynamoDbExpected.strictAssert();
        boolean relativeToSetup = dynamoDbExpected.relativeToSetup();
        boolean noChanges = dynamoDbExpected.noChanges();

        return new DynamoDbExpectedParams(expectedStateFilePath, strictAssert, relativeToSetup, noChanges);
    }
}
