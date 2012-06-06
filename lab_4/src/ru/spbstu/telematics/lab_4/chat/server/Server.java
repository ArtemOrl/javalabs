package ru.spbstu.telematics.lab_4.chat.server;

import ru.spbstu.telematics.lab_4.chat.common.Config;

/**
 * Класс сервера.
 * @author simonenko
 */
public class Server {
	
	public final static String CONFIG_FILE = "server-config.xml";
	
	public static void main(String[] args) {
		try {
			(new Controller(Config.create(CONFIG_FILE, ServerConfig.class))).run();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}