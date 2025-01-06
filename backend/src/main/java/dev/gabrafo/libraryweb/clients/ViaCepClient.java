package dev.gabrafo.libraryweb.clients;

import dev.gabrafo.libraryweb.features.address.AddressDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "https://viacep.com.br/ws/", name = "viacep")
@Tag(name = "ViaCep", description = "API para a qual enviamos uma requisição buscando os dados de endereço.")
public interface ViaCepClient {

    @GetMapping("/{zip}/json")
    AddressDTO findAddressByZipCode(@PathVariable String zip);
}