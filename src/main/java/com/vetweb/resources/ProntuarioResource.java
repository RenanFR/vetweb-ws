package com.vetweb.resources;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.vetweb.dao.AnimalDAO;
import com.vetweb.model.Especie;

@Path("prontuario")
public class ProntuarioResource {

	@Inject
	private AnimalDAO animalDAO;
	
	@Path("especies")
	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	public List<Especie> getEspecies() {
		return animalDAO.especies();
	}
	
}
