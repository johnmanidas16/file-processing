package com.file.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.file.dao.FileRepository;
import com.file.model.TransactionInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileProcessorImpl implements FileProcessor {

	private static final int CHUNK_SIZE = 8192;
	private static final int BATCH_SIZE = 1000;

	private final ExecutorService executorService = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private final FileRepository fileRepository;

	/**
	 * @param fileLocation : String
	 */
	@Override
	public void processFile(String fileLocation) {
		try (BufferedReader reader = new BufferedReader(new FileReader(fileLocation), CHUNK_SIZE)) {
			Stream<String> lines = reader.lines();
			processInBatches(lines);
		} catch (IOException e) {
			log.error("Error reading file: {}", e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param lines
	 */
	private void processInBatches(Stream<String> lines) {
		String[] batch = new String[BATCH_SIZE];
		int count = 0;

		for (String line : (Iterable<String>) lines::iterator) {
			batch[count++] = line;

			if (count == BATCH_SIZE) {
				submitBatchForProcessing(batch, count);
				count = 0;
				batch = new String[BATCH_SIZE];
			}
		}

		if (count > 0) {
			submitBatchForProcessing(batch, count);
		}
		executorService.shutdown();
	}

	/**
	 * 
	 * @param batch
	 * @param count
	 */
	private void submitBatchForProcessing(String[] batch, int count) {
		String[] finalBatch = new String[count];
		System.arraycopy(batch, 0, finalBatch, 0, count);

		executorService.submit(() -> {
			try {
				fileRepository.saveAll(parsebatch(finalBatch).toList());
			} catch (Exception e) {
				log.error("Error processing batch: {}", e.getMessage(), e);
			}
		});

	}

	/**
	 * 
	 * @param batch
	 * @return
	 */
	private Stream<TransactionInfo> parsebatch(String[] batch) {
		return Stream.of(batch).map(this::parseRecord).filter(Objects::nonNull);
	}

	/**
	 * 
	 * @param line
	 * @return TransactionInfo
	 */
	private TransactionInfo parseRecord(String line) {
		try {
			String[] parts = line.split(",");
			return TransactionInfo.builder().transactionName(parts[1]).transactionSource(parts[2]).build();
		} catch (Exception e) {
			log.warn("Failed to parse record: {}", line, e);
			return null;
		}
	}

}
