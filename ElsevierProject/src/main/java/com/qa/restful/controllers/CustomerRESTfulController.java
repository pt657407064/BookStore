package com.qa.restful.controllers;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.qa.models.Book;
import com.qa.models.Customer;
import com.qa.services.BookService;
import com.qa.services.CustomerService;

@RestController
@SessionAttributes(names={"books","cart_items","logged_in_customer","Address"})
public class CustomerRESTfulController {

	@Autowired
	BookService bookService;
	
	@Autowired
	CustomerService customerService;
	
	@RequestMapping("/loadAllBooks")
	public Iterable<Book> indexPage(HttpServletRequest request)
	{
		
		ArrayList<Book> cartItems = null;
		
		HttpSession session = request.getSession();
		
		Object items = session.getAttribute("cart_items");
		
		if(items!=null)
		{
			cartItems = (ArrayList<Book>) items;
		}else
		{
			cartItems = new ArrayList<Book>();
		}
		
	
		Iterable<Book> books = bookService.findAllBooks();
		
		ModelAndView modelAndView = new ModelAndView("index","books",books);
		
		modelAndView.addObject("cart_items",cartItems);
		return books;
		
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<Customer> register(@Valid @RequestBody Customer customer)
	{

	  	Customer created = customerService.saveCustomer(customer);
		return new ResponseEntity<>(created,HttpStatus.OK);
	}
	
}
