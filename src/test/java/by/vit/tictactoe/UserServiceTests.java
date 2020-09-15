package by.vit.tictactoe;

import by.vit.tictactoe.entity.User;
import by.vit.tictactoe.repository.UserRepository;
import by.vit.tictactoe.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
        final User user = new User();
        user.setUsername("User1");
        user.setPassword("password");
        user.setScore(0);
        this.user = userRepository.save(user);
    }

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
    }

    @Test
    public void getPaginated_InRange_ShouldReturnNotEmpty() {
        assertFalse(userService.getPaginated(PageRequest.of(0, 10)).isEmpty());
    }

    @Test
    public void getPaginated_OutOfRange_ShouldReturnEmpty() {
        assertTrue(userService.getPaginated(PageRequest.of(100, 100)).isEmpty());
    }

    @Test
    public void getById_ExistingId_ShouldReturnUser() {
        assertTrue(userService.getById(user.getId()).isPresent());
    }

    @Test
    public void getById_NotExistingId_ShouldReturnNull() {
        assertFalse(userService.getById("").isPresent());
    }

    @Test
    public void save_ShouldDoesNotThrow() {
        final User user = new User();
        user.setUsername("User2");
        user.setPassword("password");
        user.setScore(0);
        assertDoesNotThrow(() -> userService.save(user));
    }

    @Test
    public void save_ShouldSave() {
        final User user = new User();
        user.setUsername("User2");
        user.setPassword("password");
        user.setScore(0);
        final String id = userService.save(user);
        assertTrue(userRepository.findById(id).isPresent());
    }

    @Test
    public void deleteById_ExistingId_ShouldDelete() {
        userService.deleteById(user.getId());
        assertFalse(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    public void deleteById_NotExistingId_ShouldDoesNotThrow() {
        assertDoesNotThrow(() -> userService.deleteById(""));
    }

}
