package ru.otus.web.dto;

import java.util.List;

public record ClientCreationDto(String name, String address, List<String> phones) {
}
