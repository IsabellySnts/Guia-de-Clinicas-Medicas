package br.senai.sp.cfp138.clinicguia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import br.senai.sp.cfp138.clinicguia.util.HashUtil;
import lombok.Data;

@Data
@Entity
public class Administrador {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty
	private String nome;
	@Column(unique = true)
	@Email
	private String email;
	@NotEmpty
	private String senha;
	
	
	//metodo para setar a senha aplicando o hash
	public void setSenha(String senha) {

		//abrindo o hash e setando a senha no objeto
		this.senha = HashUtil.hash256(senha);
		
	}
	
	//metodo para setar a senha sem aplicar o hash
	public void setSenhaComHash(String hash) {
		
		//setando o hash na senha
		this.senha = hash;
	}
}


