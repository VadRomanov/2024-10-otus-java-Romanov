package ru.otus.service;

import org.springframework.stereotype.Service;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceAddress;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DBServicePhone;
import ru.otus.sessionmanager.TransactionManager;
import ru.otus.web.dto.ClientInfoDto;
import ru.otus.web.dto.ClientCreationDto;
import ru.otus.web.mapper.ClientInfoMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientInfoServiceImpl implements ClientInfoService {
    private final DBServiceClient dbServiceClient;
    private final DBServiceAddress dbServiceAddress;
    private final DBServicePhone dbServicePhone;
    private final ClientInfoMapper clientInfoMapper;
    private final TransactionManager transactionManager;

    public ClientInfoServiceImpl(DBServiceClient dbServiceClient, DBServiceAddress dbServiceAddress,
                                 DBServicePhone dbServicePhone, ClientInfoMapper clientInfoMapper,
                                 TransactionManager transactionManager) {
        this.dbServiceClient = dbServiceClient;
        this.dbServiceAddress = dbServiceAddress;
        this.dbServicePhone = dbServicePhone;
        this.clientInfoMapper = clientInfoMapper;
        this.transactionManager = transactionManager;
    }

    @Override
    public ClientInfoDto saveClientInfo(ClientCreationDto clientCreationDto) {
        return transactionManager.doInTransaction(() -> {
            Address savedAddress = dbServiceAddress.saveAddress(
                    new Address(null, clientCreationDto.address(), null));
            Client savedClient = dbServiceClient.saveClient(
                    new Client(null, savedAddress.id(), clientCreationDto.name(), null));
            dbServicePhone.saveBatch(
                    clientCreationDto.phones().stream()
                            .map(num -> new Phone(null, savedClient.id(), num))
                            .collect(Collectors.toSet()));
            return new ClientInfoDto(savedClient.id(), null, null, null);
        });
    }

    @Override
    public ClientInfoDto getClientInfoById(long clientId) {
        return transactionManager.doInTransaction(() -> {
            Client client = dbServiceClient.getClient(clientId)
                    .orElseThrow(() -> new RuntimeException("Client not found"));
            Address address = dbServiceAddress.getAddress(client.addressId())
                    .orElseThrow(() -> new RuntimeException("Address not found"));

            return clientInfoMapper.toDto(client, address);
        });
    }

    @Override
    public List<ClientInfoDto> getAllClientInfo() {
        return transactionManager.doInTransaction(() -> {
            List<Client> client = dbServiceClient.findAll();
            return client.stream()
                    .map(c -> clientInfoMapper.toDto(c, dbServiceAddress.getAddress(c.addressId()).get()))
                    .toList();
        });
    }
}
