package by.vladkostevich.dynamodbunit.manager;

import by.vladkostevich.dynamodbunit.enums.DynamoDbStructureFileType;
import by.vladkostevich.dynamodbunit.params.DynamoDbStructureSetupParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static org.springframework.util.Assert.notNull;

@Component
public class EmbeddedDynamoDbManagerFactory {

    private final ApplicationContext applicationContext;

    @Autowired
    public EmbeddedDynamoDbManagerFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public EmbeddedDynamoDbManager getManager(DynamoDbStructureSetupParams structureSetupParams) {
        notNull(structureSetupParams, "Structure Setup Params can't be null");

        DynamoDbStructureFileType fileType = structureSetupParams.getStructureFileType();
        String filePath = structureSetupParams.getStructureSetupFile();

        switch (fileType) {
            case AWS_CLOUD_FORMATION_YAML:
                return applicationContext.getBean(AwsCloudFormationYamlManager.class).withFilePath(filePath);
            default:
                return null;
        }
    }

}
