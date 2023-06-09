package org.itstep.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.itstep.entity.Author;
import org.itstep.entity.Book;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorDaoImpl {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Author> findAllAuthors() {
        TypedQuery<Author> query = entityManager.createQuery("select с from Author с", Author.class);
        List<Author> authors = query.getResultList();
        if (authors != null) {
            authors.forEach(System.out::println);
        }
        return authors;
    }
    public Author findAuthorById(int id) {
        Author author = entityManager.find(Author.class, id);
        return author;
    }
    @Transactional
    public void addAuthor(Author newAuthor) {
        entityManager.persist(newAuthor);
        System.out.println("Persist Author is successful");
    }
    @Transactional
    public void deleteBookInAuthor(int id) {
        List<Author> authors = findAllAuthors();
        for (Author a : authors) {
            List<Book> booksOfAuthor = a.getBooks();
            if (booksOfAuthor != null) {
                for (Book b : booksOfAuthor) {
                    if (b.getId() == id) {
                        booksOfAuthor.remove(b);
                        break;
                    }
                }
            }
        }
    }

    @Transactional
    public void deleteAuthor(int id) {
        Author author = entityManager.find(Author.class, id);
        if (author != null) {
            System.out.println("Delete author.toString() = " + author.toString());
            entityManager.remove(author);
        } else {
            System.out.println("Delete Author impl: id = null");
        }
    }
}
