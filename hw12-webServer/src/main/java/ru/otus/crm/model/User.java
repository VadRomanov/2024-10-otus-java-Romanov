package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {

    private final long id;
    private final String name;
    private final String login;
    private final String password;
}
