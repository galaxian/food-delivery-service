package com.example.fooddelivery.owner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fooddelivery.owner.domain.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
	Optional<Owner> findByIdentifier(String identifier);
}
