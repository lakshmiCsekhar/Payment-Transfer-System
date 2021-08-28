package com.org.ibpts.repository;

import com.org.ibpts.model.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface AccountInformationRepository extends CrudRepository<Account, Integer> {

    public static final String GET_ACCOUNT_BALANCE = "select balance from Account acc where acc.id = ?1 ";
    public static final String UPDATE_ACCOUNT_BALANCE = "update Account acc set acc.balance = ?1 where acc.id = ?2";

    Optional<Account> findByAccountNumber(BigInteger accountNumber);

    @Modifying
    @Transactional
    @Query(value = UPDATE_ACCOUNT_BALANCE, nativeQuery = true)
    public void updateBalanceForAccount(BigDecimal balance, String id);


    @Query(value = GET_ACCOUNT_BALANCE, nativeQuery = true)
    public BigDecimal getAccountBalance(String id);

    Optional<Account> findById(String creditorId);
}
