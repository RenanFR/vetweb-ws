package com.vetweb.service;

import java.util.List;

import javax.inject.Inject;

import com.vetweb.dao.AnimalDAO;

import com.vetweb.model.Especie;

public class EspecieService implements Service<Especie> {
	
	@Inject
	private AnimalDAO animalDAO;
	
	@Override
	public List<Especie> all() {
		return animalDAO.especies();
	}
	
	@Override
	public void add(Especie especie) {
		animalDAO.salvarEspecie(especie);
	}

	@Override
	public void remove(Long especieId) {
		Especie especie = animalDAO.especiePorId(especieId);
		animalDAO.removerEspecie(especie);
	}

	@Override
	public Especie get(Long id) {
		return animalDAO.especiePorId(id);
	}
	
}
