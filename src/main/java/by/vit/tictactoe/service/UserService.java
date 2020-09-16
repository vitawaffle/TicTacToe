package by.vit.tictactoe.service;

import by.vit.tictactoe.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService extends AppService<User, String> {

    Page<User> getPaginated(Pageable pageable);

    String create(User user);

}
