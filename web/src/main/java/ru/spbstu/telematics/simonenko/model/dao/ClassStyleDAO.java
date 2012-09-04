package main.java.ru.spbstu.telematics.simonenko.model.dao;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import main.java.ru.spbstu.telematics.simonenko.model.dto.ClassStyleDTO;

public class ClassStyleDAO extends DAO<ClassStyleDTO> {
	
	@Transactional(value = "txManager")
	public ClassStyleDTO get(Long id) {
		return get(ClassStyleDTO.class, id);
	}
	
	@Transactional(value = "txManager")
	public List<ClassStyleDTO> getRange(int start, int count) {
		return getRange(ClassStyleDTO.class, start, count);
	}
	
	@Transactional(value = "txManager")
	public int getCount() {
		return getCount(ClassStyleDTO.class);
	}

	@Transactional(value = "txManager")
	@SuppressWarnings("unchecked")
	public List<ClassStyleDTO> getByClass(Long classId) {
		return template.find("from " + ClassStyleDTO.class.getName() + 
				" where class_id = '" + classId + "'");
	}
	
	@Transactional(value = "txManager")
	@SuppressWarnings("unchecked")
	public List<ClassStyleDTO> getByStyle(Long styleId) {
		return template.find("from " + ClassStyleDTO.class.getName() + 
				" where style_id = '" + styleId + "'");
	}
	
	@Transactional(value = "txManager")
	@SuppressWarnings("unchecked")
	public List<ClassStyleDTO> getByParams(Long classId, Long styleId) {
		return template.find("from " + ClassStyleDTO.class.getName() + 
				" where class_id = '" + classId + "' and style_id = '" + styleId + "'");
	}
}
