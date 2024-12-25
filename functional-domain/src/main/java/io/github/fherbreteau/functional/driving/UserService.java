package io.github.fherbreteau.functional.driving;

import io.github.fherbreteau.functional.domain.entities.Group;
import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;

public interface UserService {

    Output<User> findUserByName(String name);

    Output<Group> findGroupByName(String name);

    <T> Output<T> processCommand(UserCommandType type, User currentUser, UserInput userInput);
}
