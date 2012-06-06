package ru.spbstu.telematics.lab_4.chat.client;

import ru.spbstu.telematics.lab_4.chat.common.ClientInfo;

/**
 * Класс внутренних сообщений клиента.
 * @author simonenko
 */
public class ClientEvent {

   public enum EventTypes {
      SHUTDOWN_EVENT,  // Завершения приложенния
      LOGIN_REQUEST,   // Запрос на вход в чат.
      LOGIN_CONFIRM,   // Отказан на вход.
      LOGIN_REJECT,    // Подтверждения входа.
      SEND_MSG_EVENT,  // Отправка сообщения.
      RECV_MSG_EVENT,  // Получения сообщения.
      UPD_USER_EVENT,  // Обновление пользователя.
      NEW_USER_EVENT,  // Новый пользователь.
      DEL_USER_EVENT,  // Удаление пользователя.
   }
   
   private EventTypes type;
   private ClientInfo info;
   private String message;
   
   public ClientEvent(EventTypes type) {
      this.type = type;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }
   
   public EventTypes getType() {
      return type;
   }

   public ClientInfo getInfo() {
      return info;
   }

   public void setInfo(ClientInfo info) {
      this.info = info;
   }
}
