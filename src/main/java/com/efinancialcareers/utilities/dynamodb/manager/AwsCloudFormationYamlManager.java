package com.efinancialcareers.utilities.dynamodb.manager;

import com.efinancialcareers.utilities.dynamodb.manager.bean.DynamoDbTableDefinition;
import com.efinancialcareers.utilities.dynamodb.manager.executor.DynamoDbRequestsExecutor;
import com.efinancialcareers.utilities.dynamodb.manager.parser.AwsCloudFormationYamlParser;
import com.efinancialcareers.utilities.dynamodb.state.StateContextHolder;
import com.efinancialcareers.utilities.dynamodb.util.FileContentResolver;
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
