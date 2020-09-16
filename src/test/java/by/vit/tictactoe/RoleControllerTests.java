package by.vit.tictactoe;

import by.vit.tictactoe.entity.Role;
import by.vit.tictactoe.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class RoleControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    private final String url = "/roles";

    private final String urlWithId = url + "/%s";

    @BeforeEach
    public void init(final WebApplicationContext context) {
        roleRepository.deleteAll();

        final Role role = new Role();
        role.setName("ROLE1");
        this.role = roleRepository.save(role);

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    public void clean() {
        roleRepository.deleteAll();
    }

    @Test
    public void getAll_ShouldReturnOkStatus() throws Exception {
        mockMvc.perform(
                get(url).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void getAll_ShouldReturnNotEmpty() throws Exception {
        mockMvc.perform(
                get(url).accept(MediaType.APPLICATION_JSON)
        ).andDo(result -> {
            final Role[] roles = mapper.readValue(
                    result.getResponse().getContentAsString(),
                    Role[].class
            );

            assertTrue(roles.length > 0);
        });
    }

    @Test
    public void getById_ShouldReturnOkStatus() throws Exception {
        mockMvc.perform(
                get(String.format(urlWithId, role.getId())).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void getById_ExistingId_ShouldReturnNotNull() throws Exception {
        mockMvc.perform(
                get(String.format(urlWithId, role.getId())).accept(MediaType.APPLICATION_JSON)
        ).andDo(result -> {
            final Role role = mapper.readValue(
                    result.getResponse().getContentAsString(),
                    Role.class
            );

            assertNotNull(role);
        });
    }

    @Test
    public void getById_NotExistingId_ShouldReturnNull() throws Exception {
        mockMvc.perform(
                get(String.format(urlWithId, "SomeNotExistingId")).accept(MediaType.APPLICATION_JSON)
        ).andDo(result -> assertNull(result.getResponse().getContentType()));
    }

    @Test
    public void save_Valid_ShouldReturnCreatedStatus() throws Exception {
        final Role role = new Role();
        role.setName("ROLE2");

        mockMvc.perform(
                post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(role))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }

    @Test
    public void save_Valid_ShouldSave() throws Exception {
        final Role role = new Role();
        role.setName("ROLE2");

        mockMvc.perform(
                post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(role))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(result -> {
            final Optional<Role> savedRole = roleRepository.findById(
                    result.getResponse().getContentAsString()
            );

            assertTrue(savedRole.isPresent());
        });
    }

    @Test
    public void save_NullName_ShouldReturnBadRequestStatus() throws Exception {
        final Role role = new Role();

        mockMvc.perform(
                post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(role))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void save_BlankName_ShouldReturnBadRequestStatus() throws Exception {
        final Role role = new Role();
        role.setName("");

        mockMvc.perform(
                post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(role))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void save_NotUniqueName_ShouldReturnBadRequestStatus() throws Exception {
        final Role role = new Role();
        role.setName(this.role.getName());

        mockMvc.perform(
                post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(role))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void save_WithExistingId_ShouldUpdate() throws Exception {
        final Role role = new Role();
        role.setId(this.role.getId());
        role.setName("ROLE2");

        mockMvc.perform(
                post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(role))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(result -> {
            final Optional<Role> updatedRole = roleRepository.findById(this.role.getId());

            assertTrue(updatedRole.isPresent());
            assertEquals(role.getName(), updatedRole.get().getName());
        });
    }

    @Test
    public void save_WithNotExistingId_ShouldCreate() throws Exception {
        final Role role = new Role();
        role.setId("SomeNotExistingId");
        role.setName("ROLE2");

        mockMvc.perform(
                post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(role))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(result -> {
            final Optional<Role> createdRole = roleRepository.findById(
                    result.getResponse().getContentAsString()
            );

            assertTrue(createdRole.isPresent());
        });
    }

    @Test
    public void deleteById_ExistingId_ShouldReturnOkStatus() throws Exception {
        mockMvc.perform(
                delete(String.format(urlWithId, role.getId())).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void deleteById_NotExistingId_ShouldReturnOkStatus() throws Exception {
        mockMvc.perform(
                delete(String.format(urlWithId, "SomeNotExistingId")).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void deleteById_ExistingId_ShouldDelete() throws Exception {
        mockMvc.perform(
                delete(String.format(urlWithId, role.getId())).accept(MediaType.APPLICATION_JSON)
        ).andDo(result -> {
            final Optional<Role> role = roleRepository.findById(this.role.getId());

            assertFalse(role.isPresent());
        });
    }

}
