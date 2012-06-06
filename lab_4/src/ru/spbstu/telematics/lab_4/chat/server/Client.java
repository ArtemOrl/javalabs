package ru.spbstu.telematics.lab_4.chat.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;
import ru.spbstu.telematics.lab_4.chat.common.ChatMessage;
import ru.spbstu.telematics.lab_4.chat.common.ChatMessage.MessageTypes;
import ru.spbstu.telematics.lab_4.chat.common.ClientInfo;
import ru.spbstu.telematics.lab_4.chat.common.Constants;

/**
 * Класс реализующий представление пользователя на сервере.
 * @author simonenko
 */
public class Client {

   enum ClientStates {
      INACTIVE,     // Неактивное состояние пользователя.
      ACTIVE,       // Активное состояние пользователя.
      REGISTRATED,  // Пользователеь зарегистрирован.
   }
   
   private Handler handler;                       // Обработчик, управляющий клиентом.
	private ClientInfo clientInfo;                 // Инфорамция о данном клиенте.
	private Queue<ChatMessage> inputMessageQueue;  // Очередь входных сообщений.
	private Queue<ChatMessage> outputMessageQueue; // Очередь выходных сообщений.
	private ClientStates clientState;              // Состояние клинета.
	
	/**
	 * Конструктор класса.
	 * @param id - иденитификатор пользователя.
	 * @param handler - ссылка на обработчик.
	 */
	public Client(int id, Handler handler) {
	   this.handler = handler;
	   inputMessageQueue = new LinkedList<ChatMessage>();
	   outputMessageQueue = new LinkedList<ChatMessage>();
	   clientInfo = new ClientInfo(id);
	   deactivate();
	}
	
	/**
	 * Метод получения входящего сообщения из сети.
	 * @param socketChannel - канал сокета.
	 * @throws StreamCorruptedException - исключение при некорректном выходе пользователя.
	 */
	public void inputMessage(SocketChannel socketChannel) throws StreamCorruptedException {
	   ChatMessage message = null;
	   try {
	      ByteBuffer buffer = ByteBuffer.allocate(Constants.MAX_MSG_LEN);
   	   int bytesLength = socketChannel.read(buffer);
   	   while(buffer.hasRemaining() && bytesLength > 0) {
   	      ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
      	   message = (ChatMessage)((new ObjectInputStream(bais)).readObject());
      	   if(message != null) {
      	      inputMessageQueue.add(message);
      	      System.out.println(selfName() + ".I: " + message);
      	   }
      	   buffer.clear();
      	   bytesLength = socketChannel.read(buffer);
   	   }
	   } catch (StreamCorruptedException sce) {
	      throw new StreamCorruptedException();
	   } catch (Exception e) {
	      System.err.println(selfName() + ".E: At inputMessage: " + e.getMessage());
	   }
	}
	
	/**
	 * Метод получения входящего сообщения из обработчика.
	 * @param message - сообщение.
	 */
	public void inputMessage(ChatMessage message) {
	   if(message.getFrom().getId() != clientInfo.getId()) {
	      System.out.println(selfName() + ".I: " + message);
	      inputMessageQueue.add(new ChatMessage(message));
	   }
	}
	
