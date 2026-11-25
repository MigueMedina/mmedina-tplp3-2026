package py.edu.uc.lp3.mm.cs2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import py.edu.uc.lp3.mm.cs2.client.ApiClientesProperties;

@SpringBootTest
class Cs2ApplicationTests {

	@Autowired
	private ApiClientesProperties propiedades;

	@Test
	void contextLoads() {
	}

	@Test
	void configuraLasOperacionesYLosCuerposDeA2YA3() {
		assertEquals("municion", propiedades.getA2().getOperationId());
		assertEquals("cargador", propiedades.getA2().getCuerpo().get("cargador"));
		assertEquals("balasPorDisparo", propiedades.getA2().getCuerpo().get("balasPorDisparo"));
		assertEquals("disparos", propiedades.getA2().getCuerpo().get("disparos"));
		assertEquals("danio", propiedades.getA3().getOperationId());
		assertEquals("tirador", propiedades.getA3().getCuerpo().get("tirador"));
		assertEquals("equipoTirador", propiedades.getA3().getCuerpo().get("equipoTirador"));
		assertEquals("vidaObjetivo", propiedades.getA3().getCuerpo().get("vidaObjetivo"));
		assertEquals("danio", propiedades.getA3().getCuerpo().get("danio"));
	}

	@Test
	void lasPropertiesDeProduccionMapeanElCampoDanio() throws IOException {
		Properties configuracion = new Properties();
		try (InputStream entrada = Files.newInputStream(Path.of("src/main/resources/application.properties"))) {
			configuracion.load(entrada);
		}
		assertEquals("danio", configuracion.getProperty("apis.a3.cuerpo.danio"));
		assertNull(configuracion.getProperty("apis.a3.cuerpo.dano"));
		assertEquals("${APIS_CONNECT_TIMEOUT:2s}", configuracion.getProperty("apis.connect-timeout"));
		assertEquals("${APIS_READ_TIMEOUT:5s}", configuracion.getProperty("apis.read-timeout"));
	}

}
