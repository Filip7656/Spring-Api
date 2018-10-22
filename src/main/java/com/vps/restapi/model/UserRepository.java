package com.vps.restapi.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
	public Optional<User> findByEmail(String email);

	public Optional<User> findByToken(String token);

	public List<User> findByActive(boolean active);
}
