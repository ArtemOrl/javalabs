package main.java.ru.spbstu.telematics.simonenko.model;

import java.util.ArrayList;
import java.util.List;

import main.java.ru.spbstu.telematics.simonenko.model.DataMessage.ErrorCodes;
import main.java.ru.spbstu.telematics.simonenko.model.DataMessage.ManageTypes;
import main.java.ru.spbstu.telematics.simonenko.model.dao.ClassDAO;
import main.java.ru.spbstu.telematics.simonenko.model.dao.ClassStyleDAO;
import main.java.ru.spbstu.telematics.simonenko.model.dao.FamilyDAO;
import main.java.ru.spbstu.telematics.simonenko.model.dao.ObjectDAO;
import main.java.ru.spbstu.telematics.simonenko.model.dao.ObjectValueDAO;
import main.java.ru.spbstu.telematics.simonenko.model.dao.StyleDAO;
import main.java.ru.spbstu.telematics.simonenko.model.dto.ClassDTO;
import main.java.ru.spbstu.telematics.simonenko.model.dto.ClassStyleDTO;
import main.java.ru.spbstu.telematics.simonenko.model.dto.FamilyDTO;
import main.java.ru.spbstu.telematics.simonenko.model.dto.ObjectDTO;
import main.java.ru.spbstu.telematics.simonenko.model.dto.ObjectValueDTO;
import main.java.ru.spbstu.telematics.simonenko.model.dto.StyleDTO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.ClassPOJO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.ClassStylePOJO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.FamilyPOJO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.ObjectPOJO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.ObjectValuePOJO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.StylePOJO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class DataBuilder {
		
	private int pageElemsAmount;
	private ClassDAO classesService;
	private ClassStyleDAO classStylesService;
	private FamilyDAO familiesService;
	private ObjectDAO objectsService;
	private ObjectValueDAO objectValuesService;
	private StyleDAO stylesService;

	private static final Long UNKNOWN_VALUE_ID = (long)1;
	
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
	
	@Autowired
	public void setObjectValuesService(ObjectValueDAO objectValuesService) {
		this.objectValuesService = objectValuesService;
	}
	
	/* 
	 * 
	 */
	
	@Transactional(value = "txManager")
	private List<ObjectPOJO> getObjectPOJOs(List<ObjectDTO> DTOs) {
		List<ObjectPOJO> POJOs = new ArrayList<ObjectPOJO>();
		for(int i = 0; i < DTOs.size(); i++) {
			ObjectDTO objectDTO = DTOs.get(i);
			ClassDTO classDTO = classesService.get(objectDTO.getClassId());
			ClassPOJO classPOJO = new ClassPOJO(classDTO.getId(),
					classDTO.getName(), classDTO.getDescription());
			ObjectPOJO objectPOJO = new ObjectPOJO(objectDTO.getId(),
					objectDTO.getObjectName(), classPOJO);
			POJOs.add(objectPOJO);
		}
		return POJOs;
	}
	
	@Transactional(value = "txManager")
	public DataMessage<ObjectPOJO> getObjects(Integer page, ManageTypes manageType) {
		List<ObjectDTO> DTOs = objectsService.getRange(page * pageElemsAmount,
				pageElemsAmount);
		List<ObjectPOJO> POJOs = getObjectPOJOs(DTOs);
		int entitiesCount = objectsService.getCount();
		int pagesAmount = (entitiesCount / pageElemsAmount) - 1;
		int additionPage = (entitiesCount % pageElemsAmount == 0) ? 0 : 1;
		return new DataMessage<ObjectPOJO>(POJOs, manageType, 
				page, (pagesAmount + additionPage));
	}
	
	@Transactional(value = "txManager")
	public DataMessage<ObjectPOJO> addObject(String objectName, String className, 
			Integer page, ManageTypes manageType) {
		List<ObjectDTO> objectDTOs = objectsService.getByName(objectName);
		if(objectDTOs.isEmpty()) {
			List<ClassDTO> classDTOs = classesService.getByName(className);
			if(classDTOs.size() == 1) {
				ObjectDTO objectDTO = new ObjectDTO();
				objectDTO.setObjectName(objectName);
				objectDTO.setClassId(classDTOs.get(0).getId());
				objectsService.add(objectDTO);
				return getObjects(page, manageType);
			}
			return new DataMessage<ObjectPOJO>(getObjects(page, manageType), 
					ErrorCodes.ERR_ADD_ID);
		}
		return new DataMessage<ObjectPOJO>(getObjects(page, manageType), 
				ErrorCodes.ERR_ADD_AC);
	}
	
	@Transactional(value = "txManager")
	public DataMessage<ObjectPOJO> editObject(Long id, String objectName, String className, 
			Integer page, ManageTypes manageType) {
		ObjectDTO objectDTO = objectsService.get(id);
		if(objectDTO != null) {
			List<ClassDTO> classDTOs = classesService.getByName(className);
			if(classDTOs.size() == 1) {
				objectDTO.setClassId(classDTOs.get(0).getId());
				objectDTO.setObjectName(objectName);
				objectsService.update(objectDTO);
				return getObjects(page, manageType);
			}
			return new DataMessage<ObjectPOJO>(getObjects(page, manageType), 
					ErrorCodes.ERR_ADD_ID);
		}
		return new DataMessage<ObjectPOJO>(getObjects(page, manageType), 
				ErrorCodes.ERR_EDT_CF);
	}
	
	@Transactional(value = "txManager")
	public DataMessage<ObjectPOJO> deleteObject(Long id, Integer page, ManageTypes manageType) {
		ObjectDTO objectDTO = objectsService.get(id);
		if(objectDTO != null) {
			List<ObjectValueDTO> values = objectValuesService.getByObject(id);
			for(int i = 0; i < values.size(); i++)
				objectValuesService.delete(values.get(i));
			objectsService.delete(objectDTO);
			if(page > 0 && 
					objectsService.getRange(page * pageElemsAmount, 1).size() == 0)
				return getObjects(page - 1, manageType);
			return getObjects(page, manageType);
		}
		return new DataMessage<ObjectPOJO>(getObjects(page, manageType), 
				ErrorCodes.ERR_DEL_CF);
	}
	
	@Transactional(value = "txManager")
	public DataMessage<ObjectPOJO> objectSearch(Integer page,
			String searchType, String value, ManageTypes manageType) {
		
		if(searchType.compareToIgnoreCase("byobjectname") == 0) {
			List<ObjectDTO> DTOs = objectsService.getByName(value);
			if(DTOs.isEmpty())
				return new DataMessage<ObjectPOJO>(getObjects(page, manageType), 
						ErrorCodes.ERR_SCH_CF);
			else if(DTOs.size() == 1)
				return new DataMessage<ObjectPOJO>(getObjectPOJOs(DTOs),
						ManageTypes.SEARCH);
			
			return new DataMessage<ObjectPOJO>(getObjects(page, manageType), 
					ErrorCodes.ERR_SCH_ID);
		}
		else if(searchType.compareToIgnoreCase("byclassname") == 0) {
			List<ClassDTO> classDTOs = classesService.getByName(value);
			if(classDTOs.isEmpty())
				return new DataMessage<ObjectPOJO>(getObjects(page, manageType), 
						ErrorCodes.ERR_SCH_CF);
			else if(classDTOs.size() == 1) {
				Long classId = classDTOs.get(0).getId();
				List<ObjectDTO> objectDTOs = 
						objectsService.getByClass(classId);
				if(objectDTOs.isEmpty()) {
					return new DataMessage<ObjectPOJO>(getObjects(page, manageType), 
							ErrorCodes.ERR_SCH_CF);
				}
				return new DataMessage<ObjectPOJO>(getObjectPOJOs(objectDTOs),
						ManageTypes.SEARCH);
			}
			return new DataMessage<ObjectPOJO>(getObjects(page, manageType), 
					ErrorCodes.ERR_SCH_ID);
		}
		return new DataMessage<ObjectPOJO>(getObjects(page, manageType), 
				ErrorCodes.ERR_SCH_IE);
	}
	
	/*
	 * 
	 */
	
	@Transactional(value = "txManager")
	public DataMessage<ClassPOJO> getClasses(Integer page, ManageTypes manageType) {
		List<ClassDTO> DTOs = classesService.getRange((page * pageElemsAmount) + 1,
				pageElemsAmount);
		List<ClassPOJO> POJOs = new ArrayList<ClassPOJO>();
		for(int i = 0; i < DTOs.size(); i++) {
			ClassDTO classDTO = DTOs.get(i);
			ClassPOJO classPOJO = new ClassPOJO(classDTO.getId(),
					classDTO.getName(),
					classDTO.getDescription());
			POJOs.add(classPOJO);
		}
		int entitiesCount = classesService.getCount();
		int pagesAmount = ((entitiesCount - 1) / pageElemsAmount) - 1;
		int additionPage = ((entitiesCount - 1) % pageElemsAmount == 0) ? 0 : 1;
		return new DataMessage<ClassPOJO>(POJOs, 
				manageType, page, (pagesAmount + additionPage));
	}
	
	@Transactional(value = "txManager")
	public DataMessage<ClassPOJO> addClass(String name, 
			String description, Integer page, ManageTypes manageType) {
		if(classesService.getByName(name).size() == 0) {
			ClassDTO classDTO = new ClassDTO();
			classDTO.setName(name);
			classDTO.setDescription(description);
			classesService.add(classDTO);
			return getClasses(page, manageType);
		}
		return new DataMessage<ClassPOJO>(getClasses(page, manageType), 
				ErrorCodes.ERR_ADD_AC);
	}

	@Transactional(value = "txManager")
	public DataMessage<ClassPOJO> editClass(Long id, String name, 
			String description, Integer page, ManageTypes manageType) {
		ClassDTO classDTO = classesService.get(id);
		if(classDTO != null) {
			classDTO.setName(name);
			classDTO.setDescription(description);
			return getClasses(page, manageType);
		}
		return new DataMessage<ClassPOJO>(getClasses(page, manageType), 
				ErrorCodes.ERR_EDT_CF);
	}
	
	@Transactional(value = "txManager")
	public DataMessage<ClassPOJO> deleteClass(Long id, Integer page, ManageTypes manageType) {
		ClassDTO classDTO = classesService.get(id);
		if(classDTO != null) {
			List<ObjectDTO> objects = objectsService.getByClass(id);
			for(int i = 0; i < objects.size(); i++) {
				objects.get(i).setClassId((long)UNKNOWN_VALUE_ID);
				objectsService.update(objects.get(i));
			}
			List<ClassStyleDTO> classStyles = classStylesService.getByClass(id);
			for(int i = 0; i < classStyles.size(); i++)
				classStylesService.delete(classStyles.get(i));
			classesService.delete(classDTO);
			if(page > 0 && 
					classesService.getRange((page * pageElemsAmount) + 1, 1).size() == 0)
				return getClasses(page - 1, manageType);
			return getClasses(page, manageType);
		}
		return new DataMessage<ClassPOJO>(getClasses(page, manageType),
				ErrorCodes.ERR_DEL_CF);
	}
	
	/*
	 * 
	 */
	
	@Transactional(value = "txManager")
	public DataMessage<FamilyPOJO> getFamilies(Integer page, ManageTypes manageType) {
		List<FamilyDTO> DTOs = familiesService.getRange((page * pageElemsAmount) + 1, 
				pageElemsAmount);
		List<FamilyPOJO> POJOs = new ArrayList<FamilyPOJO>();
		for(int i = 0; i < DTOs.size(); i++) {
			FamilyDTO familyDTO = DTOs.get(i);
			FamilyPOJO familyPOJO = new FamilyPOJO(familyDTO.getId(),
					familyDTO.getName(),
					familyDTO.getDescription());
			POJOs.add(familyPOJO);
		}
		int entitiesCount = familiesService.getCount();
		int pagesAmount = ((entitiesCount - 1) / pageElemsAmount) - 1;
		int additionPage = ((entitiesCount - 1) % pageElemsAmount == 0) ? 0 : 1;
		return new DataMessage<FamilyPOJO>(POJOs, 
				manageType, page, (pagesAmount + additionPage));
	}
	
	@Transactional(value = "txManager")
	public DataMessage<FamilyPOJO> addFamily(String name, 
			String description, Integer page, ManageTypes manageType) {
		List<FamilyDTO> familyDTOs = familiesService.getByName(name);
		if(familyDTOs.isEmpty()) {
			FamilyDTO familyDTO = new FamilyDTO();
			familyDTO.setName(name);
			familyDTO.setDescription(description);
			familiesService.add(familyDTO);
			return getFamilies(page, manageType);
		}
		return new DataMessage<FamilyPOJO>(getFamilies(page, manageType), 
				ErrorCodes.ERR_ADD_AC);
	}
	
	@Transactional(value = "txManager")
	public DataMessage<FamilyPOJO> editFamily(Long id, String name, 
			String description, Integer page, ManageTypes manageType) {
		FamilyDTO familyDTO = familiesService.get(id);
		if(familyDTO != null) {
			familyDTO.setName(name);
			familyDTO.setDescription(description);
			familiesService.update(familyDTO);
			return getFamilies(page, manageType);
		}
		return new DataMessage<FamilyPOJO>(getFamilies(page, manageType), 
				ErrorCodes.ERR_EDT_CF);
	}
	
	@Transactional(value = "txManager")
	public DataMessage<FamilyPOJO> deleteFamily(Long id, Integer page, ManageTypes manageType) {
		FamilyDTO familyDTO = familiesService.get(id);
		if(familyDTO != null) {
			List<StyleDTO> styles = stylesService.getByFamily(id);
			for(int i = 0; i < styles.size(); i++) {
				styles.get(i).setFamilyId((long)UNKNOWN_VALUE_ID);
				stylesService.update(styles.get(i));
			}
			familiesService.delete(familyDTO);
			if(page > 0 && 
					familiesService.getRange((page * pageElemsAmount) + 1, 1).size() == 0)
				return getFamilies(page - 1, manageType);
			return getFamilies(page, manageType);
		}
		return new DataMessage<FamilyPOJO>(getFamilies(page, manageType), 
				ErrorCodes.ERR_DEL_CF);
	}
	
	/*
	 * 
	 */
	
	@Transactional(value = "txManager")
	public DataMessage<StylePOJO> getStyles(Integer page, ManageTypes manageType) {
		List<StyleDTO> DTOs = stylesService.getRange((page * pageElemsAmount) + 1,
				pageElemsAmount);
		List<StylePOJO> POJOs = new ArrayList<StylePOJO>();
		for(int i = 0; i < DTOs.size(); i++) {
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
			POJOs.add(stylePOJO);
		}
		int entitiesCount = stylesService.getCount();
		int pagesAmount = ((entitiesCount - 1) / pageElemsAmount) - 1;
		int additionPage = ((entitiesCount - 1) % pageElemsAmount == 0) ? 0 : 1;
		return new DataMessage<StylePOJO>(POJOs, 
				manageType, page, (pagesAmount + additionPage));
	}
	
	@Transactional(value = "txManager")
	public DataMessage<StylePOJO> addStyle(String name,  String family,
			String isMandatoryStr, String isMultipleStr, Integer page, ManageTypes manageType) {
		List<StyleDTO> styleDTOs = stylesService.getByName(name);
		if(styleDTOs.isEmpty()) {
			boolean isMandatory = false, isMultiple = false;
			try {
				isMandatory = getBoolean(isMandatoryStr);
				isMultiple = getBoolean(isMultipleStr);
			} 
			catch (Exception exception) {
				return new DataMessage<StylePOJO>(getStyles(page, manageType), 
						ErrorCodes.ERR_EDT_ID);
			}
			List<FamilyDTO> familyDTOs = familiesService.getByName(family);
			if(familyDTOs.size() == 1) {
				StyleDTO styleDTO = new StyleDTO();
				styleDTO.setName(name);
				styleDTO.setFamilyId(familyDTOs.get(0).getId());
				styleDTO.setMandatory(isMandatory);
				styleDTO.setMultiple(isMultiple);
				stylesService.update(styleDTO);
				return getStyles(page, manageType);
			}
			return new DataMessage<StylePOJO>(getStyles(page, manageType),
					ErrorCodes.ERR_ADD_ID);
		}
		return new DataMessage<StylePOJO>(getStyles(page, manageType),
				ErrorCodes.ERR_ADD_AC);
	}
	
	@Transactional(value = "txManager")
	public DataMessage<StylePOJO> editStyle(Long id, String name, String family,
			String isMandatoryStr, String isMultipleStr, Integer page, ManageTypes manageType) {
		StyleDTO styleDTO = stylesService.get(id);
		if(styleDTO != null) {
			List<FamilyDTO> familyDTOs = familiesService.getByName(family);
			boolean isMandatory = false, isMultiple = false;
			try {
				isMandatory = getBoolean(isMandatoryStr);
				isMultiple = getBoolean(isMultipleStr);
			}
			catch(Exception exception) {
				return new DataMessage<StylePOJO>(getStyles(page, manageType),
						ErrorCodes.ERR_EDT_ID);
			}
			if(familyDTOs.size() == 1) {
				
				styleDTO.setName(name);
				styleDTO.setFamilyId(familyDTOs.get(0).getId());
				styleDTO.setMandatory(isMandatory);
				styleDTO.setMultiple(isMultiple);
				stylesService.update(styleDTO);
				return getStyles(page, manageType);
			}
			return new DataMessage<StylePOJO>(getStyles(page, manageType), 
					ErrorCodes.ERR_EDT_ID);
		}
		return new DataMessage<StylePOJO>(getStyles(page, manageType), 
				ErrorCodes.ERR_EDT_CF);
	}
	
	@Transactional(value = "txManager")
	public DataMessage<StylePOJO> deleteStyle(Long id, Integer page, ManageTypes manageType) {
		StyleDTO styleDTO = stylesService.get(id);
		if(styleDTO != null) {
			List<ObjectValueDTO> values = objectValuesService.getByStyle(id);
			for(int i = 0; i < values.size(); i++) {
				values.get(i).setStyleId((long)UNKNOWN_VALUE_ID);
				objectValuesService.update(values.get(i));
			}
			List<ClassStyleDTO> classStyles = classStylesService.getByStyle(id);
			for(int i = 0; i < classStyles.size(); i++)
				classStylesService.delete(classStyles.get(i));
			stylesService.delete(styleDTO);
			if(page > 0 && 
					stylesService.getRange((page * pageElemsAmount) + 1, 1).size() == 0)
				return getStyles(page - 1, manageType);
			return getStyles(page, manageType);
		}
		return new DataMessage<StylePOJO>(getStyles(page, manageType), 
				ErrorCodes.ERR_DEL_CF);
	}

	/*
	 * 
	 */
	
	@Transactional(value = "txManager") 
	private List<ObjectValuePOJO> getObjectValuePOJOs(List<ObjectValueDTO> DTOs) {
		List<ObjectValuePOJO> POJOs = new ArrayList<ObjectValuePOJO>();
		for(int i = 0; i < DTOs.size(); i++) {
			ObjectValueDTO objectValueDTO = DTOs.get(i);
			ObjectDTO objectDTO = objectsService.get(objectValueDTO.getObjectId());
			StyleDTO styleDTO = stylesService.get(objectValueDTO.getStyleId());
			ObjectPOJO objectPOJO = new ObjectPOJO(objectDTO.getId(),
					objectDTO.getObjectName(), null);
			StylePOJO stylePOJO = new StylePOJO(styleDTO.getId(), styleDTO.getName(),
					null, styleDTO.isMandatory(), styleDTO.isMultiple());
			POJOs.add(new ObjectValuePOJO(objectValueDTO.getId(), 
					objectPOJO, stylePOJO, objectValueDTO.getValue()));
		}
		return POJOs;
	}
	
	@Transactional(value = "txManager")
	public DataMessage<ObjectValuePOJO> getObjectValues(Integer page, ManageTypes manageType) {
		List<ObjectValueDTO> DTOs = objectValuesService.getRange(page * pageElemsAmount,
				pageElemsAmount);
		List<ObjectValuePOJO> POJOs = getObjectValuePOJOs(DTOs);
		int entitiesCount = objectValuesService.getCount();
		int pagesAmount = (entitiesCount / pageElemsAmount) - 1;
		int additionPage = (entitiesCount % pageElemsAmount == 0) ? 0 : 1;
		return new DataMessage<ObjectValuePOJO>(POJOs, 
				manageType, page, (pagesAmount + additionPage));
	}
	
	@Transactional(value = "txManager")
	public DataMessage<ObjectValuePOJO> addObjectValue(String objectName, 
			String styleName, String value, Integer page, ManageTypes manageType) {
		List<ObjectDTO> objectDTOs = objectsService.getByName(objectName);
		List<StyleDTO> styleDTOs = stylesService.getByName(styleName);
		if(objectDTOs.size() == 1 && styleDTOs.size() == 1) {
			Long objectId = objectDTOs.get(0).getId();
			Long styleId = styleDTOs.get(0).getId();
			List<ObjectValueDTO> objectValuesDTOs = 
					objectValuesService.getByParams(objectId, styleId, value);
			if(objectValuesDTOs.isEmpty()) {
				ObjectValueDTO objectValueDTO = new ObjectValueDTO();
				objectValueDTO.setObjectId(objectId);
				objectValueDTO.setStyleId(styleId);
				objectValueDTO.setValue(value);
				objectValuesService.add(objectValueDTO);
				return getObjectValues(page, manageType);
			}
			return new DataMessage<ObjectValuePOJO>(getObjectValues(page, manageType),
					ErrorCodes.ERR_ADD_AC);
		}
		return new DataMessage<ObjectValuePOJO>(getObjectValues(page, manageType), 
				ErrorCodes.ERR_ADD_ID);
	}
	
	@Transactional(value = "txManager")
	public DataMessage<ObjectValuePOJO> editObjectValue(Long id, String objectName, 
			String styleName, String value, Integer page, ManageTypes manageType) {
		ObjectValueDTO objectValueDTO = objectValuesService.get(id);
		if(objectValueDTO != null) {
			List<ObjectDTO> objectDTOs = objectsService.getByName(objectName);
			List<StyleDTO> styleDTOs = stylesService.getByName(styleName);
			if(objectDTOs.size() == 1 && styleDTOs.size() == 1) {
				objectValueDTO.setObjectId(objectDTOs.get(0).getId());
				objectValueDTO.setStyleId(styleDTOs.get(0).getId());
				objectValueDTO.setValue(value);
				objectValuesService.update(objectValueDTO);
				return getObjectValues(page, manageType);
			}
			return new DataMessage<ObjectValuePOJO>(getObjectValues(page, manageType), 
					ErrorCodes.ERR_EDT_ID);
		}
		return new DataMessage<ObjectValuePOJO>(getObjectValues(page, manageType),
				ErrorCodes.ERR_EDT_CF);
	}
	
	@Transactional(value = "txManager")
	public DataMessage<ObjectValuePOJO> deleteObjectValue(Long id, 
			Integer page, ManageTypes manageType) {
		ObjectValueDTO objectValueDTO = objectValuesService.get(id);
		if(objectValueDTO != null) {
			objectValuesService.delete(objectValueDTO);
			if(page > 0 && 
					objectValuesService.getRange(page * pageElemsAmount, 1).size() == 0)
				return getObjectValues(page - 1, manageType);
			return getObjectValues(page, manageType);
		}
		return new DataMessage<ObjectValuePOJO>(getObjectValues(page, manageType),
				ErrorCodes.ERR_DEL_CF);
	}
	
	@Transactional(value = "txManager")
	public DataMessage<ObjectValuePOJO> objectValuesSearch(Integer page, 
			String searchType, String value, ManageTypes manageType) {
		
		if(searchType.compareToIgnoreCase("byobjectname") == 0) {
			List<ObjectDTO> objectDTOs = objectsService.getByName(value);
			if(objectDTOs.isEmpty())
				return new DataMessage<ObjectValuePOJO>(getObjectValues(page, manageType), 
						ErrorCodes.ERR_SCH_CF);
			else if(objectDTOs.size() == 1) {
				Long objectId = objectDTOs.get(0).getId();
				List<ObjectValueDTO> DTOs = 
						objectValuesService.getByObject(objectId);
				if(DTOs.isEmpty()) {
					return new DataMessage<ObjectValuePOJO>(getObjectValues(page, manageType), 
							ErrorCodes.ERR_SCH_CF);
				}
				return new DataMessage<ObjectValuePOJO>(getObjectValuePOJOs(DTOs)
						, ManageTypes.SEARCH);
			}
			return new DataMessage<ObjectValuePOJO>(getObjectValues(page, manageType),
					ErrorCodes.ERR_SCH_ID);
		}
		else if(searchType.compareToIgnoreCase("bystylename") == 0) {
			List<StyleDTO> styleDTOs = stylesService.getByName(value);
			if(styleDTOs.isEmpty())
				return new DataMessage<ObjectValuePOJO>(getObjectValues(page, manageType), 
						ErrorCodes.ERR_SCH_CF);
			else if(styleDTOs.size() == 1) {
				Long styleId = styleDTOs.get(0).getId();
				List<ObjectValueDTO> DTOs = 
						objectValuesService.getByStyle(styleId);
				if(DTOs.isEmpty()) {
					return new DataMessage<ObjectValuePOJO>(getObjectValues(page, manageType),
							ErrorCodes.ERR_SCH_CF);
				}
				return new DataMessage<ObjectValuePOJO>(getObjectValuePOJOs(DTOs)
						, ManageTypes.SEARCH);
			}
			return new DataMessage<ObjectValuePOJO>(getObjectValues(page, manageType),
					ErrorCodes.ERR_SCH_ID);
		}
		return new DataMessage<ObjectValuePOJO>(getObjectValues(page, manageType),
				ErrorCodes.ERR_SCH_IE);
	}
	
	/*
	 * 
	 */
	
	@Transactional(value = "txManager")
	public DataMessage<ClassStylePOJO> getClassStyles(Integer page, ManageTypes manageType) {
		List<ClassStyleDTO> DTOs = classStylesService.getRange(page * pageElemsAmount, 
				pageElemsAmount);
		List<ClassStylePOJO> POJOs = new ArrayList<ClassStylePOJO>();
		for(int i = 0; i < DTOs.size(); i++) {
			ClassStyleDTO classStyleDTO = DTOs.get(i);
			ClassDTO classDTO = classesService.get(classStyleDTO.getClassId());
			StyleDTO styleDTO = stylesService.get(classStyleDTO.getStyleId());
			ClassPOJO classPOJO = new ClassPOJO(classDTO.getId(),
					classDTO.getName(), classDTO.getDescription());
			StylePOJO stylePOJO = new StylePOJO(styleDTO.getId(), styleDTO.getName(),
					null, styleDTO.isMandatory(), styleDTO.isMultiple());
			POJOs.add(new ClassStylePOJO(classStyleDTO.getId(), 
					classPOJO, stylePOJO));
		}
		int entitiesCount = classStylesService.getCount();
		int pagesAmount = (entitiesCount / pageElemsAmount) - 1;
		int additionPage = (entitiesCount % pageElemsAmount == 0) ? 0 : 1;
		return new DataMessage<ClassStylePOJO>(POJOs, 
				manageType, page, (pagesAmount + additionPage));
	}
	
	@Transactional(value = "txManager")
	public DataMessage<ClassStylePOJO> addClassStyle(String className, 
			String styleName, Integer page, ManageTypes manageType) {
		List<ClassDTO> classDTOs = classesService.getByName(className);
		List<StyleDTO> styleDTOs = stylesService.getByName(styleName);
		if(classDTOs.size() == 1 && styleDTOs.size() == 1) {
			Long classId = classDTOs.get(0).getId();
			Long styleId = styleDTOs.get(0).getId();
			List<ClassStyleDTO> classStyleDTOs = 
					classStylesService.getByParams(classId, styleId);
			if(classStyleDTOs.isEmpty()) {
				ClassStyleDTO classStyleDTO = new ClassStyleDTO();
				classStyleDTO.setClassId(classId);
				classStyleDTO.setStyleId(styleId);
				classStylesService.add(classStyleDTO);
				return getClassStyles(page, manageType);
			}
			return new DataMessage<ClassStylePOJO>(getClassStyles(page, manageType),
					ErrorCodes.ERR_ADD_AC);
		}
		return new DataMessage<ClassStylePOJO>(getClassStyles(page, manageType),
				ErrorCodes.ERR_ADD_ID);
	}
	
	@Transactional(value = "txManager")
	public DataMessage<ClassStylePOJO> editClassStyle(Long id,
			String className, String styleName, Integer page, ManageTypes manageType) {
		ClassStyleDTO classStyleDTO = classStylesService.get(id);
		if(classStyleDTO != null) {
			List<ClassDTO> classDTOs = classesService.getByName(className);
			List<StyleDTO> styleDTOs = stylesService.getByName(styleName);
			if(classDTOs.size() == 1 && styleDTOs.size() == 1) {
				long classId = classDTOs.get(0).getId();
				long styleId = styleDTOs.get(0).getId();
				if(classStylesService.getByParams(classId, styleId).size() == 0) {
					classStyleDTO.setClassId(classId);
					classStyleDTO.setStyleId(styleId);
					classStylesService.update(classStyleDTO);
					return getClassStyles(page, manageType);
				}
				return new DataMessage<ClassStylePOJO>(getClassStyles(page, manageType),
						ErrorCodes.ERR_ADD_AC);
			}
			return new DataMessage<ClassStylePOJO>(getClassStyles(page, manageType),
					ErrorCodes.ERR_EDT_ID);
		}
		return new DataMessage<ClassStylePOJO>(getClassStyles(page, manageType),
				ErrorCodes.ERR_EDT_CF);
	}
	
	@Transactional(value = "txManager")
	public DataMessage<ClassStylePOJO> deleteClassStyle(Long id, 
			Integer page, ManageTypes manageType) {
		ClassStyleDTO classStyleDTO = classStylesService.get(id);
		if(classStyleDTO != null) {
			classStylesService.delete(classStyleDTO);
			if(page > 0 && 
					classStylesService.getRange(page * pageElemsAmount, 1).size() == 0)
				return getClassStyles(page - 1, manageType);
			return getClassStyles(page, manageType);
		}
		return new DataMessage<ClassStylePOJO>(getClassStyles(page, manageType),
				ErrorCodes.ERR_DEL_CF);
	}
	
	private boolean getBoolean(String booleanStr) throws Exception {
		if(booleanStr.equalsIgnoreCase("true"))
			return true;
		else if(booleanStr.equalsIgnoreCase("false"))
			return false;
		else
			throw new Exception("Failed to parse boolean");
	}
		
}
