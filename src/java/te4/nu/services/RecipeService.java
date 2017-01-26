/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package te4.nu.services;

import javax.ejb.EJB;
import javax.json.JsonArray;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nu.te4.Beans.RecipeBean;
import te4.nu.support.User;

/**
 *
 * @author albinarvidsson
 */
@Path("/")
public class RecipeService {

    @EJB
    RecipeBean recipeBean;

    @GET
    @Path("recipe")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecipes(@Context HttpHeaders httpHeaders) {
        //alla ska kunna se den så behöver inte user

        JsonArray data = recipeBean.getRecipes();

        if (data == null) {
            return Response.serverError().build();
        }

        return Response.ok(data).build();
    }

    @GET
    @Path("recipe/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecipe(@PathParam("id") int id, @Context HttpHeaders httpHeaders) {

        JsonArray data = recipeBean.getRecipe(id);

        if (data == null) {
            return Response.serverError().build();
        }

        return Response.ok(data).build();
    }
    
    @GET
    @Path("ingred/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIngred(@PathParam("id") int id, @Context HttpHeaders httpHeaders){
        
        JsonArray data = recipeBean.getIngredient(id);

        if (data == null) {
            return Response.serverError().build();
        }

        return Response.ok(data).build();
    }
    @GET
    @Path("ingred")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIngred(@Context HttpHeaders httpHeaders){
        
        JsonArray data = recipeBean.getIngredients();

        if (data == null) {
            return Response.serverError().build();
        }

        return Response.ok(data).build();
    }
   
    @GET
    @Path("recipeId")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLastId(@Context HttpHeaders httpHeaders) throws Exception{
        JsonArray data = recipeBean.getLastId();

        if (data == null) {
            return Response.serverError().build();
        }

        return Response.ok(data).build();
    }
    
    @POST
    @Path("recipe")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRecipe(String body, @Context HttpHeaders httpHeaders) {
        //måste vara inloggad inloggning kommer snenare
        if (!User.authoricate(httpHeaders)) {
            System.out.println("nu fick du fel här");
            return Response.status(401).build();
        }

        if (!recipeBean.addRecipe(body)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("ingred/{id}")
    public Response addIngredients(@PathParam("id") int id, String body, @Context HttpHeaders httpHeaders) {
        //måste vara inloggad
        if (!User.authoricate(httpHeaders)) {
            return Response.status(401).build();
        }
        if (!recipeBean.addIngredients(id, body)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("recipe/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delRecipe(@PathParam("id") int id, @Context HttpHeaders httpHeaders) {
        //mpste vara inloggad kommer snenare
        if (!User.authoricate(httpHeaders)) {
            return Response.status(401).build();
        }

        if (!recipeBean.delRecipe(id)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok().build();
    }

    @DELETE
    @Path("recipe/i/{id}/{ing}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delIngr(@PathParam("id") int id, @PathParam("ing") String ing, @Context HttpHeaders httpHeaders) {
        //mpste vara inloggad kommer snenare
        if (!User.authoricate(httpHeaders)) {
            return Response.status(401).build();
        }

        if (!recipeBean.delIngr(id, ing)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok().build();
    }

    @PUT
    @Path("recipe")
    @Produces(MediaType.APPLICATION_JSON)
    public Response putRecipe(String body, @Context HttpHeaders httpHeaders) {
        //måste vara inloggad
        if (!User.authoricate(httpHeaders)) {
            return Response.status(401).build();
        }

        if (!recipeBean.putRecipe(body)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok().build();
    }

    @PUT
    @Path("recipe/i")
    @Produces(MediaType.APPLICATION_JSON)
    public Response putIngre(String body, @Context HttpHeaders httpHeaders) {
        //måste vara admin
        if (!recipeBean.putIngre(body)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok().build();
    }
}
