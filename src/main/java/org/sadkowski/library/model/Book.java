package org.sadkowski.library.model;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
class Book {

    private final String title;
    private final Integer year;
    private final String author;

    @Override
    public int hashCode() {
        return Objects.hash(title, year, author);
    }

}
