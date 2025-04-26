package ru.otus.web.mapper;

import org.springframework.stereotype.Component;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.web.dto.ClientInfoDto;

import java.util.stream.Collectors;

@Component
public class ClientInfoMapper {

    public ClientInfoDto toDto(Client client, Address address) {
        return new ClientInfoDto(client.id(), client.name(), address.street(),
                client.phones().stream()
                        .map(Phone::number)
                        .collect(Collectors.toSet()));
    }

}
