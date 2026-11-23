package py.edu.uc.lp3.mm.cs2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "CS2 API - POO-08/POO-09",
        version = "1.0.0",
        description = "API REST de Counter-Strike 2 con CRUD persistente, simulación de disparo "
                + "y escenario coordinado que consume las reglas de A2 y A3."))
public class Cs2Application {

	public static void main(String[] args) {
		SpringApplication.run(Cs2Application.class, args);
	}

}
