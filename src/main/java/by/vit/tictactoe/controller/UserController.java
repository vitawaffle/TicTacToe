package by.vit.tictactoe.controller;

import by.vit.tictactoe.entity.User;
import by.vit.tictactoe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getPaginated(
            @RequestParam final Optional<Integer> page,
            @RequestParam final Optional<Integer> size
    ) {
        return userService.getPaginated(PageRequest.of(
                page.orElse(0),
                size.orElse(30)
        )).getContent();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable final String id) {
        return userService.getById(id).orElse(null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody @Valid final User user) {
        return userService.create(user);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable final String id) {
        userService.deleteById(id);
    }

}
