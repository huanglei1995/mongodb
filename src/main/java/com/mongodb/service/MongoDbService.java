package com.mongodb.service;

import com.mongodb.model.Book;

import java.util.List;

/**
 * Created by hl on 2019/12/19 0019 16:48
 * description:
 */
public interface MongoDbService {

    String saveObj(Book book);

    List<Book> findAll();

    Book getBookById(String id);

    Book getBookByName(String name);

    String updateBook(Book book);

    String deleteBook(Book book);

    String deleteBookById(String id);
}
