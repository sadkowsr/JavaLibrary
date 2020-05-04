package org.sadkowski.library.model;

import org.sadkowski.library.ApiLibrary;

import java.util.HashMap;
import java.util.Map;

public class LibraryHashMap implements ApiLibrary {

    private final Map<Integer, BookInLibrary> books;

    public LibraryHashMap() {
        books = new HashMap<>();
    }

    @Override
    public void add(String title, Integer year, String author) {
        if (title == null || year == null || author == null || title.trim().isEmpty() || author.trim().isEmpty()) {
            System.out.println("It could not add new book to library");
            return;
        }

        final Book book = new Book(title,year,author);
        final BookInLibrary existBookInLibrary =books.get(book.hashCode());
        if(existBookInLibrary!=null){
            System.out.println("It can not add new book. Book is exist in Library. Id: " + book.hashCode() + ", " + existBookInLibrary.toString());
            return;
        }
        final BookInLibrary bookInLibrary = new BookInLibrary(book);

        books.put(book.hashCode(), bookInLibrary);
        System.out.println("Added new book. Id: " + book.hashCode() + ", " + bookInLibrary.toString());
    }


    @Override
    public void delete(int id) {
        final BookInLibrary bookInLibrary = books.get(id);
        if (bookInLibrary == null) {
            System.out.println("Not found book with id: " + id);
            return;
        }

        if (bookInLibrary.getLend() != null) {
            System.out.println("Found book has been lent. Book with id: " + id + " can not to be deleted");
            return;
        }
        books.remove(id);
        System.out.println("Deleted book with id: " + id);
    }

    @Override
    public void list() {
        System.out.println("Books in library:");

        final long total = books.size();

        final long countLend = books.entrySet()
                .stream()
                .filter(e -> e.getValue().getLend() != null)
                .count();

        System.out.println("Total number of books: " + total + ", number of books lend: " + countLend);
        books.entrySet()
                .stream()
                .forEach(e -> System.out.println("Id: " + e.getKey() + ", " + e.getValue()));
    }

    @Override
    public void lend(Integer bookId, String personalName) {
        final BookInLibrary bookInLibrary = books.get(bookId);
        if (bookInLibrary == null) {
            System.out.println("It can not find book: " + bookId + " to lend");
            return;
        }

        if (bookInLibrary.getLend() != null) {
            System.out.println("It can not lend book with id: " + bookId + " has been lent by " + bookInLibrary.getLend().getPersonName());
            return;
        }
        bookInLibrary.lendWithPersonName(personalName);
        System.out.println("Lend book: Id: " + bookId +", "+ bookInLibrary.toString());
    }

    @Override
    public void findById(Integer bookId) {
        final BookInLibrary bookInLibrary = books.get(bookId);
        if (bookInLibrary == null) {
            System.out.println("Not found a book with id:" + bookId);
            return;
        }
        System.out.println("Detailed info about book with id: " + bookId);
        System.out.println("ID: " + bookId + ", " + bookInLibrary.toString() + ", "
                + (bookInLibrary.getLend() != null ? "Book is lend by " + bookInLibrary.getLend().getPersonName() : "Is ready to lend"));
    }

    @Override
    public void findByAttributes(final String title, final Integer year, final String author) {
        final StringBuilder sb = new StringBuilder("Result of search");

        if (title != null) {
            sb.append(" title: \"" + title + "\"");
        }

        if (year != null) {
            sb.append(" year: " + year);
        }

        if (author != null) {
            sb.append(" author: \"" + author+"\"");
        }
        System.out.println(sb);

        books.entrySet()
                .stream()
                .filter(e ->
                        (title == null || e.getValue().getBook().getTitle().toLowerCase().contains(title.toLowerCase()))
                                &&(year == null || e.getValue().getBook().getYear().compareTo(year)==0)
                               &&  (author == null || e.getValue().getBook().getAuthor().toLowerCase().contains(author.toLowerCase()))

                )
                .forEach(e -> System.out.println("Id: " + e.getKey() + ", " + e.getValue()));
    }
}
