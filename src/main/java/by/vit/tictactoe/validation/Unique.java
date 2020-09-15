package by.vit.tictactoe.validation;

import by.vit.tictactoe.entity.Entity;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {

    String message() default "This value must be unique.";

    Class<? extends MongoRepository<? extends Entity, String>> repository();

}
