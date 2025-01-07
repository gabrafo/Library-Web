package dev.gabrafo.libraryweb.features.book;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public class BookMapper {

    public BookDTO toDTO(Book book){
        return BookDTO.builder().book(book).build();
    }

    public Book toEntity(BookDTO BookDTO){
        return Book.builder().dto(BookDTO).build();
    }
}