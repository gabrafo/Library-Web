package dev.gabrafo.libraryweb.domain.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.gabrafo.libraryweb.domain.entities.Address;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record AddressDTO(
        @NotBlank @JsonProperty("cep") String zipCode,
        @NotBlank @JsonProperty("logradouro") String street,
        @NotBlank @JsonProperty("bairro") String neighbourhood,
        @NotBlank @JsonProperty("localidade") String city,
        @NotBlank @JsonProperty("uf") String federalUnit
) {

    @Builder
    public AddressDTO(Address address) {
        this(
                address.getZipCode(),
                address.getStreet(),
                address.getNeighbourhood(),
                address.getCity(),
                String.valueOf(address.getFederalUnit())
        );
    }
}