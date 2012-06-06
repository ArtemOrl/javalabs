package ru.spbstu.telematics.lab_4.chat.client;

import java.awt.EventQueue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;

import ru.spbstu.telematics.lab_4.chat.client.ClientEvent.EventTypes;
import ru.spbstu.telematics.lab_4.chat.common.ChatMessage;
import ru.spbstu.telematics.lab_4.chat.common.ChatMessage.MessageTypes;
import ru.spbstu.telematics.lab_4.chat.common.ClientInfo;
import ru.spbstu.telematics.lab_4.chat.common.Constants;

/**
 * 
 * @author simonenko
 */
public class Controller {

   private boolean isActive;                   // Флаг активности прилождения.
   private Selector selector;                  // Селектор для передачи данных.
   private SocketChannel socketChannel;        // Канал сокета.
   private InetSocketAddress serverAddr;       // Адрес сервера.
   private volatile Queue<ClientEvent> queue;  // Очередь входящих от UI сообщений и из сети.
   private volatile Object queueMutex;         // Мьютекс для синхронизации доствупа к очереди.
   private UserInterface userInterface;        // Ссылка на пользовательский интерфейс.
   private Queue<ChatMessage> outputMessages;  // Очередь сообщений на отправку в сеть.
   private HashMap<Integer, ClientInfo> users; // Список пользователей в соответствии со списком из UI.
   private int usersCounter;                   // Счетчик пользователей.
   private ClientInfo info;                    // Информация о пользователе клиента.
   
   /**
    * Конструктор класса.
    * @param config - конфигурация клиента.
    */
   public Controller(ClientConfig config) {
      isActive = false;
      queueMutex = new Object();
      queue = new LinkedList<ClientEvent>();
      serverAddr = new InetSocketAddress(config.getServerAddress(), 
            config.getServerPort());
      outputMessages = new LinkedList<ChatMessage>();
      users = new HashMap<Integer, ClientInfo>();
      usersCounter = 0;
      info = new ClientInfo();
   }
   
   /**
    * Метод доступа к очереди.
    */
   public Queue<ClientEvent> getQueue() {
      return queue;
   }
   
   /**
    * Метод доступа к мьютексу очереди.
    */
   public Object getQueueMutex() {
      return queueMutex;
   }
   
