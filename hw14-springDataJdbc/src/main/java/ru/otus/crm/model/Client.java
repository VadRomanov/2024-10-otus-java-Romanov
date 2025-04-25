package ru.otus.crm.model;

import jakarta.annotation.Nonnull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Table
public record Client(

        @Id
        Long id,

        Long addressId,

        @Nonnull
        String name,

        @MappedCollection(idColumn = "client_id")
        Set<Phone> phones) {
}
