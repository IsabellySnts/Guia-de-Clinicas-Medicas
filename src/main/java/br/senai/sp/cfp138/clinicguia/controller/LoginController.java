package br.senai.sp.cfp138.clinicguia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.senai.sp.cfp138.clinicguia.annotation.Publico;

@Controller
public class LoginController {

	@Publico
	@RequestMapping("formLogin")
	public String form(Model model) {
		
		return "login/formLogin";
	}
}
