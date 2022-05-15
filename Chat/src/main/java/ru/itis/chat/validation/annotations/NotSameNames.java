package ru.itis.chat.validation.annotations;

import ru.itis.chat.validation.validators.NamesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NamesValidator.class)
public @interface NotSameNames {
    String message() default "Same names";

    String[] names() default {};

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
