package by.vit.tictactoe.controller;

import by.vit.tictactoe.entity.Role;
import by.vit.tictactoe.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<Role> getAll() {
        return roleService.getAll();
    }

    @GetMapping("/{id}")
    public Role getById(@PathVariable final String id) {
        return roleService.getById(id).orElse(null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String save(@RequestBody @Valid final Role role) {
        return roleService.save(role);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable final String id) {
        roleService.deleteById(id);
    }

}
