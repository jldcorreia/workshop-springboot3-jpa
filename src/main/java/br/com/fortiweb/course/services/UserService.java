package br.com.fortiweb.course.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.fortiweb.course.entities.User;
import br.com.fortiweb.course.repositories.UserRepository;
import br.com.fortiweb.course.services.exceptions.DatabaseException;
import br.com.fortiweb.course.services.exceptions.ResourceNotFoundException;


@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	public List<User> findAll() {
		return repository.findAll();
	}
	
	public User findById(Long id) {
		Optional<User> obj = repository.findById(id);
//		return obj.get();
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	public User insert(User obj) {
		return repository.save(obj);
	}
	
	public void delete(Long id) {
		try {
			repository.delete(findById(id));
		} catch (DataIntegrityViolationException ex) {
			throw new DatabaseException(ex.getMessage());
		}
	}
	
	public User update(Long id, User obj) {
		try {
		Optional<User> entity = repository.findById(id);
		updateData(entity.get(), obj);
		return repository.save(entity.get());
		} catch (NoSuchElementException ex) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(User entity, User obj) {
		entity.setName(obj.getName());
		entity.setEmail(obj.getEmail());
		entity.setPhone(obj.getPhone());
	}

}
