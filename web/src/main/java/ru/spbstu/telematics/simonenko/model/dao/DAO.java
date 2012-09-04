package main.java.ru.spbstu.telematics.simonenko.model.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

public class DAO<Type> {
	
	protected HibernateTemplate template;
	
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
	public List<Type> getRange(final Class<Type> clazz, final int start, final int count) {
		return (List<Type>) template.executeFind(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery("from " + clazz.getName());
				query.setFirstResult(start);
				query.setMaxResults(count);
				return query.list();
			}
		});
	}
	
	@Transactional(value = "txManager")
	public int getCount(Class<Type> clazz) {
		return DataAccessUtils.intResult(template.find("select count(*) from " + 
				clazz.getName()));
	}

}
