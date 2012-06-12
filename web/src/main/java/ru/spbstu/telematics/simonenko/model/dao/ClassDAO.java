package main.java.ru.spbstu.telematics.simonenko.model.dao;

import java.util.List;

import main.java.ru.spbstu.telematics.simonenko.model.dto.ClassDTO;

public class ClassDAO extends DAO<ClassDTO> {

	public ClassDTO get(Long id) {
		return get(ClassDTO.class, id);
	}
	
	public List<ClassDTO> getAll() {
		return getAll(ClassDTO.class);
	}
	
}
