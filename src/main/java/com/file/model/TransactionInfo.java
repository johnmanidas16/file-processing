package com.file.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;

@Builder
@Entity
@Table(name = "transaction_info")
public class TransactionInfo {

	@Id
	private Long transactionId;
	
	@Column
	private String transactionSource;
	
	@Column
	private String transactionName;
}
