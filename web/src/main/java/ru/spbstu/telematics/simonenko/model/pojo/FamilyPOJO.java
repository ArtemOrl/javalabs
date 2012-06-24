package main.java.ru.spbstu.telematics.simonenko.model.pojo;

public class FamilyPOJO {

	private Long id;
	private String name;
	private String description;
	
	public FamilyPOJO() {
		id = Long.valueOf(-1);
		name = "";
		description = "";
	}
	
	public FamilyPOJO(Long id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	public FamilyPOJO(FamilyPOJO other) {
		id = new Long(other.id);
		name = new String(other.name);
		description = new String(other.description);
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
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
