package org.sadkowski.library.model;

import lombok.Getter;

@Getter
class BookInLibrary {
    private final Book book;
    private Lend lend;

    public BookInLibrary(Book book){
        this.book=book;
    }
    public void lendWithPersonName(String personName) {
        this.lend = new Lend(personName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ");
        sb.append(book.getTitle());

        sb.append(", Year: ");
        sb.append(book.getYear());

        sb.append(", Author: ");
        sb.append(book.getAuthor());

        return sb.toString();
    }
}
