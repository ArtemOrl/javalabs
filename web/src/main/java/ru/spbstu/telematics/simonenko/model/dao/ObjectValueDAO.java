package main.java.ru.spbstu.telematics.simonenko.model.dao;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import main.java.ru.spbstu.telematics.simonenko.model.dto.ObjectValueDTO;

public class ObjectValueDAO extends DAO<ObjectValueDTO> {

	@Transactional(value = "txManager")
	public ObjectValueDTO get(Long id) {
		return get(ObjectValueDTO.class, id);
	}
	
	@Transactional(value = "txManager")
	public List<ObjectValueDTO> getRange(int start, int count) {
		return getRange(ObjectValueDTO.class, start, count);
	}
	
	@Transactional(value = "txManager")
	public int getCount() {
		return getCount(ObjectValueDTO.class);
	}
	
	@Transactional(value = "txManager")
	@SuppressWarnings("unchecked")
	public List<ObjectValueDTO> getByObject(Long objectId) {
		return template.find("from " + ObjectValueDTO.class.getName() +
				" where object_id = '" + objectId + "'");
	}

	@Transactional(value = "txManager")
	@SuppressWarnings("unchecked")
	public List<ObjectValueDTO> getByStyle(Long styleId) {
		return template.find("from " + ObjectValueDTO.class.getName() +
				" where style_id = '" + styleId + "'");
	}
	
	@Transactional(value = "txManager")
	@SuppressWarnings("unchecked")
	public List<ObjectValueDTO> getByParams(Long objectId, Long styleId, String value) {
		return template.find("from " + ObjectValueDTO.class.getName() + 
				" where object_id = '" + objectId + "'" + 
				" and style_id = '" + styleId + "'" +
				" and value = '" + value + "'");
	}
}
