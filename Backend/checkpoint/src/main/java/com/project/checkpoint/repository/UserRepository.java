package com.project.checkpoint.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.checkpoint.entity.Userentity;


public interface UserRepository extends JpaRepository<Userentity, Long>{

	Optional<Userentity> findByEmail(String email);
	
}
