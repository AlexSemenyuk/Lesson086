package org.itstep.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.itstep.entity.Book;
import org.itstep.entity.Publisher;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PublisherDaoImpl {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Publisher> findAllPublisers() {
        TypedQuery<Publisher> query = entityManager.createQuery("select с from Publisher с", Publisher.class);
        List<Publisher> publishers = query.getResultList();
        if (publishers != null) {
            publishers.forEach(System.out::println);
        }
        return publishers;
    }

    public Publisher findPublisherById(int id) {
        Publisher publisher = entityManager.find(Publisher.class, id);
        return publisher;
    }

    @Transactional
    public void addPublisher(Publisher newPublisher) {
        entityManager.persist(newPublisher);
        System.out.println("Persist Publisher is successful");
    }

        @Transactional
    public void deleteBookInPublisher(int id) {
        List<Publisher> publishers = findAllPublisers();
        for (Publisher p : publishers) {
            List<Book> booksOfPublisher = p.getBooks();
            for (Book b : booksOfPublisher) {
                if (b.getId() == id) {
                    booksOfPublisher.remove(b);
                    break;
                }
            }
        }
    }
    @Transactional
    public void deletePublisher(int id) {
        Publisher publisher = entityManager.find(Publisher.class, id);
        if (publisher != null) {
            System.out.println("Delete publisher.toString() = " + publisher.toString());
            entityManager.remove(publisher);
        } else {
            System.out.println("Delete Publisher impl: id = null");
        }
    }
}
