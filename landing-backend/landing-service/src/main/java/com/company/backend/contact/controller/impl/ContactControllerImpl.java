package com.company.backend.contact.controller.impl;

import com.company.backend.contact.controller.ContactController;
import com.company.backend.contact.dto.ContactDto;
import com.company.backend.contact.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Primary
@RestController
@CrossOrigin("*")
@RequestMapping(path = "api")
public class ContactControllerImpl implements ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactControllerImpl(ContactService contactService)
    {
        this.contactService = contactService;
    }


    @Override
    public ResponseEntity<ContactDto> save(ContactDto contactDto) {
        return new ResponseEntity<>(this.contactService.save(contactDto) , HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<ContactDto>> getAllContact() {
        return ResponseEntity.ok(this.contactService.getAllContact());
    }


}
