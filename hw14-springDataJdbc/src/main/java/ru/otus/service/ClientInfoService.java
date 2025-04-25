package ru.otus.service;

import ru.otus.web.dto.ClientInfoResponseDto;
import ru.otus.web.dto.CreationRequestDto;
import ru.otus.web.dto.CreationResponseDto;

import java.util.List;

public interface ClientInfoService {
    CreationResponseDto saveClientInfo(CreationRequestDto creationRequestDto);

    ClientInfoResponseDto getClientInfoById(long clientId);

    List<ClientInfoResponseDto> getAllClientInfo();
}
