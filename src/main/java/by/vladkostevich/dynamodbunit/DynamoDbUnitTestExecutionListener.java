package by.vladkostevich.dynamodbunit;

import by.vladkostevich.dynamodbunit.manager.EmbeddedDynamoDbManager;
import by.vladkostevich.dynamodbunit.manager.EmbeddedDynamoDbManagerFactory;
import by.vladkostevich.dynamodbunit.params.DynamoDbExpectedParams;
import by.vladkostevich.dynamodbunit.params.resolver.DynamoDbParamsResolver;
import by.vladkostevich.dynamodbunit.params.DynamoDbSetupParams;
import by.vladkostevich.dynamodbunit.params.DynamoDbStructureSetupParams;
import by.vladkostevich.dynamodbunit.state.DynamoDbStateAssertMaker;
import by.vladkostevich.dynamodbunit.state.DynamoDbStateSetupMaker;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import static org.springframework.util.Assert.notNull;

public class DynamoDbUnitTestExecutionListener extends AbstractTestExecutionListener {

    private ApplicationContext applicationContext;
    private EmbeddedDynamoDbManagerFactory managerFactory;
    private DynamoDbParamsResolver paramsResolver;

    @Override
    public void beforeTestClass(TestContext testContext) {
        this.applicationContext = testContext.getApplicationContext();
        this.managerFactory = this.applicationContext.getBean(EmbeddedDynamoDbManagerFactory.class);
        this.paramsResolver = this.applicationContext.getBean(DynamoDbParamsResolver.class);
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        // Create DynamoDB structure (tables and indexes)
        EmbeddedDynamoDbManager dynamoDbManager = getManager(testContext);
        dynamoDbManager.create();

        // Setup DynamoDb state (put items into tables)
        DynamoDbSetupParams setupParams = paramsResolver.resolveSetupParams(testContext.getTestMethod());
        if (setupParams != null) {
            DynamoDbStateSetupMaker stateSetupMaker =
                    testContext.getApplicationContext().getBean(DynamoDbStateSetupMaker.class);
            stateSetupMaker.setupDynamoDbState(setupParams);
        }
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        try {
            // Assert expected and actual DynamoDb states
            DynamoDbExpectedParams expectedParams = paramsResolver.resolveExpectedParams(testContext.getTestMethod());
            if (expectedParams != null) {
                DynamoDbStateAssertMaker assertMaker =
                        testContext.getApplicationContext().getBean(DynamoDbStateAssertMaker.class);
                assertMaker.assertState(expectedParams);
            }
        } finally {
            // Drop DynamoDb tables and indexes
            EmbeddedDynamoDbManager dynamoDbManager = getManager(testContext);
            dynamoDbManager.drop();
        }
    }

    private EmbeddedDynamoDbManager getManager(TestContext testContext) {
        DynamoDbStructureSetupParams structureSetupParams =
                paramsResolver.resolveStructureSetupParams(testContext.getTestMethod());

        if (structureSetupParams.getCustomManagerClass() != null) {
            EmbeddedDynamoDbManager customManager =
                    applicationContext.getBean(structureSetupParams.getCustomManagerClass());
            notNull(customManager, "Custom EmbeddedDynamoDbManager not found in ApplicationContext");
            return customManager;
        }

        return managerFactory.getManager(structureSetupParams);
    }

}
