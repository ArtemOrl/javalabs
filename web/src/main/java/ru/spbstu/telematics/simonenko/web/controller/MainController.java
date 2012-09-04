package main.java.ru.spbstu.telematics.simonenko.web.controller;

import main.java.ru.spbstu.telematics.simonenko.model.DataBuilder;
import main.java.ru.spbstu.telematics.simonenko.model.DataMessage;
import main.java.ru.spbstu.telematics.simonenko.model.DataMessage.ManageTypes;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.ClassPOJO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.ClassStylePOJO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.FamilyPOJO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.ObjectPOJO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.ObjectValuePOJO;
import main.java.ru.spbstu.telematics.simonenko.model.pojo.StylePOJO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/")
public class MainController {
	
	private DataBuilder builder;
	
	@Autowired
	public void setBuilder(DataBuilder builder) {
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
	
	/*
	 * 
	 */
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/obj/{page}", method = RequestMethod.GET)
	public @ResponseBody DataMessage<ObjectPOJO> getObjects(@PathVariable("page") Integer page) {
		return builder.getObjects(page, ManageTypes.NONE);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/obj/{page}/add", method = RequestMethod.GET)
	public @ResponseBody DataMessage<ObjectPOJO> addObject(@PathVariable("page") Integer page,
			@RequestParam("name") String objectName, @RequestParam("class") String className) {
		return builder.addObject(objectName, className, page, ManageTypes.ADD);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/obj/{page}/edit", method = RequestMethod.GET)
	public @ResponseBody DataMessage<ObjectPOJO> editObject(@PathVariable("page") Integer page,
			@RequestParam("id") Long id, @RequestParam("name") String objectName, 
			@RequestParam("class") String className) {
		return builder.editObject(id, objectName, className, page, ManageTypes.EDIT);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/obj/{page}/delete", method = RequestMethod.GET)
	public @ResponseBody DataMessage<ObjectPOJO> deleteObject(@PathVariable("page") Integer page,
			@RequestParam("id") Long id) {
		return builder.deleteObject(id, page, ManageTypes.DELETE);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/obj/{page}/search", method = RequestMethod.GET)
	public @ResponseBody DataMessage<ObjectPOJO> objectSearch(@PathVariable("page") Integer page,
			@RequestParam("type") String searchType, @RequestParam("value") String value) {
		return builder.objectSearch(page, searchType, value, ManageTypes.SEARCH);
	}
	
	/*
	 * 
	 */
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/cls/{page}", method = RequestMethod.GET)
	public @ResponseBody DataMessage<ClassPOJO> getClasses(@PathVariable("page") Integer page) {
		return builder.getClasses(page, ManageTypes.NONE);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/cls/{page}/add", method = RequestMethod.GET)
	public @ResponseBody DataMessage<ClassPOJO> addClass(@PathVariable("page") Integer page,
			@RequestParam("name") String name, @RequestParam("description") String description) {
		return builder.addClass(name, description, page, ManageTypes.ADD);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/cls/{page}/edit", method = RequestMethod.GET)
	public @ResponseBody DataMessage<ClassPOJO> editClass(@PathVariable("page") Integer page,
			@RequestParam("id") Long id, @RequestParam("name") String name,
			@RequestParam("description") String description) {
		return builder.editClass(id, name, description, page, ManageTypes.EDIT);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/cls/{page}/delete", method = RequestMethod.GET)
	public @ResponseBody DataMessage<ClassPOJO> deleteClass(@PathVariable("page") Integer page,
			@RequestParam("id") Long id) {
		return builder.deleteClass(id, page, ManageTypes.DELETE);
	}
	
	/*
	 * 
	 */
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/fam/{page}", method = RequestMethod.GET)
	public @ResponseBody DataMessage<FamilyPOJO> getFamilies(@PathVariable("page") Integer page) {
		return builder.getFamilies(page, ManageTypes.NONE);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/fam/{page}/add", method = RequestMethod.GET)
	public @ResponseBody DataMessage<FamilyPOJO> addFamily(@PathVariable("page") Integer page,
			@RequestParam("name") String name, @RequestParam("description") String description) {
		return builder.addFamily(name, description, page, ManageTypes.ADD);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/fam/{page}/edit", method = RequestMethod.GET)
	public @ResponseBody DataMessage<FamilyPOJO> editFamily(@PathVariable("page") Integer page,
			@RequestParam("id") Long id, @RequestParam("name") String name,
			@RequestParam("description") String description) {
		return builder.editFamily(id, name, description, page, ManageTypes.EDIT);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/fam/{page}/delete", method = RequestMethod.GET)
	public @ResponseBody DataMessage<FamilyPOJO> deleteFamily(@PathVariable("page") Integer page,
			@RequestParam("id") Long id) {
		return builder.deleteFamily(id, page, ManageTypes.DELETE);
	}
	
	/*
	 * 
	 */
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/stl/{page}", method = RequestMethod.GET)
	public @ResponseBody DataMessage<StylePOJO> getStyles(@PathVariable("page") Integer page) {
		return builder.getStyles(page, ManageTypes.NONE);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/stl/{page}/add", method = RequestMethod.GET)
	public @ResponseBody DataMessage<StylePOJO> addFamily(@PathVariable("page") Integer page,
			@RequestParam("name") String name, @RequestParam("family") String family,
			@RequestParam("ismandatory") String isMandatoryStr, @RequestParam("ismultiple") String isMultipleStr) {
		return builder.addStyle(name, family, isMandatoryStr, isMultipleStr, page, ManageTypes.ADD);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/stl/{page}/edit", method = RequestMethod.GET)
	public @ResponseBody DataMessage<StylePOJO> editStyle(@PathVariable("page") Integer page,
			@RequestParam("id") Long id, @RequestParam("name") String name,
			@RequestParam("family") String family, @RequestParam("ismandatory") String isMandatoryStr,
			@RequestParam("ismultiple") String isMultipleStr) {
		return builder.editStyle(id, name, family, isMandatoryStr, isMultipleStr, page, ManageTypes.EDIT);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/stl/{page}/delete", method = RequestMethod.GET)
	public @ResponseBody DataMessage<StylePOJO> deleteStyle(@PathVariable("page") Integer page,
			@RequestParam("id") Long id) {	
		return builder.deleteStyle(id, page, ManageTypes.DELETE);
	}
	
	/*
	 * 
	 */
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/obv/{page}", method = RequestMethod.GET)
	public @ResponseBody DataMessage<ObjectValuePOJO> getValues(@PathVariable("page") Integer page) {
		return builder.getObjectValues(page, ManageTypes.NONE);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/obv/{page}/add", method = RequestMethod.GET)
	public @ResponseBody DataMessage<ObjectValuePOJO> addValue(@PathVariable("page") Integer page,
			@RequestParam("object") String objectName, @RequestParam("style") String styleName,
			@RequestParam("value") String value) {
		return builder.addObjectValue(objectName, styleName, value, page, ManageTypes.ADD);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/obv/{page}/edit", method = RequestMethod.GET)
	public @ResponseBody DataMessage<ObjectValuePOJO> editValue(@PathVariable("page") Integer page,
			@RequestParam("id") Long id, @RequestParam("object") String objectName,
			@RequestParam("style") String styleName, @RequestParam("value") String value) {
		return builder.editObjectValue(id, objectName, styleName, value, page, ManageTypes.EDIT);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/obv/{page}/delete", method = RequestMethod.GET)
	public @ResponseBody DataMessage<ObjectValuePOJO> deleteValue(@PathVariable("page") Integer page,
			@RequestParam("id") Long id) {	
		return builder.deleteObjectValue(id, page, ManageTypes.DELETE);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/obv/{page}/search", method = RequestMethod.GET)
	public @ResponseBody DataMessage<ObjectValuePOJO> objectValuesSearch(@PathVariable("page") Integer page,
			@RequestParam("type") String searchType, @RequestParam("value") String value) {
		return builder.objectValuesSearch(page, searchType, value, ManageTypes.SEARCH);
	}
	
	/*
	 * 
	 */
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/csa/{page}", method = RequestMethod.GET)
	public @ResponseBody DataMessage<ClassStylePOJO> getClassStyles(@PathVariable("page") Integer page) {
		return builder.getClassStyles(page, ManageTypes.NONE);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/csa/{page}/add", method = RequestMethod.GET)
	public @ResponseBody DataMessage<ClassStylePOJO> addClassStyle(@PathVariable("page") Integer page,
			@RequestParam("class") String className, @RequestParam("style") String styleName) {
		return builder.addClassStyle(className, styleName, page, ManageTypes.ADD);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/csa/{page}/edit", method = RequestMethod.GET)
	public @ResponseBody DataMessage<ClassStylePOJO> editClassStyle(@PathVariable("page") Integer page,
			@RequestParam("id") Long id, @RequestParam("class") String className,
			@RequestParam("style") String styleName) {
		return builder.editClassStyle(id, className, styleName, page, ManageTypes.EDIT);
	}
	
	@RequestMapping(headers ={"Accept=application/json"},
			value = "index/csa/{page}/delete", method = RequestMethod.GET)
	public @ResponseBody DataMessage<ClassStylePOJO> deleteClassStyle(@PathVariable("page") Integer page,
			@RequestParam("id") Long id) {
		return builder.deleteClassStyle(id, page, ManageTypes.DELETE);
	}
	
}
