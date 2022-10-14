package ru.test.data.manager.api.repository.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.test.data.manager.api.entity.ClientEntity;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    ClientEntity findByMobilePhone (String mobile_phone);
}


