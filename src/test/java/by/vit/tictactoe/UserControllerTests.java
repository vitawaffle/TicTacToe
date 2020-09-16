package by.vit.tictactoe;

import by.vit.tictactoe.entity.Role;
import by.vit.tictactoe.entity.User;
import by.vit.tictactoe.repository.UserRepository;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository userRepository;

    private User user;

    private final String url = "/users";

    private final String urlWithId = url + "/%s";

    @BeforeEach
    public void init(final WebApplicationContext context) {
        userRepository.deleteAll();

        final Role role = new Role();
        role.setName("ROLE1");

        final User user = new User();
        user.setUsername("User1");
        user.setPassword("password");
        user.setRoles(new ArrayList<>(Collections.singletonList(role)));
        user.setScore(0);
        this.user = userRepository.save(user);

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    public void clean() {
        userRepository.deleteAll();
    }

    @Test
    public void getPaginated_ShouldReturnOkStatus() throws Exception {
        mockMvc.perform(
                get(url)
                        .param("page", "0")
                        .param("size", "30")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void getPaginated_InRange_ShouldReturnNotEmpty() throws Exception {
        mockMvc.perform(
                get(url)
                        .param("page", "0")
                        .param("size", "30")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(result -> {
            final User[] users = mapper.readValue(
                    result.getResponse().getContentAsString(),
                    User[].class
            );

            assertTrue(users.length > 0);
        });
    }

    @Test
    public void getPaginated_OutOfRange_ShouldReturnEmpty() throws Exception {
        mockMvc.perform(
                get(url)
                        .param("page", "100")
                        .param("size", "100")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(result -> {
            final User[] users = mapper.readValue(
                    result.getResponse().getContentAsString(),
                    User[].class
            );

            assertEquals(users.length, 0);
        });
    }

    @Test
    public void getPaginated_MissingParams_ShouldReturnFirstPage() throws Exception {
        mockMvc.perform(
                get(url).accept(MediaType.APPLICATION_JSON)
        ).andDo(result -> {
            final User[] users = mapper.readValue(
                    result.getResponse().getContentAsString(),
                    User[].class
            );

            assertTrue(users.length > 0);
        });
    }

    @Test
    public void getById_ShouldReturnOkStatus() throws Exception {
        mockMvc.perform(
                get(String.format(urlWithId, user.getId())).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void getById_ExistingId_ShouldReturnNotNull() throws Exception {
        mockMvc.perform(
                get(String.format(urlWithId, user.getId())).accept(MediaType.APPLICATION_JSON)
        ).andDo(result -> {
            final User user = mapper.readValue(
                    result.getResponse().getContentAsString(),
                    User.class
            );

            assertNotNull(user);
        });
    }

    @Test
    public void getById_NotExistingId_ShouldReturnNull() throws Exception {
        mockMvc.perform(
                get(String.format(urlWithId, "SomeNotExistingId")).accept(MediaType.APPLICATION_JSON)
        ).andDo(result -> assertNull(result.getResponse().getContentType()));
    }

    @Test
    public void getById_ShouldHidePassword() throws Exception {
        mockMvc.perform(
                get(String.format(urlWithId, user.getId())).accept(MediaType.APPLICATION_JSON)
        ).andDo(result -> {
            final User user = mapper.readValue(
                    result.getResponse().getContentAsString(),
                    User.class
            );

            assertNull(user.getPassword());
        });
    }

    @Test
    public void register_Valid_ShouldReturnCreatedStatus() throws Exception {
        final User user = new User();
        user.setUsername("User2");
        user.setPassword("password");

        mockMvc.perform(
                post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }

    @Test
    public void register_Valid_ShouldCreate() throws Exception {
        final User user = new User();
        user.setUsername("User2");
        user.setPassword("password");

        mockMvc.perform(
                post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(result -> {
            final Optional<User> createdUser = userRepository.findById(
                    result.getResponse().getContentAsString()
            );

            assertTrue(createdUser.isPresent());
        });
    }

    @Test
    public void register_NullUsername_ShouldReturnBadRequestStatus() throws Exception {
        final User user = new User();
        user.setPassword("password");

        mockMvc.perform(
                post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void register_BlankUsername_ShouldReturnBadRequestStatus() throws Exception {
        final User user = new User();
        user.setUsername("");
        user.setPassword("password");

        mockMvc.perform(
                post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void register_NotUniqueUsername_ShouldReturnBadRequestStatus() throws Exception {
        final User user = new User();
        user.setUsername(this.user.getUsername());
        user.setPassword("password");

        mockMvc.perform(
                post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void register_NullPassword_ShouldReturnBadRequestStatus() throws Exception {
        final User user = new User();
        user.setUsername("User2");

        mockMvc.perform(
                post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void register_BlankPassword_ShouldReturnBadRequestStatus() throws Exception {
        final User user = new User();
        user.setUsername("User2");
        user.setPassword("");

        mockMvc.perform(
                post(url)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void deleteById_ShouldReturnOkStatus() throws Exception {
        mockMvc.perform(
                delete(String.format(urlWithId, user.getId())).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void deleteById_ExistingId_ShouldDelete() throws Exception {
        mockMvc.perform(
                delete(String.format(urlWithId, user.getUsername())).accept(MediaType.APPLICATION_JSON)
        ).andDo(result -> {
            final Optional<User> user = userRepository.findById(
                    result.getResponse().getContentAsString()
            );

            assertFalse(user.isPresent());
        });
    }

    @Test
    public void deleteById_NotExistingId_ShouldReturnOkStatus() throws Exception {
        mockMvc.perform(
                delete(String.format(urlWithId, "SomeNotExistingId")).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

}
