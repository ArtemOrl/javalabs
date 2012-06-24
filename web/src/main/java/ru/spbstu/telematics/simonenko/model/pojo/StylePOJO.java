package main.java.ru.spbstu.telematics.simonenko.model.pojo;

public class StylePOJO {
	
	private Long id;
	private String name;
	private FamilyPOJO familyPOJO;
	private boolean isMandatory;
	private boolean isMultiple;
	
	public StylePOJO() {
		id = Long.valueOf(-1);
		name = "";
		familyPOJO = new FamilyPOJO();
		isMandatory = false;
		isMultiple = false;
	}
	
	public StylePOJO(Long id, String name, FamilyPOJO familyPOJO,
			boolean isMandatory, boolean isMultiple) {
		this.id = id;
		this.name = name;
		this.familyPOJO = familyPOJO;
		this.isMandatory = isMandatory;
		this.isMultiple = isMultiple;
	}

	public StylePOJO(StylePOJO other) {
		id = new Long(other.id);
		name = new String(other.name);
		familyPOJO = new FamilyPOJO(other.familyPOJO);
		isMandatory = other.isMandatory;
		isMultiple = other.isMultiple;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public FamilyPOJO getFamilyPOJO() {
		return familyPOJO;
	}
	
	public void setFamilyPOJO(FamilyPOJO family) {
		this.familyPOJO = family;
	}
	
	public boolean isMandatory() {
		return isMandatory;
	}
	
	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}
	
	public boolean isMultiple() {
		return isMultiple;
	}
	
	public void setMultiple(boolean isMultiple) {
		this.isMultiple = isMultiple;
	}
	
}
