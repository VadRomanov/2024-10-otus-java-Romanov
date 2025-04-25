package ru.otus.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.service.ClientInfoService;
import ru.otus.web.dto.ClientInfoResponseDto;

import java.util.List;

@Controller
public class ClientController {
    private final ClientInfoService clientInfoService;

    public ClientController(ClientInfoService clientInfoService) {
        this.clientInfoService = clientInfoService;
    }

    @GetMapping({"/clients"})
    public String clientsListView(Model model) {
        List<ClientInfoResponseDto> clients = clientInfoService.getAllClientInfo();
        model.addAttribute("clients", clients);
        return "clients";
    }

}
