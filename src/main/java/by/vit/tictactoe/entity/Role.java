package by.vit.tictactoe.entity;

import by.vit.tictactoe.repository.RoleRepository;
import by.vit.tictactoe.validation.Unique;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
public class Role extends Entity {

    @NotBlank
    @Unique(repository = RoleRepository.class)
    private String name;

}
