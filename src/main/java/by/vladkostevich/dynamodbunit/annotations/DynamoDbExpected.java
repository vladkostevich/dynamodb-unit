package by.vladkostevich.dynamodbunit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DynamoDbExpected {

    String value() default "";

    boolean strictAssert() default false;

    boolean relativeToSetup() default false;

    boolean noChanges() default false;

}
