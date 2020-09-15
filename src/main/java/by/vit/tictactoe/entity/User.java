package by.vit.tictactoe.entity;

import by.vit.tictactoe.repository.UserRepository;
import by.vit.tictactoe.validation.Unique;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class User extends Entity {

    @NotBlank
    @Unique(repository = UserRepository.class)
    private String username;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Role> roles = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer score;

}
