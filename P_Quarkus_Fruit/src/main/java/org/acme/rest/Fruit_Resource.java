package org.acme.rest;

import entity.Fruit_Entity;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.Fruit_Service;

import java.util.List;


@Path("/fruit")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Fruit_Resource {

    @Inject
    Fruit_Service fruitService;

    @POST
    @Transactional
    public Response addFruits(@Valid List<Fruit_Entity> fruits) {
        fruitService.addFruits(fruits);
        return Response.status(Response.Status.CREATED).entity(fruits).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Fruit_Entity> getFruits() {
        return fruitService.getFruits();
    }

    @Path("/{fruitId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Fruit_Entity getFruit(@PathParam("fruitId") long id) {
        Fruit_Entity fruit = fruitService.getFruit(id);
        if (fruit == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return fruit;
    }


    @PUT
    @Transactional
    @Path("/{fruitId}/quantity")
    public Response updateFruit(@PathParam("fruitId") long id, @Valid Fruit_Entity updateFruit) {
        boolean updatedFruit = fruitService.updateFruit(id, updateFruit);

        if (!updatedFruit) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("/delete/{fruitId}")
    @Transactional
    public Response deleteFruit(@PathParam("fruitId") long id) {

        boolean deletedFruit = fruitService.deleteFruit(id);
        if (deletedFruit) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}