package com.vetweb.resources;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import com.vetweb.model.Especie;

public class ProntuarioTest {
	
	private WebTarget targetVetweb;
	
	@Before
	public void configurarEnderecoRequisicao() {
		Client client = ClientBuilder.newClient();
		targetVetweb = client.target("http://localhost:8080/vetweb-api/");		
	}
	
	@Test
	public void testBuscaTodasAsEspecies() {
		
		String especiesBuscadas = targetVetweb.path("prontuario/especies").request().get(String.class);
		assertTrue(especiesBuscadas.contains("Ephippiorhynchus"));
		
	}
	
	@Test
	public void testAdicionaEspecie() {
		
		Especie especie = new Especie();
		especie.setDescricao("Especie Test");
		Response response = targetVetweb.path("prontuario/especies")
				.request()
				.post(Entity.entity(especie, MediaType.APPLICATION_JSON));
		String locNovaEspecie = response.getHeaderString("Location");
		assertTrue(response.getStatus() == 201);
		assertTrue(!locNovaEspecie.isEmpty());
		
	}
}
