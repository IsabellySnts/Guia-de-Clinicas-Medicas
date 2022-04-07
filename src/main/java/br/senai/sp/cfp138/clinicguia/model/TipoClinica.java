package br.senai.sp.cfp138.clinicguia.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity // faz dessa classe uma entidade
@Data // gera os getters e setters sozinho
public class TipoClinica {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String descricao;
	private String palavrasChave;

}
