package ru.spbstu.telematics.lab_4.chat.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import ru.spbstu.telematics.lab_4.chat.common.ClientInfo;

/**
 * Регистартор для учета пользователей в системе.
 * @author simonenko
 */
public class ClientRegistrator {

   // Максисмальное количество пользователей в ситеме
   private static int handlerMaxUsers = 0;
   // Список пользователей.                                   
   private static volatile HashMap<Integer, ArrayList<ClientInfo>> usersMap; 
   
   private ClientRegistrator() {
      
   };
   
   /**
    * Метод инициализации регистратора.
    * @param handlers - количество обработчиков на сервере.
    * @param maxUsers - количество пользователей для каждого обработчика.
    */
   public synchronized static void init(int handlers, int maxUsers) {
      handlerMaxUsers = maxUsers;
      usersMap = new HashMap<Integer, ArrayList<ClientInfo>>(handlers);
      for(int i = 0; i < handlers; i++) {
         ArrayList<ClientInfo> usersList = new ArrayList<ClientInfo>(handlerMaxUsers);
         for(int j = 0; j < handlerMaxUsers; j++) {
            int id = i * handlerMaxUsers + j;
            usersList.add(j, new ClientInfo(id));
         }
         usersMap.put(i, usersList);
      }
   }
   
   /**
    * Метод резервирования индекса для нового подключения.
    * @param handler - номер обработчика, с которого производится резервирование.
    */
   public synchronized static int reserveIndex(int handler) {
      int id = -1;
      ArrayList<ClientInfo> usersList = usersMap.get(handler);
      for(int i = 0; i < handlerMaxUsers && id == -1; i++) {
         if(usersList.get(i).isFree()) {
            id = usersList.get(i).getId();
         }
      }
      return id;
   }
   
   /**
    * Метод регистрации нового пользователя.
    * @param handler - номер обработчика, с которого происходит регистрация.
    * @param client - информация о новом клиенте.
    */
   public synchronized static boolean registrate(int handler, ClientInfo client) {
      Iterator<ArrayList<ClientInfo>> handlerIt = 
            usersMap.values().iterator();
      while(handlerIt.hasNext()) {
         ArrayList<ClientInfo> usersList = handlerIt.next();
         for(ClientInfo info : usersList) {
            if(info.getName().compareTo(client.getName()) == 0)
               return false;
         }
      }
      ArrayList<ClientInfo> usersList = usersMap.get(handler);
      int idd = client.getId() - (handler * handlerMaxUsers);
      ClientInfo info = usersList.get(idd);
      if(!info.isFree() || !info.isValid() || info.getId() != client.getId())
         return false;
      else
         info.setName(client.getName());
      return true;
   }

   /**
    * Разрегистрация клиента.
    * @param handler - номер обрабботчика, с которого производится разрегистрация.
    * @param client - информация о клиенте.
    */
   public synchronized static void deregistrate(int handler, ClientInfo client) {
      ArrayList<ClientInfo> usersList = usersMap.get(handler);
      usersList.get(client.getId() - (handler * handlerMaxUsers)).Free();
   }
   
   /**
    * Метод получения количества клиентов для обработчика.
    */
   public synchronized static int getHandlerUserAmount() {
      return handlerMaxUsers;
   }
   
}
