package com.mongodb;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.bson.Document;

import java.math.BigDecimal;
import java.util.*;

import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Filters.*;

@SpringBootTest
class MongodbApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(MongodbApplicationTests.class);

    //数据库
    private MongoDatabase db;

    //文档集合
    private MongoCollection<Document> doc;

    //连接客户端（内置连接池）
    private MongoClient client;

    public void init(){
        client = new MongoClient("192.168.4.97",27017);
        db =client.getDatabase("hl");
        doc = db.getCollection("user");
        System.out.println("ddddddddddddddddddddddd========================================================");
    }


    /**
     *
     */
    @Test
    public void insertDemo() {

        init();

        Document doc1 = new Document();
        doc1.append("username", "cang");
        doc1.append("country", "USA");
        doc1.append("age", 20);
        doc1.append("length", 1.77f);
        doc1.append("salary", new BigDecimal("6565.22"));
        Map<String, String> address1 = new HashMap<>();
        address1.put("aCode", "0000");
        address1.put("add", "xxx000");
        doc1.append("address", address1);

        //添加“favorites”子文档，其中两个属性是数组
        Map<String, Object> favorites1 = new HashMap<String, Object>();
        favorites1.put("movies", Arrays.asList("aa","bb"));
        favorites1.put("cites", Arrays.asList("东莞","东京"));
        doc1.append("favorites", favorites1);

        Document doc2  = new Document();
        doc2.append("username", "Chen");
        doc2.append("country", "China");
        doc2.append("age", 30);
        doc2.append("lenght", 1.77f);
        doc2.append("salary", new BigDecimal("8888.22"));
        Map<String, String> address2 = new HashMap<>();
        address2.put("aCode", "411000");
        address2.put("add", "我的地址2");
        doc2.append("address", address2);
        Map<String, Object> favorites2 = new HashMap<>();
        favorites2.put("movies", Arrays.asList("东游记","一路向东"));
        favorites2.put("cites", Arrays.asList("珠海","东京"));
        doc2.append("favorites", favorites2);
        doc.insertMany(Arrays.asList(doc1,doc2));
    }


    @Test
    public void testFind(){
        final List<Document> ret = new ArrayList<>();
        //block接口专门用于处理查询出来的数据
        Block<Document> printBlock = new Block<Document>() {
            @Override
            public void apply(Document t) {
                logger.info(t.toJson());//打印数据
                ret.add(t);
            }

        };
        //select * from users  where favorites.cites has "东莞"、"东京"
        //db.users.find({ "favorites.cites" : { "$all" : [ "东莞" , "东京"]}})
        Bson all = all("favorites.cites", Arrays.asList("东莞","东京"));//定义数据过滤器，喜欢的城市中要包含"东莞"、"东京"
        FindIterable<Document> find = doc.find(all);
        find.forEach(printBlock);
        ret.removeAll(ret);


        //select * from users  where username like '%s%' and (contry= English or contry = USA)
        // db.users.find({ "$and" : [ { "username" : { "$regex" : ".*s.*"}} , { "$or" : [ { "country" : "English"} , { "country" : "USA"}]}]})

        String regexStr = ".*s.*";
        Bson regex = regex("username", regexStr);//定义数据过滤器，username like '%s%'
        Bson or = or(eq("country","English"),eq("country","USA"));//定义数据过滤器，(contry= English or contry = USA)
        Bson and = and(regex,or);
        FindIterable<Document> find2 = doc.find(and);
        find2.forEach(printBlock);

    }

    @Test
    public void testUpdate(){
        //update  users  set age=6 where username = 'lison'
//    	db.users.updateMany({ "username" : "lison"},{ "$set" : { "age" : 6}},true)

        Bson eq = eq("username", "lison");//定义数据过滤器，username = 'lison'
        Bson set = set("age", 8);//更新的字段.来自于Updates包的静态导入
        UpdateResult updateMany = doc.updateMany(eq, set);
        logger.info("------------------>"+String.valueOf(updateMany.getModifiedCount()));//打印受影响的行数

        //update users  set favorites.movies add "小电影2 ", "小电影3" where favorites.cites  has "东莞"
        //    db.users.updateMany({ "favorites.cites" : "东莞"}, { "$addToSet" : { "favorites.movies" : { "$each" : [ "小电影2 " , "小电影3"]}}},true)

        Bson eq2 = eq("favorites.cites", "东莞");//定义数据过滤器，favorites.cites  has "东莞"
        Bson addEachToSet = addEachToSet("favorites.movies", Arrays.asList( "小电影2 ", "小电影3"));//更新的字段.来自于Updates包的静态导入
        UpdateResult updateMany2 = doc.updateMany(eq2, addEachToSet);
        logger.info("------------------>"+String.valueOf(updateMany2.getModifiedCount()));
    }

    @Test
    public void testDelete(){

        //delete from users where username = ‘lison’
        Bson eq = eq("username", "lison");//定义数据过滤器，username='lison'
        DeleteResult deleteMany = doc.deleteMany(eq);
        logger.info("------------------>"+String.valueOf(deleteMany.getDeletedCount()));//打印受影响的行数

        //delete from users where age >8 and age <25
        Bson gt = gt("age",8);//定义数据过滤器，age > 8，所有过滤器的定义来自于Filter这个包的静态方法，需要频繁使用所以静态导入
//    	Bson gt = Filter.gt("age",8);

        Bson lt = lt("age",25);//定义数据过滤器，age < 25
        Bson and = and(gt,lt);//定义数据过滤器，将条件用and拼接
        DeleteResult deleteMany2 = doc.deleteMany(and);
        logger.info("------------------>"+String.valueOf(deleteMany2.getDeletedCount()));//打印受影响的行数
    }

}
