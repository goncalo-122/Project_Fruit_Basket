package org.client;

import org.dto.FruitDTO;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "org.client.FruitClient")
@Path("/fruit")
public interface FruitClient {

    @GET
    @Path("/{fruitId}")
    @Produces(MediaType.APPLICATION_JSON)
    FruitDTO getFruitById(@PathParam("fruitId") Long id);

    @PUT
    @Path("/{fruitId}/quantity")
    void updateFruitQuantity(
            @PathParam("fruitId") Long id,
            @QueryParam("newQuantity") int newQuantity
    );
}