package com.mongodb.controller;

import com.mongodb.model.Book;
import com.mongodb.service.MongoDbService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by hl on 2019/12/19 0019 16:49
 * description:
 */
@RestController
public class MongoDbController {

    @Resource
    private MongoDbService mongoDbService;


    @RequestMapping("/mongo/save")
    public String saveObj (String id, String name, int price) {
        Book book = new Book();
        book.setCreateTime(new Date());
        book.setUpdateTime(new Date());
        book.setId(id);
        book.setInfo("info");
        book.setPrice(price);
        book.setPublish("publish");
        book.setName(name);
        return mongoDbService.saveObj(book);
    }

    @RequestMapping("/mongo/findAll")
    public List<Book> findAll() {return mongoDbService.findAll();}

    @RequestMapping("/mongo/findOne")
    public Book findOne(@RequestParam String id) {return mongoDbService.getBookById(id);}

    @RequestMapping("/mongo/findOneByName")
    public Book findOneByName(@RequestParam String name) {return mongoDbService.getBookByName(name);}

    @RequestMapping("/mongo/update")
    public String update(@RequestBody Book book) {return mongoDbService.updateBook(book);}

    @RequestMapping("/mongo/delById")
    public String delById(@RequestParam String id) {return mongoDbService.deleteBookById(id);}


}
