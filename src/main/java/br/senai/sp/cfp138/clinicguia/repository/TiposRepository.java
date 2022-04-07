package br.senai.sp.cfp138.clinicguia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import br.senai.sp.cfp138.clinicguia.model.TipoClinica;

public interface TiposRepository extends PagingAndSortingRepository<TipoClinica, Long>{
	
	@Query ("SELECT c FROM TipoClinica c WHERE c.palavrasChave LIKE %:c%")
	public List<TipoClinica> procurarPorPalavraChave(@Param("c") String palavrasChave);
	
	@Query ("SELECT c FROM TipoClinica c WHERE c.nome LIKE %:nome%")
	public List<TipoClinica> procurarPorNome(@Param("nome") String palavrasChave);
	
	@Query ("SELECT c FROM TipoClinica c WHERE c.nome LIKE %:d%")
	public List<TipoClinica> procurarPordesc(@Param("d") String palavrasChave);
	
	

}
