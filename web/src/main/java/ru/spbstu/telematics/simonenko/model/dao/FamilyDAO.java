package main.java.ru.spbstu.telematics.simonenko.model.dao;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import main.java.ru.spbstu.telematics.simonenko.model.dto.FamilyDTO;

public class FamilyDAO extends DAO<FamilyDTO> {

	@Transactional(value = "txManager")
	public FamilyDTO get(Long id) {
		return get(FamilyDTO.class, id);
	}
	
	@Transactional(value = "txManager")
	public List<FamilyDTO> getRange(int start, int count) {
		return getRange(FamilyDTO.class, start, count);
	}
	
	@Transactional(value = "txManager")
	public int getCount() {
		return getCount(FamilyDTO.class);
	}
	
	@Transactional(value = "txManager")
	@SuppressWarnings("unchecked")
	public List<FamilyDTO> getByName(String name) {
		return template.find("from " + FamilyDTO.class.getName() + 
				" where name = '" + name + "'");
	}
}
