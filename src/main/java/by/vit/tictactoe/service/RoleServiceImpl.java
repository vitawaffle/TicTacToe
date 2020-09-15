package by.vit.tictactoe.service;

import by.vit.tictactoe.entity.Role;
import by.vit.tictactoe.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> getById(final String id) {
        return roleRepository.findById(id);
    }

    @Override
    public String save(final Role role) {
        return roleRepository.save(role).getId();
    }

    @Override
    public void deleteById(final String id) {
        roleRepository.deleteById(id);
    }

}
