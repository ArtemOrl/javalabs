package main.java.ru.spbstu.telematics.simonenko.model.dao;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

public class DAO<Type> {
	
	private static Logger LOG = Logger.getLogger(DAO.class.getName());
	private HibernateTemplate template;
	
	@Autowired
	public void setTemplate(HibernateTemplate template) {
		this.template = template;
	}
	
	@Transactional
	public void add(Type typeDTO) {
		template.save(typeDTO);
		template.flush();
	}

	@Transactional
	public void update(Type typeDTO) {
		template.saveOrUpdate(typeDTO);
		template.flush();
	}

	@Transactional
	public void delete(Type typeDTO) {
		template.delete(typeDTO);
		template.flush();
	}

	@Transactional
	public Type get(Class<Type> clazz, Long id) {
		return (Type) template.get(clazz, id);
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<Type> getAll(Class<Type> clazz) {
		Table tableAnnotation = 
				clazz.getAnnotation(Table.class);
		if(tableAnnotation != null) {
			LOG.info("getAll: tableAnnotation.name = " + tableAnnotation.name());
			return template.find("from " + tableAnnotation.name());
		}
		return null;
	}

}