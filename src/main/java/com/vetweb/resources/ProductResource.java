package com.vetweb.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.vetweb.models.Product;

@Path("/products")
public class ProductResource
{

   // just an example to simulate a database
   private static List<Product> products = new ArrayList<Product>(Arrays.asList(new Product(
         "jaxrs-project"), new Product("spring-project")));

   @Path("/")
   @GET
   @Produces(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
   public List<Product> exampleGet()
   {
      return products;
   }

   @Path("/")
   @POST
   @Produces(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
   public Response create(Product product) throws URISyntaxException
   {
      products.add(product);
      return Response.created(new URI("products")).build();
   }
}
