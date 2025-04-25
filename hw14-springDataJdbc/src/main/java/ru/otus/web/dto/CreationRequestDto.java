package ru.otus.web.dto;

import java.util.List;

public record CreationRequestDto(String name, String address, List<String> phones) {
}
