package ru.notivent.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.notivent.dto.ContactsAddDto;
import ru.notivent.dto.ContactsDto;
import ru.notivent.dto.SearchContactDto;
import ru.notivent.service.ContactsService;

import java.util.List;
import java.util.UUID;

import static ru.notivent.util.HttpUtil.X_AUTH;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactsController {

    final ContactsService contactsService;

    /**
     * Add contact to user contacts
     * @param dto {@link ContactsAddDto}
     * @return User contacts {@link ContactsDto}
     */
    @PostMapping
    public ResponseEntity<Void> addUserToContacts(@RequestBody @Valid ContactsAddDto dto) {
        return contactsService.addUserToContacts(dto);
    }

    /**
     * Get all user contacts
     * @param userId User id
     * @return User contacts {@link ContactsDto}
     */
    @GetMapping
    public ResponseEntity<List<ContactsDto>> getUserContacts(@RequestHeader(X_AUTH) UUID userId) {
        return contactsService.getUserContacts(userId);
    }

    /**
     * Remove contact from user contacts
     * @param userId User id
     * @param contactId Contact is
     * @return User contacts {@link ContactsDto}
     */
    @PostMapping("{id}")
    public ResponseEntity<List<ContactsDto>> removeUserFromContacts(@RequestHeader(X_AUTH) UUID userId, @PathVariable("id") UUID contactId) {
        return contactsService.removeUserFromContacts(userId, contactId);
    }

    @PostMapping("search")
    public ResponseEntity<List<ContactsDto>> searchContact(@RequestHeader(X_AUTH) UUID userId, @RequestBody @Valid SearchContactDto dto) {
        return contactsService.searchContact(userId, dto);
    }
}
