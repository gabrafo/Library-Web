package dev.gabrafo.libraryweb.features.address;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AddressMapper {

    public AddressDTO toDTO(Address address){
        return AddressDTO.builder().address(address).build();
    }

    public Address toEntity(AddressDTO addressDTO){
        return Address.builder().dto(addressDTO).build();
    }
}