   /**
    * Метод, реализующий основной цикл работы приложения.
    */
   public void run() {
      // Создание селектора, регистрация, открытие сокета.
      try {
         selector = Selector.open();
         socketChannel = SocketChannel.open(serverAddr);
         socketChannel.configureBlocking(false);
         socketChannel.register(selector, 
               SelectionKey.OP_READ | SelectionKey.OP_WRITE);
         isActive = true;
      } catch (Exception e) {
         System.err.println("Error! Contoller: " + e.getMessage());
         e.printStackTrace();
         synchronized (queueMutex) {
            queue.add(new ClientEvent(EventTypes.SHUTDOWN_EVENT));
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  UserInterface.shutdown(userInterface);
               }
            });
         }
      }
      // Основной цикл работы.
      ByteBuffer buffer = ByteBuffer.allocate(Constants.MAX_MSG_LEN);
      while(isActive) {
         try {
            // Обработка внутренних сообщений клиента.
            synchronized (queueMutex) {
               while(!queue.isEmpty()) {
                  ClientEvent event = queue.poll();
                  if(event != null) {
                     switch(event.getType()) {
                     case SHUTDOWN_EVENT:
                        isActive = false;
                        outputMessages.add(new ChatMessage(new ClientInfo(), 
                              info, MessageTypes.DEL_USER, ""));
                        break;
                     case LOGIN_REQUEST:
                        if(event.getMessage().length() > 1) {
                           info.setName(event.getMessage());
                           outputMessages.add(new ChatMessage(new ClientInfo(), 
                                 info, MessageTypes.NEW_USER, ""));
                        }
                        else 
                           userInterface.showLoginResult(false, 
                                 "User already registered. Change name.");
                        break;
                     case LOGIN_CONFIRM:
                        info.setId(event.getInfo().getId());
                        EventQueue.invokeLater(new Runnable() {
                           @Override
                           public void run() {
                              System.out.println("LOGIN_CONFIRM");
                              userInterface.showLoginResult(true,
                                    "You are logged to a chat.");
                           }
                        });
                        break;
                     case LOGIN_REJECT:
                        info.setName("");
                        userInterface.showLoginResult(false, 
                              "User already registered. Change name.");
                        break;
                     case NEW_USER_EVENT:
                     case UPD_USER_EVENT:
                        final boolean isNewUser = 
                           (event.getType() == EventTypes.NEW_USER_EVENT);
                        final ClientInfo newUserInfo = event.getInfo();
                        users.put(usersCounter++, event.getInfo());
                        EventQueue.invokeLater(new Runnable() {
                           @Override
                           public void run() {
                              Vector<String> usersListData = new Vector<String>();
                              Iterator<ClientInfo> it = users.values().iterator();
                              while(it.hasNext())
                                 usersListData.add(it.next().getName());
                              String additionMessage = null;
                              if(isNewUser) additionMessage = "User '" + 
                                    newUserInfo.getName() + "' joined to the chat";
                              userInterface.updateUsersList(usersListData, additionMessage);
                           }
                        });
                        break;
                     case DEL_USER_EVENT:
                        usersCounter = 0;
                        String additionMessage = null;
                        Iterator<ClientInfo> it = users.values().iterator();
                        HashMap<Integer, ClientInfo> newUsersMap = 
                              new HashMap<Integer, ClientInfo>(users.size() - 1);
                        while(it.hasNext()) {
                           ClientInfo deletedUserInfo = it.next();
                           if(deletedUserInfo.getId() == event.getInfo().getId()) {
                              additionMessage = "User '" + deletedUserInfo.getName() + 
                                    "' left the chat";
                              it.remove(); 
                           }
                           else
                              newUsersMap.put(usersCounter++, deletedUserInfo);
                        }
                        users = newUsersMap;
                        if(additionMessage != null) {
                           final String additionMessageF = additionMessage;
                           EventQueue.invokeLater(new Runnable() {
                              @Override
                              public void run() {
                                 Vector<String> usersListData = new Vector<String>();
                                 Iterator<ClientInfo> it = users.values().iterator();
                                 while(it.hasNext())
                                    usersListData.add(it.next().getName());
                                 userInterface.updateUsersList(usersListData, additionMessageF);
                              }
                           });
                        }
                        break;
                     case RECV_MSG_EVENT:
                        final ChatMessage recvMsg = new ChatMessage(info, event.getInfo(),
                              MessageTypes.CHAT_MSG, event.getMessage());
                        EventQueue.invokeLater(new Runnable() {         
                           @Override
                           public void run() {
                              userInterface.printMessage(recvMsg);
                           }
                        });
                        break;
                     case SEND_MSG_EVENT:
                        ClientInfo toInfo = users.get(event.getInfo().getId());
                        if(toInfo == null)
                           System.out.println("fromInfo == null");
                        final ChatMessage sendMsg = new ChatMessage(toInfo, info,
                              MessageTypes.CHAT_MSG, event.getMessage());
                        EventQueue.invokeLater(new Runnable() {         
                           @Override
                           public void run() {
                              userInterface.printMessage(sendMsg);
                           }
                        });
                        outputMessages.add(sendMsg);
                        break; 
                     default:
                        System.out.println("unexpected");
                     }
                  }
               }
            }
            // Чтение и запись сообщений из сети.
            if(socketChannel.isRegistered() && selector.selectNow() > 0) {
               Set<SelectionKey> keys = selector.selectedKeys();
               Iterator<SelectionKey> it = keys.iterator();
               while(it.hasNext()) { 
                  SelectionKey key = it.next();
                  if(key.isReadable()) {
                     buffer.clear();
                     int bytesLength = socketChannel.read(buffer);
                     while(buffer.hasRemaining() && bytesLength > 0) {
                        System.out.println("1: rem = " + buffer.hasRemaining() + " len = " + bytesLength);
                        ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
                        ChatMessage message = (ChatMessage)(new ObjectInputStream(bais)).readObject();
                        if (message != null) {
                           System.out.println("IN: " + message);
                           ClientEvent event = null;
                           switch(message.getType()) {
                           case CHAT_MSG: 
                              event = new ClientEvent(EventTypes.RECV_MSG_EVENT);
                              event.setMessage(message.getMessage());
                              event.setInfo(message.getFrom());
                              break;
                           case NEW_USER:
                              if(message.getFrom().getName().compareTo(info.getName()) == 0)
                                 event = new ClientEvent(EventTypes.LOGIN_CONFIRM);
                              else
                                 event = new ClientEvent(EventTypes.NEW_USER_EVENT);
                              event.setInfo(message.getFrom());
                              break;
                           case NEW_USER_ERR:
                              event = new ClientEvent(EventTypes.LOGIN_REJECT);
                              break;
                           case UPD_USER:
                              event = new ClientEvent(EventTypes.UPD_USER_EVENT);
                              event.setInfo(message.getFrom());
                              break;
                           case DEL_USER:
                              event = new ClientEvent(EventTypes.DEL_USER_EVENT);
                              event.setInfo(message.getFrom());
                              break;
                           }
                           synchronized (queueMutex) {
                              if(event != null) queue.add(event);
                           }
                           buffer.clear();
                           bytesLength = socketChannel.read(buffer);
                           System.out.println("2: rem = " + buffer.hasRemaining() + " len = " + bytesLength);
                        }
                     }
                  }
                  else if(key.isWritable()) {
                     while(!outputMessages.isEmpty()) {
                        ChatMessage outputMessage = outputMessages.poll();
                        if(outputMessage != null) {
                           System.out.println("OUT: " + outputMessage);
                           ByteArrayOutputStream baos = new ByteArrayOutputStream();
                           (new ObjectOutputStream(baos)).writeObject(outputMessage);
                           socketChannel.write(ByteBuffer.wrap(baos.toByteArray()));
                        }
                     }
                  }
                  it.remove();
               }
            }
            
         } catch (Exception e) {
            System.err.println("Error! Contoller: " + e.getMessage());
            e.printStackTrace();
         }
      }
      try {
         socketChannel.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void setUI(UserInterface userInterface) {
      this.userInterface = userInterface;
   }
}
