import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sadkowski.library.ApiLibrary;
import org.sadkowski.library.model.LibraryHashMap;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LibraryTest {

    private PrintStream originalSystemOut;
    private ByteArrayOutputStream systemOutContent;
    private ApiLibrary library;
    private final static String CANNOT_ADD_BOOK_INFO = "It could not add new book to library\r\n";

    @BeforeEach
    void redirectOut(){
        saveOriginalSystemOut();
        setupTestSystemOut();
        library = new LibraryHashMap();
    }

    @Test
    void libraryShouldBeEmptyAtTheBeginning() {
        library.list();
        assertEquals("Books in library:\r\nTotal number of books: 0, number of books lend: 0\r\n", systemOutContent.toString());
    }

    @Test
    void shouldBeIndeticalBookWhenTitleYearAuthorAreTheSame(){

    }

    @Test
    void afterInsertNewBookShouldPrintBookWithRightId(){
        int expectedId = Objects.hash("Pan Tadeusz",1825,"Adam Mickiewicz");
        library.add("Pan Tadeusz", 1825, "Adam Mickiewicz");
        assertEquals("Added new book. Id: "+expectedId+", Title: Pan Tadeusz, Year: 1825, Author: Adam Mickiewicz\r\n", systemOutContent.toString());
    }

    @Test
    void shouldShowInfoAfterTryToAddIdenticalBook(){
        int expectedId = Objects.hash("Pan Tadeusz",1825,"Adam Mickiewicz");
        library.add("Pan Tadeusz", 1825, "Adam Mickiewicz");

        setupTestSystemOut();
        library.add("Pan Tadeusz", 1825, "Adam Mickiewicz");
        assertEquals("It can not add new book. Book is exist in Library. Id: "+expectedId+", Title: Pan Tadeusz, Year: 1825, Author: Adam Mickiewicz\r\n", systemOutContent.toString());
    }

    @Test
    void ShouldPrintInfoAfterInsertBookWithNullTitle(){
        library.add(null, 1825, "Adam Mickiewicz");
        assertEquals(CANNOT_ADD_BOOK_INFO, systemOutContent.toString());
    }

    @Test
    void shouldPrintInfoAfterInsertBookWithNullYear(){
        library.add("Pan Tadeusz", null, "Adam Mickiewicz");
        assertEquals(CANNOT_ADD_BOOK_INFO, systemOutContent.toString());
    }

    @Test
    void shouldPrintInfoAfterInsertBookWithNullAuthor(){
        library.add("Pan Tadeusz", 2015, null);
        assertEquals(CANNOT_ADD_BOOK_INFO, systemOutContent.toString());
    }

    @Test
    void shouldPrintInfoInsertBookWithEmptyTitle(){
        library.add("", 2015, "Adam Mickiewicz");
        assertEquals(CANNOT_ADD_BOOK_INFO, systemOutContent.toString());
    }

    @Test
    void shouldPrintInfoAfterInsertBookWithEmptyAuthor(){
        library.add("Pan Tadeusz", 2015, " ");
        assertEquals(CANNOT_ADD_BOOK_INFO, systemOutContent.toString());
    }

    @Test
    void shouldBookApearInLibraryAfterInsertBook(){
        library.add("Pan Tadeusz", 2015, "Adam Mickiewicz");
        setupTestSystemOut();
        library.list();
        assertEquals("Books in library:\r\n" +
                "Total number of books: 1, number of books lend: 0\r\n" +
                "Id: -267431131, Title: Pan Tadeusz, Year: 2015, Author: Adam Mickiewicz\r\n", systemOutContent.toString());
    }

    @Test
    void shouldDeleteBookWhichIsNotLend(){
        library.add("Pan Tadeusz", 2015, "Adam Mickiewicz");
        setupTestSystemOut();

        int id = Objects.hash("Pan Tadeusz", 2015,"Adam Mickiewicz");
        library.delete(id);

        assertEquals("Deleted book with id: "+id+"\r\n", systemOutContent.toString());
    }

    @Test
    void shouldPrintInfoWhenDeletingBookIsLend(){
        library.add("Pan Tadeusz", 2015, "Adam Mickiewicz");
        int id = Objects.hash("Pan Tadeusz", 2015,"Adam Mickiewicz");
        library.lend(id,"Jan Kowalski");

        setupTestSystemOut();

        library.delete(id);

        assertEquals("Found book has been lent. Book with id: -267431131 can not to be deleted\r\n", systemOutContent.toString());
    }


    @Test
    void shouldPrintAllDetailedInformationWhenBookFound(){
        library.add("Pan Tadeusz", 2015, "Adam Mickiewicz");
        int id = Objects.hash("Pan Tadeusz", 2015,"Adam Mickiewicz");

        setupTestSystemOut();

        library.findById(id);

        assertEquals("Detailed info about book with id: -267431131\r\n"
       + "ID: -267431131, Title: Pan Tadeusz, Year: 2015, Author: Adam Mickiewicz, Is ready to lend\r\n", systemOutContent.toString());
    }

    @Test
    void shouldPrintInfoWhenBookNotFound(){
        library.findById(5);

        assertEquals("Not found a book with id:5\r\n", systemOutContent.toString());
    }

    @Test
    void shouldShowInfoWhenBookWithIdNotExist(){
        library.delete(0);
        assertEquals("Not found book with id: 0\r\n", systemOutContent.toString());
    }

    @Test
    void shouldShowStatusAfterLend(){
        library.add("Pan Tadeusz", 2015, "Adam Mickiewicz");
        int id = Objects.hash("Pan Tadeusz", 2015,"Adam Mickiewicz");
        setupTestSystemOut();

        library.lend(id,"Jan Kowalski");

        assertEquals("Lend book: Id: "+id+", Title: Pan Tadeusz, Year: 2015, Author: Adam Mickiewicz\r\n", systemOutContent.toString());
    }

    @Test
    void shouldAvoidLendWhenBookIsLent(){
        library.add("Pan Tadeusz", 2015, "Adam Mickiewicz");
        int id = Objects.hash("Pan Tadeusz", 2015,"Adam Mickiewicz");
        library.lend(id,"Jan Kowalski");
        this.setupTestSystemOut();

        library.lend(id,"Jan Kowalski");

        assertEquals("It can not lend book with id: "+id+" has been lent by Jan Kowalski\r\n", systemOutContent.toString());
    }

    @Test
    void shouldAvoidLendWhenBookIsNotFound(){
        library.lend(0,"Jan Kowalski");

        assertEquals("It can not find book: 0 to lend\r\n", systemOutContent.toString());
    }

    @Test
    void shouldShowStatusWhenSearchInEmptyLibrary(){
        library.findByAttributes("123",123,"123");

        assertEquals("Result of search title: \"123\" year: 123 author: \"123\"\r\n", systemOutContent.toString());
    }

    @Test
    void shouldFoundBookByThreeAtributesExaclyLikeInLibrary(){
        library.add("Pan Tadeusz", 2015, "Adam Mickiewicz");
        setupTestSystemOut();

        library.findByAttributes("Pan Tadeusz",2015,"Adam Mickiewicz");

        assertEquals(
                "Result of search title: \"Pan Tadeusz\" year: 2015 author: \"Adam Mickiewicz\"\r\n"+
                        "Id: -267431131, Title: Pan Tadeusz, Year: 2015, Author: Adam Mickiewicz\r\n",
                systemOutContent.toString());
    }

    @Test
    void shouldFoundBookByThreeAtributesWithDifferentSizeCase(){
        library.add("Pan Tadeusz", 2015, "Adam Mickiewicz");
        setupTestSystemOut();

        library.findByAttributes("Pan Tadeusz".toLowerCase(),2015,"Adam Mickiewicz".toUpperCase());

        assertEquals(
                "Result of search title: \"pan tadeusz\" year: 2015 author: \"ADAM MICKIEWICZ\"\r\n"+
                        "Id: -267431131, Title: Pan Tadeusz, Year: 2015, Author: Adam Mickiewicz\r\n",
                systemOutContent.toString());
    }

    @Test
    void shouldShowAllBooksUsingAllNullArguments(){
        library.add("Pan Tadeusz", 2015, "Adam Mickiewicz");
        setupTestSystemOut();

        library.findByAttributes(null,null,null);

        assertEquals(
                "Result of search\r\n"+
                        "Id: -267431131, Title: Pan Tadeusz, Year: 2015, Author: Adam Mickiewicz\r\n",
                systemOutContent.toString());
    }

    @Test
    void shouldNotFindAnyBooksUsingWrongArguments(){
        library.add("Pan Tadeusz", 2015, "Adam Mickiewicz");
        setupTestSystemOut();

        library.findByAttributes("Pan Sławomir",2015,"Adam Mickiewicz");

        assertEquals(
                "Result of search title: \"Pan Sławomir\" year: 2015 author: \"Adam Mickiewicz\"\r\n",
                systemOutContent.toString());
    }


    @Test
    void shouldFoundBooksUsingOnlyTitleArgument(){
        library.add("Pan Tadeusz", 2015, "Adam Mickiewicz");
        setupTestSystemOut();

        library.findByAttributes("Pan Tadeusz",null,null);

        assertEquals(
                "Result of search title: \"Pan Tadeusz\"\r\n"+
                        "Id: -267431131, Title: Pan Tadeusz, Year: 2015, Author: Adam Mickiewicz\r\n",
                systemOutContent.toString());
    }

    @Test
    void shouldFoundBooksUsingOnlyYearArgument(){
        library.add("Pan Tadeusz", 2015, "Adam Mickiewicz");
        setupTestSystemOut();

        library.findByAttributes(null,2015,null);

        assertEquals(
                "Result of search year: 2015\r\n"+
                        "Id: -267431131, Title: Pan Tadeusz, Year: 2015, Author: Adam Mickiewicz\r\n",
                systemOutContent.toString());
    }

    @Test
    void shouldFoundBooksUsingOnlyAuthorArgument(){
        library.add("Pan Tadeusz", 2015, "Adam Mickiewicz");
        setupTestSystemOut();

        library.findByAttributes(null,null,"Adam Mickiewicz");

        assertEquals(
                "Result of search author: \"Adam Mickiewicz\"\r\n"+
                        "Id: -267431131, Title: Pan Tadeusz, Year: 2015, Author: Adam Mickiewicz\r\n",
                systemOutContent.toString());
    }


    @Test
    void shouldFoundBooksUsingTitleAndYearArguments(){
        library.add("Pan Tadeusz", 2015, "Adam Mickiewicz");
        setupTestSystemOut();

        library.findByAttributes("Pan Tadeusz",2015,null);

        assertEquals(
                "Result of search title: \"Pan Tadeusz\" year: 2015\r\n"+
                        "Id: -267431131, Title: Pan Tadeusz, Year: 2015, Author: Adam Mickiewicz\r\n",
                systemOutContent.toString());
    }

    @Test
    void shouldFoundBooksUsingTitleAndAuthorArgument(){
        library.add("Pan Tadeusz", 2015, "Adam Mickiewicz");
        setupTestSystemOut();

        library.findByAttributes("Pan Tadeusz",null,"Adam Mickiewicz");

        assertEquals(
                "Result of search title: \"Pan Tadeusz\" author: \"Adam Mickiewicz\"\r\n"+
                        "Id: -267431131, Title: Pan Tadeusz, Year: 2015, Author: Adam Mickiewicz\r\n",
                systemOutContent.toString());
    }

    @Test
    void shouldFoundBooksUsingYearAndAuthorArgument(){
        library.add("Pan Tadeusz", 2015, "Adam Mickiewicz");
        setupTestSystemOut();

        library.findByAttributes(null,2015,"Adam Mickiewicz");

        assertEquals(
                "Result of search year: 2015 author: \"Adam Mickiewicz\"\r\n"+
                        "Id: -267431131, Title: Pan Tadeusz, Year: 2015, Author: Adam Mickiewicz\r\n",
                systemOutContent.toString());
    }

    @AfterEach
    void restoreOut(){
        restoreOriginalOut();
    }

    private void saveOriginalSystemOut(){
        originalSystemOut=System.out;
    }

    private void setupTestSystemOut(){
        systemOutContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOutContent));
    }

    private void restoreOriginalOut() {
        System.setOut(originalSystemOut);
    }
}
