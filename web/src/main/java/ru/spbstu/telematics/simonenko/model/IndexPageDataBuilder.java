package main.java.ru.spbstu.telematics.simonenko.model;

import java.util.ArrayList;
import java.util.List;

import main.java.ru.spbstu.telematics.simonenko.model.dao.ClassDAO;
import main.java.ru.spbstu.telematics.simonenko.model.dao.ClassStyleDAO;
import main.java.ru.spbstu.telematics.simonenko.model.dao.FamilyDAO;
import main.java.ru.spbstu.telematics.simonenko.model.dao.ObjectDAO;
import main.java.ru.spbstu.telematics.simonenko.model.dao.StyleDAO;
import main.java.ru.spbstu.telematics.simonenko.model.dto.ClassDTO;
import main.java.ru.spbstu.telematics.simonenko.model.dto.FamilyDTO;
import main.java.ru.spbstu.telematics.simonenko.model.dto.ObjectDTO;
import main.java.ru.spbstu.telematics.simonenko.model.dto.StyleDTO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.ClassPOJO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.FamilyPOJO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.ObjectPOJO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.StylePOJO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class IndexPageDataBuilder {
		
	private int pageElemsAmount;
	private ClassDAO classesService;
	private ClassStyleDAO classStylesService;
	private FamilyDAO familiesService;
	private ObjectDAO objectsService;
	private StyleDAO stylesService;
	
	@Autowired
	public void setClassesService(ClassDAO classesService) {
		this.classesService = classesService;
	}

	@Autowired
	public void setClassStylesService(ClassStyleDAO classStylesService) {
		this.classStylesService = classStylesService;
	}

	@Autowired
	public void setFamiliesService(FamilyDAO familiesService) {
		this.familiesService = familiesService;
	}

	@Autowired
	public void setObjectsService(ObjectDAO objectsService) {
		this.objectsService = objectsService;
	}

	@Autowired
	public void setStylesService(StyleDAO stylesService) {
		this.stylesService = stylesService;
	}
	
	@Autowired
	public void setPageElemsAmount(int pageElemsAmount) {
		this.pageElemsAmount = pageElemsAmount;
	}
	
	@Transactional(value = "txManager")
	public List<ObjectPOJO> getObjects(Integer page) {
		int start = page * pageElemsAmount;
		int end = (page + 1) * pageElemsAmount;
		List<ObjectDTO> DTOs = objectsService.getAll();
		List<ObjectPOJO> result = new ArrayList<ObjectPOJO>();
		for(int i = start; i < end &&  i < DTOs.size(); i++) {
			ObjectDTO objectDTO = DTOs.get(i);
			ClassDTO classDTO = classesService.get(objectDTO.getClassId());
			ClassPOJO classPOJO = new ClassPOJO(classDTO.getId(),
					classDTO.getName(), classDTO.getDescription());
			ObjectPOJO objectPOJO = new ObjectPOJO(objectDTO.getId(),
					objectDTO.getObjectName(), classPOJO);
			result.add(objectPOJO);
		}
		return result;
	}
	
	@Transactional(value = "txManager")
	public List<ClassPOJO> getClasses(Integer page) {
		int start = page * pageElemsAmount;
		int end = (page + 1) * pageElemsAmount;
		List<ClassDTO> DTOs = classesService.getAll();
		List<ClassPOJO> result = new ArrayList<ClassPOJO>();
		for(int i = start; i < end &&  i < DTOs.size(); i++) {
			ClassDTO classDTO = DTOs.get(i);
			ClassPOJO classPOJO = new ClassPOJO(classDTO.getId(),
					classDTO.getName(),
					classDTO.getDescription());
			result.add(classPOJO);
		}
		return result;
	}
	
	@Transactional(value = "txManager")
	public List<ClassPOJO> getClass(Long id)
	{	
		ClassDTO DTO = classesService.get(id);
		List<ClassPOJO> result = new ArrayList<ClassPOJO>();
		if(DTO != null) {
			result.add(new ClassPOJO(DTO.getId(),
					DTO.getName(), DTO.getDescription()));
		}
		return result;
	}
	
	@Transactional(value = "txManager")
	public void addClass(ClassPOJO classPOJO) {
		ClassDTO DTO = new ClassDTO();
		DTO.setId(classPOJO.getId());
		DTO.setName(classPOJO.getName());
		DTO.setDescription(classPOJO.getDescription());
		classesService.add(DTO);
	}

	@Transactional(value = "txManager")
	public void deleteClass(Long id) {
		ClassDTO DTO = classesService.get(id);
		if(DTO != null) {
			try {
				classesService.delete(DTO);
			}
			catch(Exception e) {
				// есть зависимости
			}
		}
	}
	
	@Transactional(value = "txManager")
	public List<FamilyPOJO> getFamilies(Integer page) {
		int start = page * pageElemsAmount;
		int end = (page + 1) * pageElemsAmount;
		List<FamilyDTO> DTOs = familiesService.getAll();
		List<FamilyPOJO> result = new ArrayList<FamilyPOJO>();
		for(int i = start; i < end &&  i < DTOs.size(); i++) {
			FamilyDTO familyDTO = DTOs.get(i);
			FamilyPOJO familyPOJO = new FamilyPOJO(familyDTO.getId(),
					familyDTO.getName(),
					familyDTO.getDescription());
			result.add(familyPOJO);
		}
		return result;
	}
	
	@Transactional(value = "txManager")
	public List<FamilyPOJO> getFamily(Long id) {
		FamilyDTO DTO = familiesService.get(id);
		List<FamilyPOJO> result = new ArrayList<FamilyPOJO>();
		if(DTO != null) {
			result.add(new FamilyPOJO(DTO.getId(),
					DTO.getName(), DTO.getName()));
		}
		return result;
	}
	
	@Transactional(value = "txManager")
	public void deleteFamily(Long id) {
		FamilyDTO DTO = familiesService.get(id);
		if(DTO != null) {
			try {
				familiesService.delete(DTO);
			}
			catch(Exception e) {
				// есть зависимости
			}
		}
	}
	
	@Transactional(value = "txManager")
	public List<StylePOJO> getStyles(Integer page) {
		int start = page * pageElemsAmount;
		int end = (page + 1) * pageElemsAmount;
		List<StyleDTO> DTOs = stylesService.getAll();
		List<StylePOJO> result = new ArrayList<StylePOJO>();
		for(int i = start; i < end &&  i < DTOs.size(); i++) {
			StyleDTO styleDTO = DTOs.get(i);
			FamilyDTO familyDTO = familiesService.get(styleDTO.getFamilyId());
			FamilyPOJO familyPOJO = new FamilyPOJO(familyDTO.getId(),
					familyDTO.getName(),
					familyDTO.getDescription());
			StylePOJO stylePOJO = new StylePOJO(styleDTO.getId(),
					styleDTO.getName(),
					familyPOJO,
					styleDTO.isMandatory(),
					styleDTO.isMultiple());
			result.add(stylePOJO);
		}
		return result;
	}
	
	@Transactional(value = "txManager")
	public void deleteStyle(Long id) {
		StyleDTO DTO = stylesService.get(id);
		if(DTO != null) {
			try {
				stylesService.delete(DTO);
			}
			catch(Exception e) {
				// есть зависимости
			}
		}
	}
}
