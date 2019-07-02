package org.library.model;


public class Search {

    // member variables

    private Book book;

    public Search() {
        book=new Book();
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getIsbn() {
        return book.getIsbn();
    }

    public void setIsbn(String isbn) {
       book.setIsbn(isbn);
    }

    public String getTitle() {
        return book.getTitle();
    }

    public void setTitle(String title) {
        book.setTitle(title);
    }

    public String getAuthor() {
        return book.getAuthor();
    }

    public void setAuthor(String author) {
        book.setAuthor(author);
    }

    @Override
    public String toString() {
        return "Search{" +
                "isbn='" + book.getIsbn() + '\'' +
                ", title='" + book.getTitle() + '\'' +
                ", author='" + book.getAuthor() + '\'' +
                '}';
    }
}