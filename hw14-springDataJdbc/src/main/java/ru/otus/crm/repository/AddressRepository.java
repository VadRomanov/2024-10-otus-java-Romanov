package ru.otus.crm.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.otus.crm.model.Address;

public interface AddressRepository extends ListCrudRepository<Address, Long> {
}
