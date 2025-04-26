package ru.otus.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.service.ClientInfoService;
import ru.otus.web.dto.ClientInfoDto;
import ru.otus.web.dto.ClientCreationDto;
import ru.otus.web.dto.ClientCreationRespDto;

import java.util.List;

@RestController
public class ClientRestController {

    private final ClientInfoService clientInfoService;

    public ClientRestController(ClientInfoService clientInfoService) {
        this.clientInfoService = clientInfoService;
    }

    @GetMapping("/api/client/{id}")
    public ResponseEntity<ClientInfoDto> getClientById(@PathVariable(name = "id") long id) {
        ClientInfoDto clientInfoById = clientInfoService.getClientInfoById(id);
        return ResponseEntity.ok(clientInfoById);
    }

    @GetMapping("/api/client")
    public ResponseEntity<List<ClientInfoDto>> getAllClients() {
        List<ClientInfoDto> clientInfoDtos = clientInfoService.getAllClientInfo();
        return ResponseEntity.ok(clientInfoDtos);
    }

    @PostMapping("/api/client")
    public ResponseEntity<ClientCreationRespDto> saveClient(@RequestBody ClientCreationDto requestDto) {
        return ResponseEntity.ok(new ClientCreationRespDto(clientInfoService.saveClientInfo(requestDto)));
    }
}
