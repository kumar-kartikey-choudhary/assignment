package com.company.backend.client.service;

import com.company.backend.client.dto.ClientDto;

import java.util.List;

public interface ClientService {


    ClientDto create(ClientDto clientDto);

    List<ClientDto> getAllClients();

    ClientDto update(ClientDto clientDto, String clientName);

    void delete(String clientName);
}
