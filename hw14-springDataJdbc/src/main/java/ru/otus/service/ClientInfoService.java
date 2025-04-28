package ru.otus.service;

import ru.otus.web.dto.ClientInfoDto;
import ru.otus.web.dto.ClientCreationDto;

import java.util.List;

public interface ClientInfoService {
    ClientInfoDto saveClientInfo(ClientCreationDto clientCreationDto);

    ClientInfoDto getClientInfoById(long clientId);

    List<ClientInfoDto> getAllClientInfo();
}
