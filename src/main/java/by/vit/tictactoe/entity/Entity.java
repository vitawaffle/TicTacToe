package by.vit.tictactoe.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Entity {

    @Id
    private String id;

}
