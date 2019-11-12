package by.vladkostevich.dynamodbunit.annotations;

import by.vladkostevich.dynamodbunit.enums.DynamoDbStructureFileType;
import by.vladkostevich.dynamodbunit.manager.EmbeddedDynamoDbManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static by.vladkostevich.dynamodbunit.enums.DynamoDbStructureFileType.AWS_CLOUD_FORMATION_YAML;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DynamoDbStructureSetup {

    String structureFile() default "dynamodb.yml";

    DynamoDbStructureFileType structureFileType() default AWS_CLOUD_FORMATION_YAML;

    Class<? extends EmbeddedDynamoDbManager> customManager() default EmbeddedDynamoDbManager.class;

}
