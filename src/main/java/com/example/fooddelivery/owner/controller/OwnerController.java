package com.example.fooddelivery.owner.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fooddelivery.owner.dto.OwnerJoinReqDto;
import com.example.fooddelivery.owner.service.OwnerService;

@RestController
@RequestMapping("api/v1/owners")
public class OwnerController {

	private final OwnerService ownerService;

	public OwnerController(OwnerService ownerService) {
		this.ownerService = ownerService;
	}

	@PostMapping()
	public ResponseEntity<Void> join(@RequestBody @Valid OwnerJoinReqDto reqDto) {
		Long id = ownerService.join(reqDto);
		return ResponseEntity.created(URI.create("/api/v1/owners" + id)).build();
	}
}
