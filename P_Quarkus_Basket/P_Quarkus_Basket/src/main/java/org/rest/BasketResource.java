package org.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.entity.BasketEntity;
import org.service.BasketService;
import jakarta.transaction.Transactional;
import java.util.List;

import org.dto.BasketDTO;
import org.dto.BasketItemDTO;

@Path("/basket")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BasketResource {

    @Inject
    BasketService basketService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<BasketEntity> getBaskets() {
        return basketService.getBaskets();
    }

    @GET
    @Path("/{basketId}")
    public BasketDTO getBasket(@PathParam("basketId") Long basketId) {
        return basketService.getBasket(basketId);
    }


    @POST
    public Response addItem(BasketItemDTO newItem) {
        BasketItemDTO savedItem = basketService.addItemToBasket(newItem);
        return Response.status(Response.Status.CREATED).entity(savedItem).build();
    }

    @DELETE
    @Path("/delete/{basketId}")
    @Transactional
    public Response deleteBasket(@PathParam("basketId") long id){
        boolean deleteBasket = basketService.deleteBasket(id);
        if (deleteBasket) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
