package dev.gabrafo.libraryweb.domain.mappers;

import dev.gabrafo.libraryweb.domain.dtos.AddressDTO;
import dev.gabrafo.libraryweb.domain.entities.Address;

public abstract class AddressMapper {

    public AddressDTO toDTO(Address address){
        return AddressDTO.builder().address(address).build();
    }

    public Address toEntity(AddressDTO addressDTO){
        return Address.builder().dto(addressDTO).build();
    }
}