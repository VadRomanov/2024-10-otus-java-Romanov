package ru.otus.crm.service;

import ru.otus.crm.model.Phone;

import java.util.Set;

public interface DBServicePhone {

    void saveBatch(Set<Phone> phone);
}
