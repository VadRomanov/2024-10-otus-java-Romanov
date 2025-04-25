package ru.otus.service;

import org.springframework.stereotype.Service;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceAddress;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DBServicePhone;
import ru.otus.web.dto.ClientInfoResponseDto;
import ru.otus.web.dto.CreationRequestDto;
import ru.otus.web.dto.CreationResponseDto;
import ru.otus.web.mapper.ClientInfoMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientInfoServiceImpl implements ClientInfoService {
    private final DBServiceClient dbServiceClient;
    private final DBServiceAddress dbServiceAddress;
    private final DBServicePhone dbServicePhone;
    private final ClientInfoMapper clientInfoMapper;

    public ClientInfoServiceImpl(DBServiceClient dbServiceClient,
                                 DBServiceAddress dbServiceAddress, DBServicePhone dbServicePhone,
                                 ClientInfoMapper clientInfoMapper) {
        this.dbServiceClient = dbServiceClient;
        this.dbServiceAddress = dbServiceAddress;
        this.dbServicePhone = dbServicePhone;
        this.clientInfoMapper = clientInfoMapper;
    }

    @Override
    public CreationResponseDto saveClientInfo(CreationRequestDto creationRequestDto) {
        Address savedAddress = dbServiceAddress.saveAddress(
                new Address(null, creationRequestDto.address(), null));
        Client savedClient = dbServiceClient.saveClient(
                new Client(null, savedAddress.id(), creationRequestDto.name(), null));
        dbServicePhone.saveBatch(
                creationRequestDto.phones().stream()
                        .map(num -> new Phone(null, savedClient.id(), num))
                        .collect(Collectors.toSet()));

        return new CreationResponseDto(savedClient.id());
    }

    @Override
    public ClientInfoResponseDto getClientInfoById(long clientId) {
        Client client = dbServiceClient.getClient(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        Address address = dbServiceAddress.getAddress(client.addressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        return clientInfoMapper.toDto(client, address);
    }

    @Override
    public List<ClientInfoResponseDto> getAllClientInfo() {
        List<Client> client = dbServiceClient.findAll();
        return client.stream()
                .map(c -> clientInfoMapper.toDto(c, dbServiceAddress.getAddress(c.addressId()).get()))
                .toList();
    }
}
