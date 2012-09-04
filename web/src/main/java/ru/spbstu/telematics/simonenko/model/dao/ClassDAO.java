package main.java.ru.spbstu.telematics.simonenko.model.dao;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import main.java.ru.spbstu.telematics.simonenko.model.dto.ClassDTO;

public class ClassDAO extends DAO<ClassDTO> {

	@Transactional(value = "txManager")
	public ClassDTO get(Long id) {
		return get(ClassDTO.class, id);
	}
	
	@Transactional(value = "txManager")
	public List<ClassDTO> getRange(int start, int count) {
		return getRange(ClassDTO.class, start, count);
	}
	
	@Transactional(value = "txManager")
	public int getCount() {
		return getCount(ClassDTO.class);
	}
	
	@Transactional(value = "txManager")
	@SuppressWarnings("unchecked")
	public List<ClassDTO> getByName(String name) {
		return template.find("from " + ClassDTO.class.getName() + 
				" where name = '" + name + "'");
	}
}
