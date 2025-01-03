package dev.gabrafo.libraryweb.features.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record BookAdditionDTO(@NotEmpty String isbn, @Min(1) int units) {
}
