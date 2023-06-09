package org.itstep.service;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.itstep.dao.AuthorDaoImpl;
import org.itstep.dao.BookDaoImpl;
//import org.itstep.entity.AuthorDTO;
import org.itstep.dao.PublisherDaoImpl;
import org.itstep.entity.*;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
//import org.itstep.entity.BookDTO;


import java.lang.annotation.Repeatable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;


@Service
@RequiredArgsConstructor
public class BookService {

    protected final BookDaoImpl bookDao;
    protected final AuthorDaoImpl authorDao;
    protected final PublisherDaoImpl publisherDao;

    public List<Book> findAllBooks() {
        List<Book> books = bookDao.findAllBooks();
        return books;
    }

    public List<Author> findAllAuthors() {
        List<Author> authors = authorDao.findAllAuthors();
        return authors;
    }

    public List<Publisher> findAllPublishers() {
        List<Publisher> publishers = publisherDao.findAllPublisers();
        return publishers;
    }

    public void addBook(BookDTO bookDTO, AuthorDTO authorDTO, PublisherDTO publisherDTO) {
        List<Integer> authorsId = getAuthorsId(authorDTO);
        List<Integer> publishersId = getPublishersId(publisherDTO);

        List<Author> authorsList = new ArrayList<>();
        for (Integer id : authorsId) {
            authorsList.add(authorDao.findAuthorById(id));
        }
        List<Publisher> publishersList = new ArrayList<>();
        for (Integer id : publishersId) {
            publishersList.add(publisherDao.findPublisherById(id));
        }

        publishersList.forEach(System.out::println);
        authorsList.forEach(System.out::println);

        int bookId = getBookId(bookDTO);
        if (bookId == 0) {
            Book newBook = new Book(bookDTO.getTitle(), bookDTO.getPages(), authorsList, publishersList);
            if (newBook != null) {
                System.out.println("ADD Book (Service): " + newBook);
                bookDao.addBook(newBook);
            }
        }
    }

    private int getBookId(BookDTO bookDTO) {
        int id = 0;
        List<Book> books = bookDao.findAllBooks();
        for (Book book : books) {
            if (book.getTitle().equals(bookDTO.getTitle())) {
                id = book.getId();
                break;
            }
        }
        return id;
    }

    private List<Integer> getAuthorsId(AuthorDTO authorDTO) {
        List<Integer> authorsId = new CopyOnWriteArrayList<>();
        String[] authorsTMP = authorDTO.getAuthor().split(",");

        for (int i = 0; i < authorsTMP.length; i++) {
            if (!authorsTMP[i].equals("")) {
                int idTmp = 0;
                idTmp = getAuthorsIdTmp(authorsTMP[i]);
                // add unique author
                if (idTmp == 0) {
                    Author newAuthor = new Author(authorsTMP[i]);
                    if (newAuthor != null) {
                        System.out.println("ADD Author (Service): " + newAuthor);
                        authorDao.addAuthor(newAuthor);
                    }
                }
                idTmp = getAuthorsIdTmp(authorsTMP[i]);

                if (idTmp != 0) {
                    Author authorTmp = authorDao.findAuthorById(idTmp);
                    if (authorTmp != null) {
                        authorsId.add(idTmp);
                    }
                }
            }
        }
        return authorsId;
    }

    private int getAuthorsIdTmp(String author) {
        int idTmp = 0;
        List<Author> authors = authorDao.findAllAuthors();
        for (Author a : authors) {
            if (a.getAuthor().equals(author)) {
                idTmp = a.getId();
            }
        }
        return idTmp;
    }

    public void addAuthor(String author) {
        AuthorDTO authorDTO = new AuthorDTO(author);
        getAuthorsId(authorDTO);
    }


    private List<Integer> getPublishersId(PublisherDTO publisherDTO) {
        List<Integer> publishersId = new CopyOnWriteArrayList<>();
        String[] publishersTMP = publisherDTO.getPublisher().split(",");

        for (int i = 0; i < publishersTMP.length; i++) {
            if (!publishersTMP[i].equals("")) {
                int idTmp = 0;
                idTmp = getPublishersIdTmp(publishersTMP[i]);
                // add unique author
                if (idTmp == 0) {
                    Publisher newPublisher = new Publisher(publishersTMP[i]);
                    if (newPublisher != null) {
                        System.out.println("ADD Publisher (Service): " + newPublisher);
                        publisherDao.addPublisher(newPublisher);
                    }
                }
                idTmp = getPublishersIdTmp(publishersTMP[i]);

                if (idTmp != 0) {
                    Publisher publisherTmp = publisherDao.findPublisherById(idTmp);
                    if (publisherTmp != null) {
                        publishersId.add(idTmp);
                    }
                }
            }
        }
        return publishersId;
    }


    private int getPublishersIdTmp(String publisher) {
        int idTmp = 0;
        List<Publisher> publishers = publisherDao.findAllPublisers();
        for (Publisher p : publishers) {
            if (p.getPublisher().equals(publisher)) {
                idTmp = p.getId();
            }
        }
        return idTmp;
    }

    public void addPublisher(String publisher) {
        PublisherDTO publisherDTO = new PublisherDTO(publisher);
        getPublishersId(publisherDTO);
    }

    public void deleteBook(int id) {
        authorDao.deleteBookInAuthor(id);
        publisherDao.deleteBookInPublisher(id);
        bookDao.deleteBook(id);
        System.out.println("After Book delete");
    }

    public void deleteAuthor(int id) {
        authorDao.deleteAuthor(id);
    }

    public void deletePublisher(int id) {
        publisherDao.deletePublisher(id);
    }

    public List<Book> addFindBy(String findType, String data) {
        List<Book> booksRezult = new CopyOnWriteArrayList<>();
        List<Book> books = bookDao.findAllBooks();
        Book bookTmp = null;
        int id = 0;
        switch (findType) {
            case "title" -> {
                for (Book b : books) {
                    if (data.equals(b.getTitle().trim())) {
                        bookTmp = bookDao.findBookById(b.getId());
                        if (bookTmp != null) {
                            booksRezult.add(bookTmp);
                        }
                        break;
                    }
                }
            }
            case "author" -> {
                for (Book b : books) {
                    label:
                    for (Author a : b.getAuthors()) {
                        if (data.equals(a.getAuthor().trim())) {
                            bookTmp = bookDao.findBookById(b.getId());
                            if (bookTmp != null) {
                                booksRezult.add(bookTmp);
                            }
                            break label;
                        }
                    }
                }
            }
            case "publisher" -> {
                for (Book b : books) {
                    label:
                    for (Publisher p : b.getPublishers()) {
                        if (data.equals(p.getPublisher().trim())) {
                            bookTmp = bookDao.findBookById(b.getId());
                            if (bookTmp != null) {
                                booksRezult.add(bookTmp);
                            }
                            break label;
                        }
                    }
                }
            }
            default -> System.out.println("unknown type");
        }
        if (booksRezult != null) {
            System.out.println("List<Book> findBy isn't null");
            booksRezult.stream().forEach(System.out::println);
        } else {
            System.out.println("List<Book> findBy is null");
        }
        return booksRezult;
    }

}



