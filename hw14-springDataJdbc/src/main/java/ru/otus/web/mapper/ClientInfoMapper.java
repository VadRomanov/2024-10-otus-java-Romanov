package ru.otus.web.mapper;

import org.springframework.stereotype.Component;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.web.dto.ClientInfoResponseDto;

import java.util.stream.Collectors;

@Component
public class ClientInfoMapper {

    public ClientInfoResponseDto toDto(Client client, Address address) {
        return new ClientInfoResponseDto(client.id(), client.name(), address.street(),
                client.phones().stream()
                        .map(Phone::number)
                        .collect(Collectors.toSet()));
    }

}
