package main.java.ru.spbstu.telematics.simonenko.model.pojo;

public class ObjectValuePOJO {

	private Long id;
	private ObjectPOJO objectPOJO;
	private StylePOJO stylePOJO;
	private String value;
	
	public ObjectValuePOJO() {
		id = Long.valueOf(-1);
		objectPOJO = new ObjectPOJO();
		stylePOJO = new StylePOJO();
		value = "";
	}
	
	public ObjectValuePOJO(ObjectValuePOJO other) {
		id = new Long(other.id);
		objectPOJO = new ObjectPOJO(other.objectPOJO);
		stylePOJO = new StylePOJO(other.stylePOJO);
		value = new String(other.value);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ObjectPOJO getObjectPOJO() {
		return objectPOJO;
	}

	public void setObjectPOJO(ObjectPOJO objectPOJO) {
		this.objectPOJO = objectPOJO;
	}

	public StylePOJO getStylePOJO() {
		return stylePOJO;
	}

	public void setStylePOJO(StylePOJO stylePOJO) {
		this.stylePOJO = stylePOJO;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
