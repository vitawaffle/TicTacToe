package by.vit.tictactoe.validation;

import by.vit.tictactoe.entity.Entity;
import by.vit.tictactoe.repository.RoleRepository;
import by.vit.tictactoe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueValidator implements ConstraintValidator<Unique, String> {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private Class<? extends MongoRepository<? extends Entity, String>> repository;

    @Autowired
    public UniqueValidator(final RoleRepository roleRepository, final UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(final Unique constraintAnnotation) {
        repository = constraintAnnotation.repository();
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (repository.equals(RoleRepository.class)) {
            return value == null || roleRepository.findByName(value).isEmpty();
        }
        if (repository.equals(UserRepository.class)) {
            return value == null || userRepository.findByUsername(value).isEmpty();
        }
        throw new IllegalArgumentException("Class " + repository.getName() + " cannot be target of repository.");
    }

}
