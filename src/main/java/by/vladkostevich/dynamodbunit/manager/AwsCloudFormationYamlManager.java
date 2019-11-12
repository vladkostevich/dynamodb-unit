package by.vladkostevich.dynamodbunit.manager;

import by.vladkostevich.dynamodbunit.manager.bean.DynamoDbTableDefinition;
import by.vladkostevich.dynamodbunit.manager.executor.DynamoDbRequestsExecutor;
import by.vladkostevich.dynamodbunit.manager.parser.AwsCloudFormationYamlParser;
import by.vladkostevich.dynamodbunit.state.StateContextHolder;
import by.vladkostevich.dynamodbunit.util.FileContentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.util.Assert.hasText;

@Component
public class AwsCloudFormationYamlManager implements EmbeddedDynamoDbManager {

    private final FileContentResolver fileContentResolver;
    private final AwsCloudFormationYamlParser parser;
    private final DynamoDbRequestsExecutor requestsExecutor;
    private final StateContextHolder stateContextHolder;

    private String yamlFilePath;

    @Autowired
    public AwsCloudFormationYamlManager(
            FileContentResolver fileContentResolver,
            AwsCloudFormationYamlParser parser,
            DynamoDbRequestsExecutor requestsExecutor,
            StateContextHolder stateContextHolder) {

        this.fileContentResolver = fileContentResolver;
        this.parser = parser;
        this.requestsExecutor = requestsExecutor;
        this.stateContextHolder = stateContextHolder;
    }

    @Override
    public void create() {
        hasText(yamlFilePath, "YAML File Path can't be blank");

        String yamlFileContent = fileContentResolver.resolveFileContent(yamlFilePath);
        hasText(yamlFileContent, "YAML File Content can't be blank");

        List<DynamoDbTableDefinition> tableDefinitions = parser.parseTableDefinitions(yamlFileContent);
        stateContextHolder.initTableToPrimaryKeyMap(tableDefinitions);

        requestsExecutor.executeCreateTables(tableDefinitions);
    }

    @Override
    public void drop() {
        hasText(yamlFilePath, "YAML File Path can't be blank");

        String yamlFileContent = fileContentResolver.resolveFileContent(yamlFilePath);
        hasText(yamlFileContent, "YAML File Content can't be blank");

        List<String> tableNames = parser.parseTableNames(yamlFileContent);

        requestsExecutor.executeDropTables(tableNames);
    }

    public AwsCloudFormationYamlManager withFilePath(String filePath) {
        this.yamlFilePath = filePath;
        return this;
    }

}
