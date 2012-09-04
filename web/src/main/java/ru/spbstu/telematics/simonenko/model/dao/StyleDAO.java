package main.java.ru.spbstu.telematics.simonenko.model.dao;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import main.java.ru.spbstu.telematics.simonenko.model.dto.StyleDTO;

public class StyleDAO extends DAO<StyleDTO> {
	
	@Transactional(value = "txManager")
	public StyleDTO get(Long id) {
		return get(StyleDTO.class, id);
	}
	
	@Transactional(value = "txManager")
	public List<StyleDTO> getRange(int start, int count) {
		return getRange(StyleDTO.class, start, count);
	}

	@Transactional(value = "txManager")
	public int getCount() {
		return getCount(StyleDTO.class);
	}
	
	@Transactional(value = "txManager")
	@SuppressWarnings("unchecked")
	public List<StyleDTO> getByFamily(Long familyId) {
		return template.find("from " + StyleDTO.class.getName() + 
				" where family_id = '" + familyId + "'");
	}
	
	@Transactional(value = "txManager")
	@SuppressWarnings("unchecked")
	public List<StyleDTO> getByName(String name) {
		return template.find("from " + StyleDTO.class.getName() + 
				" where name = '" + name + "'");
	}

}
