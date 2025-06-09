package fr.uge.yams.controllers;

import fr.uge.yams.models.User;

public interface UserController {
    void setGameController(GameController gameController);

    void setUser(User user);
}
