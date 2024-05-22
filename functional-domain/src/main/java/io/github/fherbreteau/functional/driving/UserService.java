package io.github.fherbreteau.functional.driving;

import io.github.fherbreteau.functional.domain.entities.Output;
import io.github.fherbreteau.functional.domain.entities.User;
import io.github.fherbreteau.functional.domain.entities.UserCommandType;
import io.github.fherbreteau.functional.domain.entities.UserInput;

public interface UserService {

    Output findUserByName(String name);

    Output findGroupByName(String name);

    Output processCommand(UserCommandType type, User currentUser, UserInput userInput);
}
