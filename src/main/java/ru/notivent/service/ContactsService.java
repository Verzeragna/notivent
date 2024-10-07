package ru.notivent.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.notivent.dao.ContactsDAO;
import ru.notivent.dto.ContactsAddDto;
import ru.notivent.dto.ContactsDto;
import ru.notivent.dto.SearchContactDto;
import ru.notivent.mapper.ContactsMapper;
import ru.notivent.model.Contacts;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactsService {

  final UserService userService;
  final SubscriptionService subscriptionService;
  final ContactsMapper contactsMapper;

  @Delegate final ContactsDAO contactsDAO;

  public ResponseEntity<List<ContactsDto>> getUserContacts(UUID userId) {
    if (!subscriptionService.isUserHasActiveSubscription(userId)) {
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
    var user = userService.findById(userId);
    if (user.isEmpty()) {
      log.error("User {} not found.", userId);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    var contacts = contactsDAO.findByUserId(userId);
    var userContacts =
        contacts.stream()
            .map(Contacts::getMainUserId)
            .filter(uuid -> !Objects.equals(uuid, userId))
            .collect(Collectors.toSet());
    userContacts.addAll(
        contacts.stream()
            .map(Contacts::getSubUserId)
            .filter(uuid -> !Objects.equals(uuid, userId))
            .collect(Collectors.toSet()));
    var users = userService.findByIds(userContacts);
    var userContactDtos =
        users.stream()
            .map(contactsMapper::toDto)
            .sorted(Comparator.comparing(ContactsDto::userName))
            .toList();
    return ResponseEntity.ok(userContactDtos);
  }

  public ResponseEntity<Void> addUserToContacts(ContactsAddDto dto) {
      if (!subscriptionService.isUserHasActiveSubscription(dto.mainUserId())) {
          return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
      }
    // check main user
    var user = userService.findById(dto.mainUserId());
    if (user.isEmpty()) {
      log.error("Main user {} not found.", dto.mainUserId());
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // check sub user
    user = userService.findById(dto.subUserId());
    if (user.isEmpty()) {
      log.error("Sub user {} not found.", dto.subUserId());
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    var contact = contactsMapper.toModel(dto);
    if (contactsDAO.checkIfContactsExists(contact) > 0) {
      log.error(
          "Contact already exists. Main user {}, sub user {}.", dto.mainUserId(), dto.subUserId());
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    contactsDAO.save(contact);
    return ResponseEntity.ok().build();
  }

  public ResponseEntity<List<ContactsDto>> removeUserFromContacts(UUID mainUserId, UUID contactId) {
      if (!subscriptionService.isUserHasActiveSubscription(mainUserId)) {
          return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
      }
    var user = userService.findById(contactId);
    if (user.isEmpty()) {
      log.error("Contact user {} not found.", contactId);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    var contact = new Contacts(mainUserId, contactId, null);
    contactsDAO.delete(contact);
    return getUserContacts(mainUserId);
  }

  public ResponseEntity<List<ContactsDto>> searchContact(UUID userId, SearchContactDto dto) {
      if (!subscriptionService.isUserHasActiveSubscription(userId)) {
          return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
      }
    var userOpt = userService.findByUserName(dto.value());
    if (userOpt.isPresent()) {
      val user = userOpt.get();
      if (contactsDAO.checkIfContactsExists(contactsMapper.toModelFromUser(user, userId)) > 0) {
        return ResponseEntity.ok(Collections.emptyList());
      }
      return ResponseEntity.ok(List.of(contactsMapper.toDto(user)));
    }
    return ResponseEntity.ok(Collections.emptyList());
  }
}
