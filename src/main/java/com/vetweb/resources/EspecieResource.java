package com.vetweb.resources;

import java.net.URI;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.vetweb.model.Especie;
import com.vetweb.service.EspecieService;

@RequestScoped
@Path("especies")
public class EspecieResource {

	private URI uriVetweb = URI.create("/vetweb-api");
	
	private URI uriResource = uriVetweb.relativize(URI.create("/prontuario"));
	
	@Inject
	private EspecieService especieService;
	
	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	public List<Especie> getEspecies() {
		return especieService.all();
	}
	
	@POST
	@Consumes(value = MediaType.APPLICATION_JSON)
	public Response postEspecie(Especie especie) {
		especieService.add(especie);
		return Response.created(URI.create(uriResource.toString() + "/especies/" + especie.getEspecieId())).build();
	}
}
