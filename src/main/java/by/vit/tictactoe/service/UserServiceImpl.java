package by.vit.tictactoe.service;

import by.vit.tictactoe.entity.Role;
import by.vit.tictactoe.entity.User;
import by.vit.tictactoe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<User> getPaginated(final Pageable pageable) {
        final int pageNumber = pageable.getPageNumber();
        final int pageSize = pageable.getPageSize();
        final int startItem = pageNumber * pageSize;
        final List<User> users = userRepository.findAll();
        List<User> page = new ArrayList<>();
        if (startItem >= 0 && startItem < users.size()) {
            page = users.subList(startItem, Math.min(startItem + pageSize, users.size()));
        }
        return new PageImpl<>(page, PageRequest.of(pageNumber, pageSize), users.size());
    }

    @Override
    public Optional<User> getById(final String id) {
        return userRepository.findById(id);
    }

    @Override
    public String save(final User user) {
        return userRepository.save(user).getId();
    }

    @Override
    public void deleteById(final String id) {
        userRepository.deleteById(id);
    }

    @Override
    public String create(final User user) {
        final Role role = new Role();
        role.setName("USER");

        user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new ArrayList<>(Collections.singletonList(role)));
        user.setScore(0);

        return save(user);
    }

}
