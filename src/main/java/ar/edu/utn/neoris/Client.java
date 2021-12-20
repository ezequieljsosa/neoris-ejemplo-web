package ar.edu.utn.neoris;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClient;

public class Client {




    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        WebClient client = WebClient.create("https://cat-fact.herokuapp.com");
        String rta = client.get().uri("/facts/random").retrieve().bodyToMono(String.class).block();
        System.out.println("*********************************");
        JsonNode data = mapper.readValue(rta, JsonNode.class);
        rta = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        System.out.println(rta);
        System.out.println("*********************************");

    }

}
