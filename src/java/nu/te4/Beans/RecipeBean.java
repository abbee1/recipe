/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.Beans;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import te4.nu.support.ConnectionFactory;

/**
 *
 * @author albinarvidsson
 */
@Stateless
public class RecipeBean {

    public JsonArray getRecipes() {
        try {
            Connection connection = ConnectionFactory.make("Server");
            String sql = "SELECT * FROM `recipes`";
            Statement stmt = connection.createStatement();
            ResultSet data = stmt.executeQuery(sql);

            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                int id = data.getInt("r_id");
                String name = data.getString("name");
                String cat = data.getString("category");
                String user = data.getString("username");

                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("id", id)
                        .add("titel", name)
                        .add("category", cat)
                        .add("author", user).build());
            }
            connection.close();
            return jsonArrayBuilder.build();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

    public JsonArray getRecipe(int id) {
        try {
            Connection connection = ConnectionFactory.make("Server");
            String sql = "SELECT * FROM `vy` WHERE r_id =" + id;
            Statement stmt = connection.createStatement();
            ResultSet data = stmt.executeQuery(sql);

            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                String name = data.getString("name");
                String instru = data.getString("instructions");
                String cat = data.getString("category");
                String user = data.getString("username");

                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("titel", name)
                        .add("instructions", instru)
                        .add("category", cat)
                        .add("author", user).build());
            }
            connection.close();
            return jsonArrayBuilder.build();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public JsonArray getIngredient(int id) {
        try {
            Connection connection = ConnectionFactory.make("Server");
            String sql = "SELECT * FROM `ingredientsvy` WHERE r_id =" + id;
            Statement stmt = connection.createStatement();
            ResultSet data = stmt.executeQuery(sql);

            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                String ingredients = data.getString("name");
                String amount = data.getString("amount");

                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("id", id)
                        .add("ingredients", ingredients)
                        .add("amount", amount).build());
            }
            connection.close();
            return jsonArrayBuilder.build();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
    public JsonArray getIngredients() {
        try {
            Connection connection = ConnectionFactory.make("Server");
            String sql = "SELECT * FROM `ingredientsvy` WHERE 1";
            Statement stmt = connection.createStatement();
            ResultSet data = stmt.executeQuery(sql);

            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                int r_id = data.getInt("r_id");
                String ingredients = data.getString("name");
                String amount = data.getString("amount");

                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("id", r_id)
                        .add("ingredients", ingredients)
                        .add("amount", amount).build());
            }
            connection.close();
            return jsonArrayBuilder.build();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public boolean addRecipe(String body) {
        try {
            JsonReader jsonReader = Json.createReader((new StringReader(body)));
            JsonObject data = jsonReader.readObject();
            jsonReader.close();
            String name = data.getString("name");
            String instru = data.getString("instructions");
            int cat = data.getInt("category");
            int aut = data.getInt("author");

            Connection connection = ConnectionFactory.make("Server");
            String sql = "INSERT INTO `recipe` VALUES (NULL, ?, ?, ?, ?);";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, name);
            stmt.setString(2, instru);
            stmt.setInt(3, cat);
            stmt.setInt(4, aut);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error post: " + e.getMessage());
        }
        return false;
    }

    public boolean addIngredients(int id, String body) {
        try {
            JsonReader jsonReader = Json.createReader((new StringReader(body)));
            JsonObject data = jsonReader.readObject();
            jsonReader.close();

            String amount = data.getString("amount");
            int i_id = data.getInt("i_id");

            Connection connection = ConnectionFactory.make("Server");
            String sql = "INSERT INTO `amount` VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, id);
            stmt.setString(2, amount);
            stmt.setInt(3, i_id);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error post: " + e.getMessage());
            return false;
        }
    }

    public boolean delRecipe(int id) {
        try {
            Connection connection = ConnectionFactory.make("Server");
            String sql = "DELETE FROM `recipe` WHERE `recipe`.`r_id` = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error del: " + e.getMessage());
            return false;
        }
    }

    public boolean delIngr(int id, String ing) {
        try {
            Connection connection = ConnectionFactory.make("Server");
            String sql = "DELETE FROM `amount` WHERE `r_id` = ? AND i_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setString(2, ing);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error del: " + e.getMessage());
            return false;
        }
    }

    public boolean putRecipe(String body) {
        try {
            JsonReader jsonReader = Json.createReader(new StringReader(body));
            JsonObject data = jsonReader.readObject();
            jsonReader.close();
            int id = data.getInt("r_id");
            String name = data.getString("name");
            String instru = data.getString("instructions");
            int cat = data.getInt("category");
            int aut = data.getInt("author");

            Connection connection = ConnectionFactory.make("Server");
            String sql = "UPDATE recipe SET name = ? , instructions = ? , category = ? , author = ? WHERE r_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, instru);
            stmt.setInt(3, cat);
            stmt.setInt(4, aut);
            stmt.setInt(5, id);
            stmt.executeUpdate();
            connection.close();
            return true;

        } catch (Exception e) {
            System.out.println("Error put: " + e.getMessage());
            return false;
        }
    }

    public boolean putIngre(String body) {
        try {
            JsonReader jsonReader = Json.createReader(new StringReader(body));
            JsonObject data = jsonReader.readObject();
            jsonReader.close();
            int id = data.getInt("r_id");
            String amount = data.getString("amount");
            int i_id = data.getInt("i_id");

            Connection connection = ConnectionFactory.make("Server");
            String sql = "UPDATE amount SET amount =?, i_id=? WHERE r_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, amount);
            stmt.setInt(4, i_id);
            stmt.setInt(5, id);
            stmt.executeUpdate();
            connection.close();
            return true;

        } catch (Exception e) {
            System.out.println("Error put: " + e.getMessage());
            return false;
        }
    }

    public JsonArray getLastId() throws Exception {
        try {
            Connection connection = ConnectionFactory.make("Server");
            String sql = "SELECT r_id FROM recipe ORDER BY r_id DESC LIMIT 1";
            Statement stmt = connection.createStatement();
            ResultSet data = stmt.executeQuery(sql);

            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                int id = data.getInt("r_id");

                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("id", id).build());
            }
            connection.close();
            return jsonArrayBuilder.build();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
}
