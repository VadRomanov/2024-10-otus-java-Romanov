package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.crm.model.Phone;
import ru.otus.crm.repository.PhoneRepository;
import ru.otus.sessionmanager.TransactionManager;

import java.util.Set;

@Service
public class DbServicePhoneImpl implements DBServicePhone {
    private static final Logger log = LoggerFactory.getLogger(DbServicePhoneImpl.class);

    private final TransactionManager transactionManager;
    private final PhoneRepository phoneRepository;

    public DbServicePhoneImpl(TransactionManager transactionManager, PhoneRepository phoneRepository) {
        this.transactionManager = transactionManager;
        this.phoneRepository = phoneRepository;
    }

    @Override
    public void saveBatch(Set<Phone> phone) {
        transactionManager.doInTransaction(() -> {
            var savedPhones = phoneRepository.saveAll(phone);
            log.info("saved phones: {}", savedPhones);
            return savedPhones;
        });
    }
}
