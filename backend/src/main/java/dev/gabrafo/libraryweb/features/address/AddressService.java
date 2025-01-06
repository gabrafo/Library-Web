package dev.gabrafo.libraryweb.features.address;

import dev.gabrafo.libraryweb.clients.ViaCepClient;
import dev.gabrafo.libraryweb.errors.exceptions.ClientUnavailableException;
import dev.gabrafo.libraryweb.errors.exceptions.InvalidEntryException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AddressService {
    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

    private final AddressRepository repository;

    private final ViaCepClient viaCepClient;

    private final Validator validator;

    private final AddressMapper addressMapper;

    public AddressService(AddressRepository repository, ViaCepClient viaCepClient, Validator validator, AddressMapper addressMapper) {
        this.repository = repository;
        this.viaCepClient = viaCepClient;
        this.validator = validator;
        this.addressMapper = addressMapper;
    }

    public Address findAddress(String zip) {

        // Tira qualquer caractere que não for um número
        zip = zip.replaceAll("\\D", "");

        if (zip.length()!=8) throw new InvalidEntryException("CEP inválido");

        // Tenta fazer a requisição e se ela der certo salva o endereço no banco de dados e retorna os valores cadastrados
        try{
            AddressDTO addressDTO = viaCepClient.findAddressByZipCode(zip);

            // Valida o AddressDTO e retorna uma exceção com todos os campos inválidos
            Set<ConstraintViolation<AddressDTO>> violations = validator.validate(addressDTO);
            if (!violations.isEmpty()) {
                String errorMessage = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(", "));
                logger.error("Dados inválidos recebidos da API ViaCep: {}", errorMessage);
                throw new InvalidEntryException("Dados inválidos! Erros: " + errorMessage);
            }

            Address address = addressMapper.toEntity(addressDTO);
            repository.save(address);
            logger.info("Endereço salvo com sucesso: {}", address);
            return address;
        } catch (HttpClientErrorException.BadRequest e){
            logger.error("CEP não encontrado: {}", zip, e);
            throw new InvalidEntryException("CEP não encontrado");
        } catch (FeignException.FeignClientException e){
            logger.error("Erro ao consultar a API ViaCep para o CEP: {}", zip, e);
            throw new ClientUnavailableException("Erro ao consultar o CEP");
        }
    }
}
