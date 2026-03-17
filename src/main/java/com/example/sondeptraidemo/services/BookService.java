package com.example.sondeptraidemo.services;

import com.example.sondeptraidemo.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final List<Book> books = new ArrayList<>();
    private long nextId = 1;

    public BookService() {
        // dữ liệu mẫu
        addBook(new Book(null, "Spring Boot", "Huy Cuong"));
        addBook(new Book(null, "Spring Boot V2", "Anh"));
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public void addBook(Book book) {
        book.setId(nextId++);
        books.add(book);
    }

    public Optional<Book> getBookById(Long id) {
        return books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
    }

    public void updateBook(Book updatedBook) {
        books.stream()
                .filter(b -> b.getId().equals(updatedBook.getId()))
                .findFirst()
                .ifPresent(b -> {
                    b.setTitle(updatedBook.getTitle());
                    b.setAuthor(updatedBook.getAuthor());
                });
    }

    public void deleteBookById(Long id) {
        books.removeIf(b -> b.getId().equals(id));
    }
}
