package net.codejava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AppController {

	@Autowired
	private ProductService service;

	@RequestMapping("/")
	public String viewHomePage(Model model) {
		// String keyword = "reebok";
		String keyword = null;

		/*
		 * if (keyword != null) { return listByPage(model, 1, "name", "asc", keyword); }
		 */
		return listByPage(model, 1, "name", "asc", keyword);

	}

	@GetMapping("/page/{pageNumber}")
	public String listByPage(Model model, @PathVariable("pageNumber") int currentPage,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		Page<Product> page = service.listAll(currentPage, sortField, sortDir, keyword);

		long totalItems = page.getTotalElements();
		int totalPages = page.getTotalPages();
		// int currentPage = page.previousPageable().getPageNumber();

		List<Product> listProducts = page.getContent();

		model.addAttribute("totalItems", totalItems);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("listProducts", listProducts); // next bc of thymeleaf we make the index.html

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("reverseSortDir", reverseSortDir);

		return "index";
	}

	@RequestMapping("/new")
	public String showNewProductForm(Model model) {
		Product product = new Product();
		model.addAttribute("product", product);

		return "new_product";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveProduct(@ModelAttribute("product") Product product) {
		service.save(product);

		return "redirect:/";
	}

	@RequestMapping("/edit/{id}")
	public ModelAndView showEditProductForm(@PathVariable(name = "id") Long id) {
		ModelAndView modelAndView = new ModelAndView("edit_product");
		Product product = service.get(id);
		modelAndView.addObject("product", product);

		return modelAndView;
	}

	@RequestMapping("/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Long id) {
		service.delete(id);

		return "redirect:/";
	}

}
