package com.minibanking.banking.infrastructure.persistence.repository;

import com.minibanking.banking.infrastructure.persistence.entity.AccountTransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataAccountTransactionRepository extends JpaRepository<AccountTransactionEntity, UUID> {

    Page<AccountTransactionEntity> findByAccountIdOrderByCreatedAtDesc(
            UUID accountId,
            Pageable pageable
    );

    long countByAccountId(UUID accountId);

}
