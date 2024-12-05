package dev.gabrafo.libraryweb.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Column(name = "zip_code", nullable = false)
    @NotEmpty()
    private String zipCode;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "neighbourhood", nullable = false)
    private String neighbourhood;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "federal_unit", nullable = false)
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