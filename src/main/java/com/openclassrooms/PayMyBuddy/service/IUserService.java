package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.ContactDto;
import com.openclassrooms.PayMyBuddy.model.EmailAddress;
import com.openclassrooms.PayMyBuddy.model.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> getUser(Long id);

    Iterable<User> getUsers();

    void deleteUser(Long id);

}
