package dev.gabrafo.libraryweb.features.book;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    Book fromResponseToEntity(BookResponseDTO dto);

    Book fromRequestToEntity(BookRequestDTO dto);

    BookRequestDTO toRequestDTO(Book book);

    BookResponseDTO toResponseDTO(Book book);
}