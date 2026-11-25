package py.edu.uc.lp3.mm.cs2.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientHttpConfig {

    @Bean
    public RestClient.Builder restClientBuilder(ApiClientesProperties propiedades) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(propiedades.getConnectTimeout());
        factory.setReadTimeout(propiedades.getReadTimeout());
        return RestClient.builder().requestFactory(new BufferingClientHttpRequestFactory(factory));
    }
}
