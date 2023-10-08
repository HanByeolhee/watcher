package com.watcher.business.main.controller;

import com.watcher.business.comm.service.CategoryService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.watcher.business.main.service.implementation.MainServiceImpl;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping(value="/")
public class MainController {
	@Autowired
	MainServiceImpl mainService;

	@Autowired
	CategoryService categoryService;

	@RequestMapping(value={"main","/"})
	public ModelAndView showMainPage(HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView("main/main");

		JSONArray jsonArray = new JSONArray().putAll(categoryService.getCategorys());
		mav.addObject("category_list", jsonArray);

		return mav;
	}
}
