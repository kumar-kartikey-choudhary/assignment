package com.company.backend.contact.service.impl;

import com.company.backend.contact.dto.ContactDto;
import com.company.backend.contact.model.Contact;
import com.company.backend.contact.repository.ContactRepository;
import com.company.backend.contact.service.ContactService;
import com.company.backend.utilty.MapperUtility;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
@Slf4j
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository)
    {
        this.contactRepository = contactRepository;
    }


    @Override
    @Transactional
    public ContactDto save(ContactDto contactDto) {
        log.info("Inside @class ContactServiceImpl @method save @Param : {}", contactDto);
        try {
            Contact contact = MapperUtility.sourceToTarget(contactDto, Contact.class);
            log.info("Saving contact to database..");
            contact = this.contactRepository.saveAndFlush(contact);
            log.info("Contact info saved to db and mapping to dto ");
            return MapperUtility.sourceToTarget(contact, ContactDto.class);
        }catch (Exception e)
        {
            throw new RuntimeException("Error to map entity to dto");
        }
    }

    @Override
    public List<ContactDto> getAllContact() {
        log.info("Inside @class ContactServiceImpl @method getAllContact");
        try {
            List<Contact> all = this.contactRepository.findAll();
            return all.stream().map(contact -> {
                try {
                    return MapperUtility.sourceToTarget(contact, ContactDto.class);
                } catch (Exception e) {
                    throw new RuntimeException("Error while mapping...");
                }
            }).toList();
        } catch (Exception e) {
            throw new RuntimeException("Can not get all the contact information");
        }
    }

}
