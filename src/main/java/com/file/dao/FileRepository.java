package com.file.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.file.model.TransactionInfo;

public interface FileRepository extends JpaRepository<TransactionInfo, Long>{

}
