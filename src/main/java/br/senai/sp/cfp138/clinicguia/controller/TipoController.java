package br.senai.sp.cfp138.clinicguia.controller;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.senai.sp.cfp138.clinicguia.model.TipoClinica;
import br.senai.sp.cfp138.clinicguia.repository.TiposRepository;

@Controller
public class TipoController {
	
	@Autowired
	private TiposRepository repository;

	//metodo do form
	@RequestMapping("formTipos")
	public String cadTipos() {
		
		return "clinicas/formTipos";
	}
	
	//metodo para salvar as clinicas
	
	@RequestMapping(value  = "SalvarClinicas", method = RequestMethod.POST)
	public String salvaClinica(@Valid TipoClinica clinicas, BindingResult result, RedirectAttributes attr) {
		
		try {
			
			repository.save(clinicas);
			attr.addFlashAttribute("mensagemSucesso", "Administrador cadastrado com sucesso. Caso a senha não tenha sido informada no cadastro, "
					+ "a primeira parte do Email será inserido como senha temporária. ID:" + clinicas.getId());
			
			return"redirect:formTipos";
			
		} catch (Exception e) {
			
			attr.addFlashAttribute("mensagemErro", "Houve um erro ao cadastrar o Administrador:" +e.getMessage());
		}
		
		return"redirect:formTipos";
	}
	
	
	@RequestMapping("listarclinicas/{page}")
	public String listarClinicas(Model model, @PathVariable("page") int page){
		
		//pageble com 6 elementos ordenado de forma ascendente
		PageRequest pageable = PageRequest.of(page-1, 6, Sort.by(Sort.Direction.ASC,"nome"));
		
		//criando a pagina atual
		Page<TipoClinica> pagina = repository.findAll(pageable);
		
		//total de paginas 
		
		int totalPaginas = pagina.getTotalPages();
		
		List<Integer>pageNumbers = new ArrayList<Integer>();
		
		for(int i = 0; i < totalPaginas; i++) {
			
			pageNumbers.add(i+1);
		}
		
		model.addAttribute("clinica", pagina.getContent());
		model.addAttribute("totalpages", totalPaginas);
		model.addAttribute("paginaAtual", page);
		model.addAttribute("numeroPaginas", pageNumbers);
		
		return"clinicas/listaTipos";
	}
	
	@RequestMapping("excluirClinica")
	public String excluirClinica(Long id) {
		
		repository.deleteById(id);
		
		return "redirect:listarclinicas/1";
	}
	
	@RequestMapping("alterarClinica")
	public String alterarClinica(Model model, Long id) {
		
		TipoClinica clinica= repository.findById(id).get();
		model.addAttribute("clinica", clinica);
		
		return "forward:formTipos";
	}
	
	@RequestMapping("buscando")
	public String buscando() {
		
		return"clinicas/busca";
	}
	
	@RequestMapping("buscarClinica")
	public String buscarNome(String select, String busca, Model model) {
		
		if(select.equals("palavrasChave")) {
			model.addAttribute("clinica", repository.procurarPorPalavraChave(busca));
			return "clinicas/listaTipos";
		}else if(select.equals("nome")) {
			model.addAttribute("clinica", repository.procurarPorNome(busca));
			return "clinicas/listaTipos";
		}else {
			
			model.addAttribute("clinica", repository.procurarPordesc(busca));
			
		}
		
		
		
		return "clinicas/listaTipos";
				
	}
}
