package ar.edu.utn.neoris;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class Server {

    public static void main(String[] args) throws Exception {

        final List<Drink> bebidas = new ArrayList<>();
        bebidas.add(new Drink(1, "café", "un cafe", 150, "caliente"));
        bebidas.add(new Drink(2, "coca", "una coca común", 100, "frio"));
        bebidas.add(new Drink(3, "té", "una te ", 100, "caliente"));
        bebidas.add(new Drink(4, "mate cocido", "una mate cocido ", 90, "caliente"));
        bebidas.add(new Drink(5, "café con leche", "un buen café ", 250, "caliente"));
        bebidas.add(new Drink(6, "7up", "una seven!", 110, "frio"));

        ObjectMapper mapper = new ObjectMapper();
        port(4567);
        get("/drink", (req, res) -> {
            System.out.println(req.queryParams());
            List<Drink> bebidasARetornar = bebidas;
            if (req.queryParams().contains("tipo")) {
                bebidasARetornar = bebidasARetornar.stream().filter(x -> x.getTipo().equals(req.queryParams("tipo"))
                    ).collect(Collectors.toList());
            }
            if (req.queryParams().contains("text")) {
                bebidasARetornar = bebidasARetornar.stream().filter(x -> x.getDescripcion().contains(req.queryParams("text").toString())
                    ).collect(Collectors.toList());
            }
            return bebidasARetornar;
        },mapper::writeValueAsString);

        post("/drink", (req, res) -> {
            Drink drink =  mapper.readValue( req.body() , Drink.class );
            int maxId = bebidas.stream().mapToInt(x -> x.getId()).max().getAsInt();
            drink.setId( maxId + 1  );
            bebidas.add(drink);
            res.status(201);
            return drink;
        },mapper::writeValueAsString);

        delete("/drink/:id", (req, res) -> {
            List<Drink> bebidas2 = bebidas.stream().filter(x -> !x.getId().toString().equals(req.params("id"))).collect(Collectors.toList());
            bebidas.removeAll(bebidas);
            bebidas.addAll(bebidas2);
            return "removed";
        },mapper::writeValueAsString);

        put("/drink/:id", (req, res) -> {
            List<Drink> bebidas2 = bebidas.stream().filter(x -> !x.getId().toString().equals(req.params("id"))).collect(Collectors.toList());
            Drink drink =  mapper.readValue( req.body() , Drink.class );
            bebidas2.add(drink);
            bebidas.removeAll(bebidas);
            bebidas.addAll(bebidas2);
            return drink;
        },mapper::writeValueAsString);

    }


}
