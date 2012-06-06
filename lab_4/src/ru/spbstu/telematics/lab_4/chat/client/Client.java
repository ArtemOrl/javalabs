package ru.spbstu.telematics.lab_4.chat.client;

import ru.spbstu.telematics.lab_4.chat.common.Config;

/**
 * Класс клиентского приложения.
 * @author simonenko
 */
public class Client {
   
   public final static String CONFIG_FILE = "client-config.xml";
   
   public static void main(String[] args) {
      try {
         ClientConfig config = Config.create(CONFIG_FILE, ClientConfig.class);
         Controller controller = new Controller(config);
         (new UserInterface(controller)).run();
         controller.run();
      } catch (Exception e) {
         System.err.println("Error! " + e.getMessage());
      }
   }
}
