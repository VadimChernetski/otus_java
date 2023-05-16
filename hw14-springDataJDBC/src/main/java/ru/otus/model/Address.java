package ru.otus.model;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "address")
public class Address {

    @Id
    private Long id;

    @Nonnull
    private final String street;

    @PersistenceCreator
    public Address(String street, Long id) {
        this.id = id;
        this.street = street;
    }

    public Address(String street) {
        this.street = street;
    }
}
