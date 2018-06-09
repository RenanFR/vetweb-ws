/**
 * @author renanfr
 *
 */
package com.vetweb.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.vetweb.model.Prontuario;

@Repository
public class RelatorioDAO {
	
	@PersistenceContext(unitName = "vetwebpu")
	private EntityManager entityManager;
	
	public double contasAReceber() {
		List<Prontuario> prontuarios = entityManager
				.createQuery("SELECT p FROM Prontuario p", Prontuario.class)
				.getResultList();
		double contasReceberAtendimentos = prontuarios.stream()
				.flatMap(prontuario -> prontuario.getAtendimentos().stream())
				.filter(atendimento -> !atendimento.isPago())
				.mapToDouble(atendimento -> atendimento.getTipoDeAtendimento().getCusto().doubleValue())
				.sum();
		double contasReceberVacinas = prontuarios.stream()
				.flatMap(prontuario -> prontuario.getVacinas().stream())
				.filter(vacina -> !vacina.isPago())
				.mapToDouble(vacina -> vacina.getVacina().getPreco().doubleValue())
				.sum();
		return Double.sum(contasReceberAtendimentos, contasReceberVacinas);
	}

}
