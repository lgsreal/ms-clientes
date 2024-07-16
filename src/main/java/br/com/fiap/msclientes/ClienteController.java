package br.com.fiap.msclientes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/clientes", produces = {"application/json"})
@Tag(name = "api-cliente")
public class ClienteController {
    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Operation(summary = "Retorna todos os clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum cliente encontrado", content = {
                    @Content(schema = @Schema())
            })
    })
    @GetMapping
    public ResponseEntity<List<Cliente>> findAll() {
        List<Cliente> clientes = clienteRepository.findAll();
        if (clientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(clientes);
        }
    }

    @Operation(summary = "Retorna um cliente específico por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum cliente encontrado para o id informado", content = {
                    @Content(schema = @Schema())
            })
    })
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> findById(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Operation(summary = "Grava um cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente gravado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados", content = {
                    @Content(schema = @Schema())
            })
    })
    @PostMapping
    public ResponseEntity<Cliente> insert(@Valid @RequestBody Cliente cliente) {
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return ResponseEntity.created(URI.create(String.format("/clientes/%s", clienteSalvo.getId()))).body(clienteSalvo);
    }

    @Operation(summary = "Atualiza um cliente com base no id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação dos dados", content = {
                    @Content(schema = @Schema())
            })
    })
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        Optional<Cliente> clienteAtual = clienteRepository.findById(id);
        if (clienteAtual.isPresent()) {
            Cliente clienteAtualizado = clienteAtual.get();
            clienteAtualizado.setNome(cliente.getNome());
            clienteAtualizado.setEmail(cliente.getEmail());
            return ResponseEntity.created(URI.create(String.format("/clientes/%s", id))).body(clienteRepository.save(clienteAtualizado));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Exclui um cliente com base no id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso", content = {
                    @Content(schema = @Schema())
            })
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
