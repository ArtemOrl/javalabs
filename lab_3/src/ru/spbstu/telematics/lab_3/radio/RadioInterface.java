package ru.spbstu.telematics.lab_3.radio;

import java.io.*;
import java.util.*;

import javax.swing.plaf.SliderUI;

/**
 * Класс реализующий интерфейс радио, позволяющий выполнять команды
 * со стандлартного потока ввода.
 */
public class RadioInterface {
	
	private boolean _isOn;
	private IRadio _radio;
	private String _inputMessage;
	private List<String> _outputMessages;
	private Object _outputMutex;
	private IRadioButton _activeButton;
	private HashMap<String, IRadioButton> _buttons;
	private Thread _thread;
	
	public RadioInterface(IRadio radio) {
		_isOn = false;
		_radio = radio;
		_activeButton = null;
		_inputMessage = null;
		_outputMessages = new ArrayList<String>();
		_outputMutex = new Object();
		_buttons = new HashMap<String, IRadioButton>();
		_thread = null;
	}
	
	/**
	 * Метод, который инициализирует набор кнопок для радио.
	 */
	public void init() {
		ButtonBuilder buttonBuilder = new ButtonBuilder();
		for(int i = 0; i < Radio.BUTTON_TYPES.length; i++) {
			_buttons.put(Radio.BUTTON_TYPES[i], buttonBuilder.makeButton(Radio.BUTTON_TYPES[i]));
		}
	}
	
	/**
	 * Метод, реализующий основной алгоритм работы интерфейса, позволяющий вводить имя команды,
	 * вызывающий метод {@link IRadioButton#click(IRadio)} для соответствующей кнопки радио, а так же выводящий сообщения
	 * приходящие от радио.
	 */
	public void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
		_thread = Thread.currentThread();
		while(_isOn) {
			try {
				if(reader.ready())
				{
					_inputMessage = reader.readLine();
					if(_inputMessage.length() > 1) {
						_activeButton = _buttons.get(_inputMessage.toUpperCase());
						if(_activeButton != null)
							_activeButton.click(_radio);
						else
							message("Incorrect command");
					}
				}
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			if(!_outputMessages.isEmpty()) {
				try {
					synchronized (_outputMutex) {
						for(int i = 0; i < _outputMessages.size(); i++) {
							writer.write(_outputMessages.remove(i));
							writer.newLine();
						}
						writer.flush();
					}
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				_thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			writer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Включение интерфейса.
	 */
	public void switchOn() {
		_isOn = true;
	}
	
	/**
	 * Выключение интерфейса.
	 */
	public void switchOff() {
		_isOn = false;
	}
	
	/**
	 * Метод, выводящий информацию на экран.
	 * @param msg - текст сообщения.
	 */
	public void message(String msg) {
		synchronized (_outputMutex) {
			_outputMessages.add(new String(msg));
		}
	}
}