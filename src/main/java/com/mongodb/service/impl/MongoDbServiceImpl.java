package com.mongodb.service.impl;

import com.mongodb.model.Book;
import com.mongodb.service.MongoDbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by hl on 2019/12/19 0019 16:48
 * description:
 */
@Service
public class MongoDbServiceImpl implements MongoDbService {


    private static final Logger logger = LoggerFactory.getLogger(MongoDbService.class);

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 保存对象
     * @param book
     * @return
     */
    @Override
    public String saveObj(Book book) {
        logger.info("--------------------->[MongoDB save start]");
        book.setCreateTime(new Date());
        book.setUpdateTime(new Date());
        mongoTemplate.save(book);
        return "添加成功";
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<Book> findAll() {
        logger.info("--------------------->[MongoDB find start]");
        return mongoTemplate.findAll(Book.class);
    }

    /***
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public Book getBookById(String id) {
        logger.info("--------------------->[MongoDB find start]");
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, Book.class);
    }

    /**
     * 根据名称查询
     * @param name
     * @return
     */
    @Override
    public Book getBookByName(String name) {
        logger.info("--------------------->[MongoDB find start]");
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query, Book.class);
    }

    /**
     * 更新对象
     * @param book
     * @return
     */
    @Override
    public String updateBook(Book book) {
        logger.info("--------------------->[MongoDB update start]");
        Query query = new Query(Criteria.where("_id").is(book.getId()));
        Update update = new Update().set("publish", book.getPublish())
                .set("info", book.getInfo())
                .set("updateTime", new Date());
        //updateFirst 更新查询返回结果集的第一条
        mongoTemplate.updateFirst(query, update, Book.class);
        return "success";
    }

    /***
     * 删除对象
     * @param book
     * @return
     */
    @Override
    public String deleteBook(Book book) {
        logger.info("--------------------->[MongoDB delete start]");
        mongoTemplate.remove(book);
        return "success";
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @Override
    public String deleteBookById(String id) {
        logger.info("--------------------->[MongoDB delete start]");
        //findOne
        Book book = getBookById(id);
        deleteBook(book);
        return "success";
    }
}
