package ru.otus.web.dto;

import java.util.Set;

public record ClientInfoDto(long id, String name, String address, Set<String> phones) {
}
