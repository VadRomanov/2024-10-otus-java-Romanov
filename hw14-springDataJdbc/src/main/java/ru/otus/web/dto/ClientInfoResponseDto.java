package ru.otus.web.dto;

import java.util.Set;

public record ClientInfoResponseDto(long id, String name, String address, Set<String> phones) {
}
