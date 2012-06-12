package main.java.ru.spbstu.telematics.simonenko.model.dao;

import java.util.List;

import main.java.ru.spbstu.telematics.simonenko.model.dto.ObjectDTO;

public class ObjectDAO extends DAO<ObjectDTO> {

	public ObjectDTO get(Long id) {
		return get(ObjectDTO.class, id);
	}
	
	public List<ObjectDTO> getAll() {
		return getAll(ObjectDTO.class);
	}
	
}
