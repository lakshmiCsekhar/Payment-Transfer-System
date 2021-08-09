package com.org.ibpts.repository;

import com.org.ibpts.model.Transactions;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionsRepository extends CrudRepository<Transactions, String> {

    public List<Transactions> findByDebtorId(String debtorId, Pageable pageable);

    Transactions findByReference(String reference);
}
