package ru.otus.crm.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.otus.crm.model.Phone;

public interface PhoneRepository extends ListCrudRepository<Phone, Long> {

}
