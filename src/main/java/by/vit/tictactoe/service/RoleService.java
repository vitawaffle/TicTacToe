package by.vit.tictactoe.service;

import by.vit.tictactoe.entity.Role;

import java.util.List;

public interface RoleService extends AppService<Role, String> {

    List<Role> getAll();

}
