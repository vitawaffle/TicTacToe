package by.vit.tictactoe;

import by.vit.tictactoe.entity.Role;
import by.vit.tictactoe.repository.RoleRepository;
import by.vit.tictactoe.service.RoleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RoleServiceTests {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    public void init() {
        roleRepository.deleteAll();
        final Role role = new Role();
        role.setName("ROLE1");
        this.role = roleRepository.save(role);
    }

    @AfterEach
    public void clean() {
        roleRepository.deleteAll();
    }

    @Test
    public void getAll_ShouldReturnNotEmpty() {
        assertFalse(roleService.getAll().isEmpty());
    }

    @Test
    public void getById_ExistingId_ShouldReturnRole() {
        assertTrue(roleService.getById(role.getId()).isPresent());
    }

    @Test
    public void getById_NotExistingId_ShouldReturnNull() {
        assertFalse(roleService.getById("").isPresent());
    }

    @Test
    public void save_ShouldDoesNotThrow() {
        final Role role = new Role();
        role.setName("ROLE2");
        assertDoesNotThrow(() -> roleService.save(role));
    }

    @Test
    public void save_ShouldSave() {
        final Role role = new Role();
        role.setName("ROLE2");
        final String id = roleService.save(role);
        assertTrue(roleRepository.findById(id).isPresent());
    }

    @Test
    public void deleteById_ExistingId_ShouldDelete() {
        roleService.deleteById(role.getId());
        assertFalse(roleRepository.findById(role.getId()).isPresent());
    }

    @Test
    public void deleteById_NotExistingId_ShouldDoesNotThrow() {
        assertDoesNotThrow(() -> roleService.deleteById(""));
    }

}
