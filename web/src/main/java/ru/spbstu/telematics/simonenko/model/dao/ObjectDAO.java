package main.java.ru.spbstu.telematics.simonenko.model.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import main.java.ru.spbstu.telematics.simonenko.model.dto.ObjectDTO;

public class ObjectDAO extends DAO<ObjectDTO> {

	@Transactional(value = "txManager")
	public ObjectDTO get(Long id) {
		return get(ObjectDTO.class, id);
	}
	
	@Transactional(value = "txManager")
	public List<ObjectDTO> getRange(int start, int count) {
		return getRange(ObjectDTO.class, start, count);
	}
	
	@Transactional(value = "txManager")
	public int getCount() {
		return getCount(ObjectDTO.class);
	}
	
	@Transactional(value = "txManager")
	@SuppressWarnings("unchecked")
	public List<ObjectDTO> getByClass(Long classId) {
		return template.find("from " + ObjectDTO.class.getName() + 
				" where class_id = '" + classId + "'");		
	}
	
	@Transactional(value = "txManager")
	@SuppressWarnings("unchecked")
	public List<ObjectDTO> getByName(String name) {
		return template.find("from " + ObjectDTO.class.getName() +
				" where object_name = '" + name + "'");
	}
	
}
