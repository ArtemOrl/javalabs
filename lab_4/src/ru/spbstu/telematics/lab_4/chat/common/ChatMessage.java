package ru.spbstu.telematics.lab_4.chat.common;

import java.io.Serializable;

/**
 * Сериализуемый класс сообщений чата.
 * @author simonenko
 */
public class ChatMessage implements Serializable {
	
	public enum MessageTypes {
		NEW_USER,      // Новый пользователь.
		NEW_USER_ERR,  // Ошибка при создании нового пользователя.
		NEW_USER_TRAN, // Служебное сообщение о новом пользователе.
		UPD_USER,      // Обновление информации о пользователе.
		UPD_USER_TRAN, // Служебное сообщение об обновлении информации.
		CHAT_MSG,      // Сообщение чата.
		CHAT_MSG_TRAN, // Служебное сообщение о новом сообщении чата.
		DEL_USER,      // Удаление пользователя.
		DEL_USER_TRAN, // Служебное сообщение об удалении пользователя.
	}
	
	private static final long serialVersionUID = 9140981791477964042L;
	
	private ClientInfo to;     // Информация о пользователее-получателе сообщения.
	private ClientInfo from;   // Информация о пользователее-отправителе сообщения.
	private MessageTypes type; // Тип сообщения.
	private String message;    // Текст сообщения.
	
	public ChatMessage(ClientInfo to, ClientInfo from,
			MessageTypes type, String message) {
		this.to = to;
		this.from = from;
		this.type = type;
		this.message = message;
	}
	
	public ChatMessage(ChatMessage msg) {
	   type = msg.type;
      to = new ClientInfo(msg.to);
      from = new ClientInfo(msg.from);
      message = new String(msg.message);
   }

   public ClientInfo getTo() {
		return to;
	}
	
	public void setTo(ClientInfo to) {
		this.to = to;
	}
	
	public ClientInfo getFrom() {
		return from;
	}
	
	public void setFrom(ClientInfo from) {
		this.from = from;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public MessageTypes getType() {
		return type;
	}

	public void setType(MessageTypes type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
	   return "msg [" + type.name() + "]: {'" + 
	          from.getName() + "':" + from.getId() + "} > {'" +
	          to.getName() + "':" + to.getId() + "}";
	}
}
