package main.java.ru.spbstu.telematics.simonenko.model.dto;

import javax.persistence.*;

@Entity
@Table(name = "T_OBJECT_VALUE")
public class ObjectValueDTO {

	@Id
	@Column(name = "object_value_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "object_id")
	private Long objectId;
	
	@Column(name = "style_id")
	private Long styleId;
	
	@Column(name = "value")
	private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Long getStyleId() {
		return styleId;
	}

	public void setStyleId(Long styleId) {
		this.styleId = styleId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
