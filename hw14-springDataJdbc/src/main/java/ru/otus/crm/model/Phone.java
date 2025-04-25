package ru.otus.crm.model;

import jakarta.annotation.Nonnull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public record Phone(
        @Id
        Long id,

        @Nonnull
        Long client_id,

        @Nonnull
        String number) {
}