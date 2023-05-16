package ru.otus.model;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "phone")
public class Phone {

    @Id
    private Long id;

    @Nonnull
    private final String number;

    private Long clientId;

    @PersistenceCreator
    public Phone(Long id, String number, Long clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

    public Phone(String number) {
        this.number = number;
    }
}