	/**
	 * Метод обработки входящих сообщений.
	 */
	public void processMessages() {
		while(!inputMessageQueue.isEmpty()) {
		   try {
   		   ChatMessage message = inputMessageQueue.poll();
   		   switch(message.getType()) {
               case NEW_USER:
                  if(clientState == ClientStates.INACTIVE) {
                     clientInfo.setName(message.getFrom().getName());
                     if(ClientRegistrator.registrate(handler.getHandlerId(), clientInfo)) {
                        clientState = ClientStates.ACTIVE;
                        message.setFrom(clientInfo);
                        outputMessageQueue.add(message);
                        ChatMessage broadcastMsg = new ChatMessage(message);
                        broadcastMsg.setType(MessageTypes.NEW_USER_TRAN);
                        handler.message(broadcastMsg, true);
                     }
                     else {
                        deactivate();
                        message.setType(MessageTypes.NEW_USER_ERR);
                        outputMessageQueue.add(message);
                     }
                  } 
                  else {
                     throw new Exception("invalid NEW_USER request");
                  }
                  break;
               case CHAT_MSG:
                  if(clientState == ClientStates.ACTIVE) {
                     message.setType(MessageTypes.CHAT_MSG_TRAN);
                     handler.message(message, true);
                  } 
                  else
                     throw new Exception("invalid CHAT_MSG request");
                  break;
               case DEL_USER:
                  if(clientState == ClientStates.ACTIVE) {
                     clientState = ClientStates.REGISTRATED;
                     ClientRegistrator.deregistrate(handler.getHandlerId(), clientInfo);
                     message.setType(MessageTypes.DEL_USER_TRAN);
                     handler.message(message, true);
                  }
                  else
                     throw new Exception("invalid DEL_USER request");
                  break;
               case NEW_USER_TRAN:
                  if(clientState != ClientStates.ACTIVE)
                     throw new Exception("invalid NEW_USER_TRAN request");
                  if(message.getTo().getId() != -1 && 
                        message.getTo().getId() != clientInfo.getId()) 
                     break;
                  message.setType(MessageTypes.NEW_USER);
                  message.setTo(clientInfo);
                  outputMessageQueue.add(message);
                  ChatMessage answer = new ChatMessage(message);
                  answer.setFrom(message.getTo());
                  answer.setTo(message.getFrom());
                  answer.setType(MessageTypes.UPD_USER_TRAN);
                  handler.message(answer, true);
                  break;
               case CHAT_MSG_TRAN:
                  if(clientState != ClientStates.ACTIVE)
                     throw new Exception("invalid CHAT_MSG_TRAN request");
                  if(message.getTo().getId() != clientInfo.getId()) 
                     break;
                  message.setType(MessageTypes.CHAT_MSG);
                  outputMessageQueue.add(message);
                  break;
               case DEL_USER_TRAN:
                  if(clientState != ClientStates.ACTIVE)
                     throw new Exception("invalid DEL_USER_TRAN request");
                  if(message.getTo().getId() != -1 && 
                        message.getTo().getId() != clientInfo.getId()) 
                     break;
                  message.setType(MessageTypes.DEL_USER);
                  outputMessageQueue.add(message);
                  break;
               case UPD_USER_TRAN:
                  if(clientState != ClientStates.ACTIVE)
                     throw new Exception("invalid NEW_USER_TRAN_ANS request");
                  if(message.getTo().getId() != clientInfo.getId()) 
                     break;
                  message.setType(MessageTypes.UPD_USER);
                  outputMessageQueue.add(message);
                  break;
               default:
                  throw new Exception("invalid message type");
   		   }
		   } catch (Exception e) {
		      System.err.println(selfName() + ".E: At processMessages: " + e.getMessage());
		      e.printStackTrace();
		   }
		}
	}
	
	/**
	 * Метод отправки сообщений в сеть.
	 * @param socketChannel - канал сокета.
	 */
	public void outputMessage(SocketChannel socketChannel) {
	   while(!outputMessageQueue.isEmpty()) {
	      try {
	         ChatMessage msg = outputMessageQueue.poll();
	         ByteArrayOutputStream baos = new ByteArrayOutputStream();
	         (new ObjectOutputStream(baos)).writeObject(msg);
	         socketChannel.write(ByteBuffer.wrap(baos.toByteArray()));
	         System.out.println(selfName() + ".O: " + msg);
	      } catch (Exception e) {
	         System.err.println(selfName() + ".E: At outputMessage: " + e.getMessage());
	      }
	   }
	}
	
	/**
	 * Метод деактивации пользователя.
	 */
	public void deactivate() {
	   clientInfo.setName("");
	   inputMessageQueue.clear();
	   outputMessageQueue.clear();
	   clientState = ClientStates.INACTIVE;
	}
	
	/**
	 * Метод проверки активен ли пользователь.
	 */
	public boolean isActive() {
	   return (clientState == ClientStates.ACTIVE);
	}
	
	/**
	 * Метод проверки зарегистрирован ли пользователь.
	 */
	public boolean isRegistrated() {
	   return (clientState == ClientStates.REGISTRATED);
	}

	private String selfName() {
	   return "C." + clientInfo.getId();
	}
}
