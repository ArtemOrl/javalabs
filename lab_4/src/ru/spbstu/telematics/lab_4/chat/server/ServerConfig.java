package ru.spbstu.telematics.lab_4.chat.server;

import javax.xml.bind.annotation.XmlRootElement;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import ru.spbstu.telematics.lab_4.chat.common.IConfig;

/**
 * Класс конфигурации сервера.
 * @author simonenko
 */
@XmlRootElement
public class ServerConfig implements IConfig {
	
	private String serverAddress;      // IP-адрес сервера.
	private int serverPort;            // Порт сервера.
	private int handlersAmount;        // Количество обработчиков.
	private int handlerMaxConnections; // Количество клиентов обработчика.
	
	public ServerConfig() {
		this.serverAddress = null;
		this.serverPort = -1;
		this.handlersAmount = -1;
		this.handlerMaxConnections = -1;
	}
	
	public ServerConfig(String serverAddress,
			int serverPort,
			int handlersAmount,
			int handlerMaxConnections) {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.handlersAmount = handlersAmount;
		this.handlerMaxConnections = handlerMaxConnections;
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

	public int getHandlersAmount() {
		return handlersAmount;
	}

	@XmlElement
	public void setHandlersAmount(int handlersAmount) {
		this.handlersAmount = handlersAmount;
	}

	public int getHandlerMaxConnections() {
		return handlerMaxConnections;
	}
	
	@XmlElement
	public void setHandlerMaxConnections(int handlerMaxConnections) {
		this.handlerMaxConnections = handlerMaxConnections;
	}
	
	public boolean checkConfig() {
		return ((serverAddress != null) && 
				(serverPort > 0 && serverPort < (2 * Short.MAX_VALUE + 1)) && 
				(handlersAmount > 0) && 
				(handlerMaxConnections > 0));
	}
}
