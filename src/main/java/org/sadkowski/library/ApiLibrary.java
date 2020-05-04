package org.sadkowski.library;

public interface ApiLibrary {

    void add(String title, Integer year, String author);

    void delete(int id);

    void list();

    void lend(Integer bookId, String personalName);

    void findById(Integer bookId);

    void findByAttributes(String title, Integer year, String author);

}
