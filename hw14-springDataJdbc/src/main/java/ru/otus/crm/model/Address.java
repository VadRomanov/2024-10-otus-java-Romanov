package ru.otus.crm.model;

import jakarta.annotation.Nonnull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Table
public record Address(
        @Id
        Long id,

        @Nonnull
        String street,

        @MappedCollection(idColumn = "address_id")
        Set<Client> client) {
}
