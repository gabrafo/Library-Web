package dev.gabrafo.libraryweb.domain.entities;

import dev.gabrafo.libraryweb.domain.dtos.AddressDTO;
import dev.gabrafo.libraryweb.domain.enums.FederalUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "tb_address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Column(nullable = false)
    @NotEmpty()
    private String zipCode;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String neighbourhood;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FederalUnit federalUnit;

    @Builder
    public Address(AddressDTO dto){
        this.zipCode = dto.zipCode();
        this.street = dto.street();
        this.neighbourhood = dto.neighbourhood();
        this.city = dto.city();
        this.federalUnit = FederalUnit.valueOf(dto.federalUnit());
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(obj, this); // Compara os objetos e não endereços de memória
    }
}