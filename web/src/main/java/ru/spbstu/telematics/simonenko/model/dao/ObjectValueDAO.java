package main.java.ru.spbstu.telematics.simonenko.model.dao;

import java.util.List;

import main.java.ru.spbstu.telematics.simonenko.model.dto.ObjectValueDTO;

public class ObjectValueDAO extends DAO<ObjectValueDTO> {

	public ObjectValueDTO get(Long id) {
		return get(ObjectValueDTO.class, id);
	}
	
	public List<ObjectValueDTO> getAll() {
		return getAll(ObjectValueDTO.class);
	}
	
}
