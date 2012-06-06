package ru.spbstu.telematics.lab_4.chat.common;

import java.io.Serializable;

/**
 * Сериализуемый класс информации о пользователе.
 * @author simonenko
 */
public class ClientInfo implements Serializable {

	private static final long serialVersionUID = 7625312792616598350L;
	
	private int id;      // Идентификатор пользователя.
	private String name; // Имя пользователя.
	
	public ClientInfo() {
		id = -1;
		name = "";
	}
	
	public ClientInfo(int id) {
		this.id = id;
		this.name = "";
	}
	
	public ClientInfo(String name) {
      this.id = -1;
      this.name = name;
   }
	
	public ClientInfo(ClientInfo info) {
	   if(info != null) {
	      id = info.id;
	      name = new String(info.name);
	   }
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isFree() {
	   return (name.length() < 1);
	}
	
	public boolean isValid() {
	   return (id != -1);
	}
	
	public void Free() {
	   name = "";
	}
}
