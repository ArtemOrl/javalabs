package main.java.ru.spbstu.telematics.simonenko.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import main.java.ru.spbstu.telematics.simonenko.model.IndexPageDataBuilder;
import main.java.ru.spbstu.telematics.simonenko.model.dao.ClassDAO;
import main.java.ru.spbstu.telematics.simonenko.model.dao.ClassStyleDAO;
import main.java.ru.spbstu.telematics.simonenko.model.dao.FamilyDAO;
import main.java.ru.spbstu.telematics.simonenko.model.dao.ObjectDAO;
import main.java.ru.spbstu.telematics.simonenko.model.dao.StyleDAO;
import main.java.ru.spbstu.telematics.simonenko.model.dto.ObjectDTO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.ClassPOJO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.FamilyPOJO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.ObjectPOJO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.StylePOJO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/")
public class IndexController {
	
	private static Logger LOG = Logger.getLogger("IndexController");
	
	private IndexPageDataBuilder builder;
	
	@Autowired
	public void setBuilder(IndexPageDataBuilder builder) {
		this.builder = builder;
	}

	@RequestMapping("/")
	public String rootRedirect() {
		return "redirect:index";
	}
	
	@RequestMapping("index")
	public ModelAndView getIndexPage() {
		ModelAndView mav = new ModelAndView("index");
		return mav;
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/obj/{page}", method = RequestMethod.GET)
	public @ResponseBody List<ObjectPOJO> getObjects(@PathVariable("page") Integer page)
	{
		return builder.getObjects(page);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/obj/{page}/delete/{id}", method = RequestMethod.GET)
	public @ResponseBody List<ObjectPOJO> deleteObject(@PathVariable("page") Integer page,
			@PathVariable("id") Long id)
	{
		return builder.getObjects(page);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/cls/{page}", method = RequestMethod.GET)
	public @ResponseBody List<ClassPOJO> getClasses(@PathVariable("page") Integer page)
	{
		return builder.getClasses(page);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/cls/id/{id}", method = RequestMethod.GET)
	public @ResponseBody List<ClassPOJO> getClass(@PathVariable("id") Long id)
	{
		return builder.getClass(id);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/cls/{page}/add", method = RequestMethod.GET)
	public @ResponseBody List<ClassPOJO> addClass(@PathVariable("page") Integer page,
			@RequestParam("id") Long classId, @RequestParam("name") String className,
			@RequestParam("description") String classDescription)
	{
		builder.addClass(new ClassPOJO(classId, className, classDescription));
		return builder.getClasses(page);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/cls/{page}/edit", method = RequestMethod.GET)
	public @ResponseBody List<ClassPOJO> editClass(@PathVariable("page") Integer page,
			@RequestBody ClassPOJO classPOJO)
	{
		//TODO
		return builder.getClasses(page);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/cls/{page}/delete/{id}", method = RequestMethod.GET)
	public @ResponseBody List<ClassPOJO> deleteClass(@PathVariable("page") Integer page,
			@PathVariable("id") Long id)
	{
		builder.deleteClass(id);
		return builder.getClasses(page);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/fam/{page}", method = RequestMethod.GET)
	public @ResponseBody List<FamilyPOJO> getFamilies(@PathVariable("page") Integer page)
	{
		return builder.getFamilies(page);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/fam/id/{id}", method = RequestMethod.GET)
	public @ResponseBody List<FamilyPOJO> getFamily(@PathVariable("id") Long id)
	{
		return builder.getFamily(id);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/fam/{page}/delete/{id}", method = RequestMethod.GET)
	public @ResponseBody List<FamilyPOJO> deleteFamily(@PathVariable("page") Integer page,
			@PathVariable("id") Long id)
	{
		return builder.getFamilies(page);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/stl/{page}", method = RequestMethod.GET)
	public @ResponseBody List<StylePOJO> getStyles(@PathVariable("page") Integer page)
	{
		return builder.getStyles(page);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/stl/{page}/delete/{id}", method = RequestMethod.GET)
	public @ResponseBody List<StylePOJO> deleteStyle(@PathVariable("page") Integer page,
			@PathVariable("id") Long id)
	{
		return builder.getStyles(page);
	}
	
}
