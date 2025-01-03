package dev.gabrafo.libraryweb.features.address;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
@Tag(name = "Endereço", description = "Endpoints para realizar a consulta de CEP usando a API ViaCep")
public class AddressController {

    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @Operation(summary = "Busca um endereço",
            description = "Faz uma requisição à API ViaCep usando o CEP (ZIP) para checar " +
                    "se o endereço existe ou não, caso exista o retorna ao usuário.",
            tags = {"Address"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = AddressDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @GetMapping(value = "/{zip}", produces = "application/json")
    public ResponseEntity<Address> findByZip(@PathVariable String zip) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAddress(zip));
    }
}