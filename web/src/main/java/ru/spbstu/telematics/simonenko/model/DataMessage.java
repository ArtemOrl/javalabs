package main.java.ru.spbstu.telematics.simonenko.model;

import java.util.List;

public class DataMessage<POJOType> {
	
	public enum ErrorCodes {
		ERR_NONE,   // Нет ошибки
		ERR_EDT_AC, // [Already created]        При редактировании получен объект уже имеющийся в БД
		ERR_EDT_CF, // [Can't find]             Нельзя найти объект для редактирования по данному id
		ERR_EDT_ID, // [Incorrect dependencies] Указаны зависимости не существующие в БД
		ERR_ADD_AC, // [Already created]        
		ERR_ADD_ID, // [Incorrect dependencies] 
		ERR_DEL_CF, // [Can't find]
		ERR_SCH_IE, // [Internal error]         Пришли неверные данные, которых не ожидали
		ERR_SCH_ID, // [Incorrect dependencies] 
		ERR_SCH_CF, // [Can't find]
	}
	
	public enum ManageTypes {
		ADD,
		EDIT,
		DELETE,
		SEARCH,
		NONE,
	}
	
	private int currentPage;
	private int pagesAmount;
	private List<POJOType> POJOs;
	private ErrorCodes errorCode;
	private ManageTypes manageType;
	
	public DataMessage(List<POJOType> POJOs, ManageTypes manageType) {
		this.POJOs = POJOs;
		this.currentPage = 0;
		this.pagesAmount = 0;
		this.errorCode = ErrorCodes.ERR_NONE;
		this.manageType = manageType;
	}
	
	public DataMessage(List<POJOType> POJOs, 
			ManageTypes manageType, int currentPage, int pagesAmount) {
		this.POJOs = POJOs;
		this.currentPage = currentPage;
		this.pagesAmount = pagesAmount;
		this.errorCode = ErrorCodes.ERR_NONE;
		this.manageType = manageType;
	}
	
	public DataMessage(DataMessage<POJOType> source, ErrorCodes errorCode) {
		this.POJOs = source.POJOs;
		this.errorCode = errorCode;
		this.currentPage = source.currentPage;
		this.pagesAmount = source.pagesAmount;
		this.manageType = ManageTypes.NONE;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	public int getPagesAmount() {
		return pagesAmount;
	}
	
	public void setPagesAmount(int pagesAmount) {
		this.pagesAmount = pagesAmount;
	}
	
	public List<POJOType> getPOJOs() {
		return POJOs;
	}
	
	public void setPOJOs(List<POJOType> POJOs) {
		this.POJOs = POJOs;
	}
	
	public ErrorCodes getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCodes errorCode) {
		this.errorCode = errorCode;
	}
	
	public ManageTypes getManageType() {
		return manageType;
	}
	
	public void setManageType(ManageTypes manageType) {
		this.manageType = manageType;
	}

}
