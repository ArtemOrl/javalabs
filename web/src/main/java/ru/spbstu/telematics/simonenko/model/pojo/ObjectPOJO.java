package main.java.ru.spbstu.telematics.simonenko.model.pojo;

public class ObjectPOJO {

	private Long id;
	private String objectName;
	private ClassPOJO classPOJO;
	
	public ObjectPOJO() {
		id = Long.valueOf(-1);
		objectName = "";
		classPOJO = new ClassPOJO();
	}
	
	public ObjectPOJO(Long id, String objectName, ClassPOJO classPOJO) {
		this.id = id;
		this.objectName = objectName;
		this.classPOJO = classPOJO;
	}
	
	public ObjectPOJO(ObjectPOJO other) {
		id = new Long(other.id);
		objectName = new String(other.objectName);
		classPOJO = new ClassPOJO(other.classPOJO);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public ClassPOJO getClassPOJO() {
		return classPOJO;
	}

	public void setClassPOJO(ClassPOJO classPOJO) {
		this.classPOJO = classPOJO;
	}
	
}
