package com.company.backend.contact.service;

import com.company.backend.contact.dto.ContactDto;

import java.util.List;

public interface ContactService {

    ContactDto save(ContactDto contactDto);

    List<ContactDto> getAllContact();
}
