package ru.test.data.manager.api.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.test.data.manager.api.entity.ClientEntity;
import ru.test.data.manager.api.entity.ProductEntity;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByClientId (long clientId);
}


