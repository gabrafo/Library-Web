package dev.gabrafo.libraryweb.features.email;

import lombok.Builder;

@Builder
public record Email(String to, String subject, String body) {
}
