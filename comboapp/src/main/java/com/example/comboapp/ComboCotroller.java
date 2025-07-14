package com.example.comboapp;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;



@Controller
public class ComboCotroller {
	
	//private List<Combo> comboList = new ArrayList<>();
	
	private final ComboRepository comboRepository;
	
	public ComboCotroller(ComboRepository comboRepository) {
		this.comboRepository = comboRepository;
	}
	
	@GetMapping("/form")
	public String showForm(Model model) {
		model.addAttribute("comboForm", new ComboForm());
		model.addAttribute("submitted", false);
		return "form";
	}
	
	@PostMapping("/add")
	public String addCombo(@Valid @ModelAttribute ComboForm form, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		//comboList.add(combo);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("submitted", true);
			return "form";
		}
		
		ModelMapper mapper = new ModelMapper();
		Combo comboEntity = mapper.map(form, Combo.class);
		
		comboRepository.save(comboEntity);
		redirectAttributes.addFlashAttribute("message", "登録しました!");
		redirectAttributes.addFlashAttribute("messageType", "success");
		return "redirect:/list";
	}
	
	@GetMapping("/list")
	public String showList(Model model) {
		model.addAttribute("combos", comboRepository.findAll());
		return "list";
	}
	
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id,Model model) {
		Combo combo = comboRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid combo Id:" + id));
		model.addAttribute("combo", combo);
		return "edit";
		
	}
	
	@PostMapping("/update")
	public String updateCombo(@ModelAttribute UpdateForm form, RedirectAttributes redirectAttributes) {
		
		Combo existing = comboRepository.findById(form.getId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid combo Id:"));
		
		existing.setCharacter(form.getCharacter());
		existing.setCombo(form.getCombo());
		
		comboRepository.save(existing);
		
		redirectAttributes.addFlashAttribute("message", "更新しました！");
		redirectAttributes.addFlashAttribute("messageType", "success");
		return "redirect:/list";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		comboRepository.deleteById(id);
		redirectAttributes.addFlashAttribute("message","削除しました!");
		redirectAttributes.addFlashAttribute("messageType", "danger");
		return "redirect:/list";
	}
	

}
