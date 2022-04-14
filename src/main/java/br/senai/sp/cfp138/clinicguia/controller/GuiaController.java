package br.senai.sp.cfp138.clinicguia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
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

import br.senai.sp.cfp138.clinicguia.annotation.Publico;
import br.senai.sp.cfp138.clinicguia.model.Administrador;
import br.senai.sp.cfp138.clinicguia.repository.AdminRepository;
import br.senai.sp.cfp138.clinicguia.util.HashUtil;

@Controller
public class GuiaController {
	
	//permite acessar o repository
	@Autowired
	private AdminRepository repository;
	
	//metodo para acessar o formulario
	@RequestMapping("formAdm")
	public String formAdm() {
		return "administrador/formAdm";
	}
	
	//metodo para salvar os adm
	@RequestMapping(value  = "SalvarAdministrador", method = RequestMethod.POST)
	
	public String salvarAdm(@Valid Administrador adm, BindingResult result, RedirectAttributes attr) {
		
		//verifica se houve erro na validação do objeto
		if(result.hasErrors()) {
			//se tiver algum erro retornara a mensagem
			attr.addFlashAttribute("mensagemErro", "Verifique os campos.");
			return "redirect:formAdm";
		}
		
		//verifica se esta sendo feita uma alteração ao invés de uma inserção
		boolean alteracao = adm.getId() != null ? true : false;
		
		//verifica se a senha esta vazia
		if(adm.getSenha().equals(HashUtil.hash256(""))) {
			
			//se nao for alteracao, a primeira parte do email sera a senha
			if(!alteracao) {
				String parte = adm.getEmail().substring(0, adm.getEmail().indexOf("@"));
				
				//definindo  a senha do adm
				
				adm.setSenha(parte);
			}else {
				
				String hash = repository.findById(adm.getId()).get().getSenha();
				
				//setando a senha com o hash
				
				adm.setSenhaComHash( hash);
			}
		}
		try {
			//salvando administrador
			repository.save(adm);
			attr.addFlashAttribute("mensagemSucesso", "Administrador cadastrado com sucesso. Caso a senha não tenha sido informada no cadastro, "
					+ "a primeira parte do Email será inserido como senha temporária. ID:" +adm.getId());
			return"redirect:formAdm";
			
		} catch (Exception e) {
			attr.addFlashAttribute("mensagemErro", "Houve um erro ao cadastrar o Administrador:" +e.getMessage());
		}
		
		return "redirect:formAdm";
		
	}
	
	// request mapping para listar informando a pasta desejada pelo page
	@RequestMapping("listarAdm/{page}")
	public String litar(Model model, @PathVariable("page") int page) {
		
		//criando um pageble com 6 elementos por pagina e ordenando os objetos nome de forma ascendente
		PageRequest pageable = PageRequest.of(page-1, 6, Sort.by(Sort.Direction.ASC,"nome"));
		
		
		//criando a pagina atual através do repository
		
		Page<Administrador> pagina = repository.findAll(pageable);
		
		//descobrindo o total de paginas
		
		int totalPages = pagina.getTotalPages();
		
		//criando uma lista de inteiros para representar as páginas
		
		List<Integer>pageNumbers = new ArrayList<Integer>();
		
		for(int i = 0; i < totalPages; i++) {
			
			pageNumbers.add(i+1);
		}
		
		model.addAttribute("admin", pagina.getContent());
		model.addAttribute("paginaAtual", page);
		model.addAttribute("totalPaginas", totalPages);
		model.addAttribute("numPaginas", pageNumbers);
		return("administrador/listaAdm");
	}
	
	@RequestMapping("excluirAdm")
	public String excluiradm(Long id) {
		
		repository.deleteById(id);
		
		return "redirect:listarAdm/1";
	}
	
	@RequestMapping("alterarAdm")
	public String alterarAdm(Model model, Long id) {
		
		Administrador adm = repository.findById(id).get();
		model.addAttribute("adm", adm);
		
		return "forward:formAdm";
	}
	@Publico
	@RequestMapping ("login")
	public String login(Administrador admLogin, RedirectAttributes attr, HttpSession session) {
		
		//buscar o adm no Banco de Dados através do email e da senha
		
		Administrador admin = repository.findByEmailAndSenha(admLogin.getEmail(), admLogin.getSenha());
		
		//verificando se existe o adm
		
		//se adm for igual a nulo
		if(admin== null) {
			//avisando ao usuario que nao foi encontrado
			attr.addFlashAttribute("mensagemErro", "Login e/ou senha inválido(s)");
			return "redirect:formLogin";
		}else {
			//se não for nulo, salva na sessão e acessa o sistema
			session.setAttribute("usuarioLogado", admin);
			return "redirect:listandoClinicas/1";
		}
	}
	
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		
		return "redirect:formLogin";
	}
}
