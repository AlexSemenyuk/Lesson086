package org.itstep.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import org.itstep.entity.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class BookDaoImpl {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Book> findAllBooks() {
        TypedQuery<Book> query = entityManager.createQuery("select с from Book с", Book.class);
        List<Book> books = query.getResultList();
        if (books != null) {
            System.out.println("BookList from Impl");
            books.stream().forEach(System.out::println);
        }
        return books;
    }


    public Book findBookById(int id) {
        Book book = entityManager.find(Book.class, id);
        return book;
    }


    @Transactional
    public void addBook(Book book) {
        entityManager.persist(book);
        book.getAuthors().forEach(a -> entityManager.merge(a));
        book.getPublishers().forEach(p -> entityManager.merge(p));
        System.out.println("Persist Book is successful");
    }


    @Transactional
    public void deleteBook(int id) {
        Book book = entityManager.find(Book.class, id);
        if (book != null) {
            System.out.println("book before DELETE" + book.toString());
            entityManager.remove(book);
        } else {
            System.out.println("Delete Book impl: id = null");
        }
    }

}
