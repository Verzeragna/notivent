package ru.notivent.dao;

import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import ru.notivent.model.Contacts;

@Repository
public interface ContactsDAO {

    void save(@Param("entity") Contacts entity);

    List<Contacts> findByUserId(@Param("userId") UUID userId);

    void delete(@Param("entity") Contacts entity);

    int checkIfContactsExists(@Param("entity") Contacts entity);

}
