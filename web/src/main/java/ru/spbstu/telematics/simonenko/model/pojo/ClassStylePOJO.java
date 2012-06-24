package main.java.ru.spbstu.telematics.simonenko.model.pojo;

public class ClassStylePOJO {

	private Long id;
	private ClassPOJO classPOJO;
	private StylePOJO stylePOJO;
	
	public ClassStylePOJO() {
		id = Long.valueOf(-1);
		classPOJO = new ClassPOJO();
		stylePOJO = new StylePOJO();
	}
	
	public ClassStylePOJO(ClassStylePOJO other) {
		id = new Long(other.id);
		classPOJO = new ClassPOJO(other.classPOJO);
		stylePOJO = new StylePOJO(other.stylePOJO);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ClassPOJO getClassPOJO() {
		return classPOJO;
	}

	public void setClassPOJO(ClassPOJO classPOJO) {
		this.classPOJO = classPOJO;
	}

	public StylePOJO getStylePOJO() {
		return stylePOJO;
	}

	public void setStylePOJO(StylePOJO stylePOJO) {
		this.stylePOJO = stylePOJO;
	}
	
}
