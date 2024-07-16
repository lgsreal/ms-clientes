package br.com.fiap.msclientes;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Microsserviço de clientes", version = "1.0", description = "Sistema de Gerenciamento de Pedidos"))
public class MsClientesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsClientesApplication.class, args);
    }

}
