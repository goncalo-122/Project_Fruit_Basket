package org.resource;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.entity.FruitEntity;
import org.service.FruitService;

import java.util.List;

@Path("/fruit")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FruitResource {

    @Inject
    FruitService service;

    // ---------------------------------------------------------
    // POST /fruit
    // ---------------------------------------------------------
    @POST
    @Transactional
    public Response addFruits(List<FruitEntity> fruits) {

        for (FruitEntity f : fruits) {
            if (f.fruitId == null || f.fruitId <= 0) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid fruitId").build();
            }
            if (f.name == null || f.name.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid name").build();
            }
            if (f.quantity < 0) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid quantity").build();
            }
        }

        try {
            service.addFruits(fruits);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // ---------------------------------------------------------
    // GET /fruit
    // ---------------------------------------------------------
    @GET
    public Response getFruits() {
        return Response.ok(service.getFruits()).build();
    }

    // ---------------------------------------------------------
    // GET /fruit/{id}
    // ---------------------------------------------------------
    @GET
    @Path("/{id}")
    public Response getFruit(@PathParam("id") Long id) {
        FruitEntity fruit = service.getFruit(id);
        if (fruit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(fruit).build();
    }

    // ---------------------------------------------------------
    // PUT /fruit/{id}/quantity
    // ---------------------------------------------------------
    @PUT
    @Path("/{id}/quantity")
    @Transactional
    public Response updateQuantity(@PathParam("id") Long id, FruitEntity body) {

        if (body.quantity < 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Quantity cannot be negative").build();
        }

        boolean updated = service.updateFruitStock(id, body.quantity);

        if (!updated) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Fruit not found").build();
        }

        return Response.ok().build();
    }

    // ---------------------------------------------------------
    // DELETE /fruit/delete/{id}
    // ---------------------------------------------------------
    @DELETE
    @Path("/delete/{id}")
    @Transactional
    public Response deleteFruit(@PathParam("id") Long id) {

        boolean deleted = service.deleteFruit(id);

        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
