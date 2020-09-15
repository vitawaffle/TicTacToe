package by.vit.tictactoe.service;

import java.util.Optional;

public interface AppService<T, ID> {

    Optional<T> getById(ID id);

    ID save(T t);

    void deleteById(ID id);

}
