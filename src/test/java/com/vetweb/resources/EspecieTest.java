package com.vetweb.resources;

import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Before;
import org.junit.Test;

import com.vetweb.model.Especie;


public class EspecieTest {
	
	private ResteasyWebTarget targetVetweb;
	
	@Before
	public void configurarEnderecoRequisicao() {
		ResteasyClient client = new ResteasyClientBuilder().build();
		targetVetweb = client.target("http://localhost:8080/vetweb-api/");		
	}
	
	@Test
	public void testBuscaTodasAsEspecies() {
		
		String especiesBuscadas = targetVetweb.path("especies").request().get(String.class);
		assertTrue(especiesBuscadas.contains("Especie 1"));
		
	}
	
	@Test
	public void testAdicionaEspecie() {
		
		Especie especie = new Especie();
		especie.setDescricao("EspecieTest");
		Entity<Especie> entity = Entity.entity(especie, MediaType.APPLICATION_JSON);
		Response response = targetVetweb.path("especies")
				.request()
				.post(entity);
		String locNovaEspecie = response.getHeaderString("Location");
		System.out.println(response.getStatus());
		assertTrue(response.getStatus() == 201);
		assertTrue(!locNovaEspecie.isEmpty());
		
	}
}
