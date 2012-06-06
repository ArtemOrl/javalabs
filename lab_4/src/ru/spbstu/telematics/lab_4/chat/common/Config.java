package ru.spbstu.telematics.lab_4.chat.common;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * Класс для создания конфигурации из 
 * соответствующих xml-файлов.
 * @author simonenko
 */
public class Config {
	
	private Config() { }
	
	public static <Type extends IConfig> Type create(String configFileName, Class<Type> clazz) 
			throws Exception {
		File configXml = new File(configFileName);
		JAXBContext jc = JAXBContext.newInstance(Class.forName(clazz.getName()));
		Unmarshaller unmarshaller = jc.createUnmarshaller();
        Type config = clazz.cast(unmarshaller.unmarshal(configXml));
        if(!config.checkConfig())
        	throw new Exception("config is incorrect");
        return config;
	}
}
