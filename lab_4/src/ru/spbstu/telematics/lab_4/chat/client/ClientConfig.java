package ru.spbstu.telematics.lab_4.chat.client;

import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.internal.txw2.annotation.XmlElement;

import ru.spbstu.telematics.lab_4.chat.common.IConfig;

/**
 * Класс реализующий настроки клиентского приложения.
 * @author simonenko
 */
@XmlRootElement
public class ClientConfig implements IConfig {

   private String serverAddress; // IP-адресс сервера.
   private int serverPort;       // Порт сервера

   public ClientConfig() {
      serverAddress = null;
      serverPort = -1;
   }
   
   public ClientConfig(String serveAddress, int serverPort, int selfPort) {
      this.serverAddress = serveAddress;
      this.serverPort = serverPort;
   }
   
   public String getServerAddress() {
      return serverAddress;
   }

   @XmlElement
   public void setServerAddress(String serverAddress) {
      this.serverAddress = serverAddress;
   }

   public int getServerPort() {
      return serverPort;
   }

   @XmlElement
   public void setServerPort(int serverPort) {
      this.serverPort = serverPort;
   }

   @Override
   public boolean checkConfig() {
      return (serverAddress != null && serverAddress.length() > 0) && 
             (serverPort > 0 && serverPort < (2 * Short.MAX_VALUE + 1));
   }
}
