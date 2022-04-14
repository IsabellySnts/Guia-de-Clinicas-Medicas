package br.senai.sp.cfp138.clinicguia.controller;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import br.senai.sp.cfp138.clinicguia.model.Clinica;
import br.senai.sp.cfp138.clinicguia.repository.ClinicaRepository;
import br.senai.sp.cfp138.clinicguia.repository.TiposRepository;
import br.senai.sp.cfp138.clinicguia.util.FirebaseUtil;

@Controller
public class ClinicaController {

	@Autowired
	private TiposRepository clinicrep;
	@Autowired
	private ClinicaRepository repository;
	@Autowired
	private FirebaseUtil firebaseUtil;
	
	
	//metodo levando ao formulario
	@RequestMapping("formClinica")
	public String form(Model model) {
	model.addAttribute("tipo", clinicrep.findAll());
		return "clinica/form";
	}
	
	//metodo para salvar as clinicas no BD
	@RequestMapping("SalvarClinica")
	public String salvar(Clinica clinica, @RequestParam("inputFotos") MultipartFile[] inputFotos) {
		
		//String para a URL das fotos
		String fotos = clinica.getFotos();
		
		for (MultipartFile arquivo : inputFotos) {
			//verficiando se o arquivo está vazio
			if(arquivo.getOriginalFilename().isEmpty()) {
				//vai para o proximo
				continue;
			}
			
			//fazendo o upload para a nuvem e obtém a url gerada
			try {
				fotos += firebaseUtil.uploadFile(arquivo)+";";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//atribui a String fotos ao objeto clinica
		clinica.setFotos(fotos);
		
		repository.save(clinica);
			
		return"redirect:formClinica";
	}
	
	//método para listar as clinicas
	@RequestMapping("listandoClinicas/{page}")
	public String listarClinicas(Model model, @PathVariable ("page") int page) {
		
		PageRequest pageable = PageRequest.of(page -1, 6, Sort.by(Sort.Direction.ASC, "nome"));
		
		
		//criando a pagina atual através do repository
		
		Page<Clinica> pagina = repository.findAll(pageable);
		
		//descobrindo o total de paginas
		
		int totalPages = pagina.getTotalPages();
		
		//criando uma lista de inteiros para representar as páginas
		
		List<Integer>pageNumbers = new ArrayList<Integer>();
		
		for(int i = 0; i < totalPages; i++) {
			
			pageNumbers.add(i+1);
		}
		
		model.addAttribute("clinica", pagina.getContent());
		model.addAttribute("totalpages", totalPages);
		model.addAttribute("paginaAtual", page);
		model.addAttribute("numeroPaginas", pageNumbers);
		
		return "clinica/listaDasClinicas";
	}
	
	//metodo para alterar as clinicas
	@RequestMapping("alterarClin")
	public String alterarClinica(Model model, Long id) {
		
		Clinica c = repository.findById(id).get();
		model.addAttribute("clinica", c);
		
		return "forward:formClinica";
	}
	
	@RequestMapping("excluindoClinica")
	public String excluirClinic(Long id) {
		
		Clinica clin = repository.findById(id).get();
		
		//se o tamanho da foto for maior que zero fazer o for percorrendo as fotos
		if(clin.getFotos().length() > 0) {
			for (String foto : clin.verFotos()) {
				firebaseUtil.deletar(foto);
			}
		}
		
	
		return "redirect:listandoClinica/1";
	}
	
	
	@RequestMapping ("excluirFotoClinica")
	public String excluirFoto(Long idClinica, int numFoto, Model model) {
		
		//buscando a clinica no banco de dados
		
		Clinica clinic = repository.findById(idClinica).get();
		
		//pegando a String da foto a ser excluida
		String fotoUrl = clinic.verFotos()[numFoto];
		
		//excluindo do firebase
		
		firebaseUtil.deletar(fotoUrl);
		
		//arracando a foto da String fotos 
		clinic.setFotos(clinic.getFotos().replace("fotoUrl"+ ";", ""));
		
		//salvando no banco de dados o objeto rest
		repository.save(clinic);
		
		//adicionando o rest na Model
		model.addAttribute("clinica", clinic);
		
		//encaminhando para o form
		
		return "forward:excluirFoto";
	}
	
}
