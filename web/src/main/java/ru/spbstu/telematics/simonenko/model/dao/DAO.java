package main.java.ru.spbstu.telematics.simonenko.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

public class DAO<Type> {
	
	private HibernateTemplate template;
	
	@Autowired
	public void setTemplate(HibernateTemplate template) {
		this.template = template;
	}
	
	@Transactional(value = "txManager")
	public void add(Type typeDTO) {
		template.save(typeDTO);
		template.flush();
	}

	@Transactional(value = "txManager")
	public void update(Type typeDTO) {
		template.saveOrUpdate(typeDTO);
		template.flush();
	}

	@Transactional(value = "txManager")
	public void delete(Type typeDTO) {
		template.delete(typeDTO);
		template.flush();
	}

	@Transactional(value = "txManager")
	public Type get(Class<Type> clazz, Long id) {
		return (Type) template.get(clazz, id);
	}

	@Transactional(value = "txManager")
	@SuppressWarnings("unchecked")
	public List<Type> getAll(Class<Type> clazz) {
		return template.find("from " + clazz.getName());
	}

}
