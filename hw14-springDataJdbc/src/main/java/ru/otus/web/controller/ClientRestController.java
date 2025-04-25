package ru.otus.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.service.ClientInfoService;
import ru.otus.web.dto.ClientInfoResponseDto;
import ru.otus.web.dto.CreationRequestDto;
import ru.otus.web.dto.CreationResponseDto;

import java.util.List;

@RestController
public class ClientRestController {

    private final ClientInfoService clientInfoService;

    public ClientRestController(ClientInfoService clientInfoService) {
        this.clientInfoService = clientInfoService;
    }

    @GetMapping("/api/client/{id}")
    public ResponseEntity<ClientInfoResponseDto> getClientById(@PathVariable(name = "id") long id) {
        ClientInfoResponseDto clientInfoById = clientInfoService.getClientInfoById(id);
        return ResponseEntity.ok(clientInfoById);
    }

    @GetMapping("/api/client")
    public ResponseEntity<List<ClientInfoResponseDto>> getAllClients() {
        List<ClientInfoResponseDto> clientInfoResponseDtos = clientInfoService.getAllClientInfo();
        return ResponseEntity.ok(clientInfoResponseDtos);
    }

    @PostMapping("/api/client")
    public ResponseEntity<CreationResponseDto> saveClient(@RequestBody CreationRequestDto requestDto) {
        return ResponseEntity.ok(clientInfoService.saveClientInfo(requestDto));
    }
}
