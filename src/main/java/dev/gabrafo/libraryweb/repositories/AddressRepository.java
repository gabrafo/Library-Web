package dev.gabrafo.libraryweb.repositories;

import dev.gabrafo.libraryweb.domain.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByZipCode(String zip);
}