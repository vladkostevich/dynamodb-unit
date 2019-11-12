package by.vladkostevich.dynamodbunit.params.resolver;

import by.vladkostevich.dynamodbunit.annotations.DynamoDbExpected;
import by.vladkostevich.dynamodbunit.annotations.DynamoDbSetup;
import by.vladkostevich.dynamodbunit.annotations.DynamoDbStructureSetup;
import by.vladkostevich.dynamodbunit.manager.EmbeddedDynamoDbManager;
import by.vladkostevich.dynamodbunit.enums.DynamoDbStructureFileType;
import by.vladkostevich.dynamodbunit.params.DynamoDbExpectedParams;
import by.vladkostevich.dynamodbunit.params.DynamoDbSetupParams;
import by.vladkostevich.dynamodbunit.params.DynamoDbStructureSetupParams;
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
