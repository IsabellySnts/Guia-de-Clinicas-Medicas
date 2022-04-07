package br.senai.sp.cfp138.clinicguia.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.senai.sp.cfp138.clinicguia.model.Clinica;
import br.senai.sp.cfp138.clinicguia.repository.ClinicaRepository;
import br.senai.sp.cfp138.clinicguia.repository.TiposRepository;

@Controller
public class ClinicaController {

	@Autowired
	private TiposRepository clinicrep;
	@Autowired
	private ClinicaRepository repository;
	
	@RequestMapping("formClinica")
	public String form(Model model) {
	model.addAttribute("tipo", clinicrep.findAll());
		return "clinica/form";
	}
	
	@RequestMapping("SalvarClinica")
	public String salvar(Clinica clinica, @RequestParam("inputFotos") MultipartFile[] inputFotos) {
		
		System.out.println(inputFotos.length);
		//repository.save(clinica);
			
		return"redirect:formClinica";
	}
	
}
