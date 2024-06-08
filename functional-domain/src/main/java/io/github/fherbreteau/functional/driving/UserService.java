package io.github.fherbreteau.functional.driving;

import io.github.fherbreteau.functional.domain.entities.*;

public interface UserService {

    Output<User> findUserByName(String name);

    Output<Group> findGroupByName(String name);

    <T> Output<T> processCommand(UserCommandType type, User currentUser, UserInput userInput);
}
