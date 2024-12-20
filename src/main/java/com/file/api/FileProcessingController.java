package com.file.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.file.dto.ProcessingStatus;
import com.file.service.FileProcessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/api/file")
@RequiredArgsConstructor
public class FileProcessingController {

	private final FileProcessor fileProcessor;

	/**
	 * This method is to process the big in efficient way without memory issues.
	 * @param fileLocation
	 * @return
	 */
	@PostMapping("/proocess")
	public ResponseEntity<?> processFile(@PathVariable("fileLocation") String fileLocation) {
		try {
			fileProcessor.processFile(fileLocation);
			return ResponseEntity.ok("File processing started successfully");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Failed to process file: " + e.getMessage());
		}
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping("/status")
	public ResponseEntity<ProcessingStatus> getStatus() {
		// Implement status tracking
		return ResponseEntity.ok(new ProcessingStatus());
	}
}
