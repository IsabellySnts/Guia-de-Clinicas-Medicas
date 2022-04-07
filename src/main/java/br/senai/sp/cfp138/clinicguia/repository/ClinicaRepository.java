package br.senai.sp.cfp138.clinicguia.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.senai.sp.cfp138.clinicguia.model.Clinica;

public interface ClinicaRepository extends PagingAndSortingRepository<Clinica, Long> {

}
