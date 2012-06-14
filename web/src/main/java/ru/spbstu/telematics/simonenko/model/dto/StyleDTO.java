package main.java.ru.spbstu.telematics.simonenko.model.dto;

import javax.persistence.*;

@Entity
@Table(name = "T_STYLE")
public class StyleDTO {
	
	@Id
	@Column(name = "style_id")
	private Long id;
	
	@Column(name = "family_id")
	private Long familyId;
	
	@Column(name = "is_mandatory")
	private boolean isMandatory;
	
	@Column(name = "is_multiple")
	private boolean isMultiple;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFamilyId() {
		return familyId;
	}

	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
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
