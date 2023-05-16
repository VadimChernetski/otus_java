package ru.otus.model;


import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    private Long id;

    @Nonnull
    private final String name;

    @MappedCollection(idColumn = "id")
    private final Address address;

    @Nonnull
    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phones;

    @PersistenceCreator
    public Client(Long id, String name, Address address, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public Client(String name, Address address, Set<Phone> phones) {
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
