# mongodb
mongodb和java集成demo


## 一：MongoDb安装步骤

#### 第一步：下载mongodb

```
cd /usr/local
wget https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-4.0.14.tgz
```

#### 第二步：解压MongoDB
```
tar -zxvf mongodb-linux-x86_64-4.0.14.tgz
cp mongodb-linux-x86_64-4.0.14.tgz mongodb
```

#### 第三步： 新建日志文件和数据文件

```$xslt
cd /usr/local/mongodb
mkdir data
mkdir logs
cd logs
vi mongodb.log
```

#### 第四步：设置配置文件
```$xslt
vi mongo.conf

dbpath=/usr/local/mongodb/data  # 数据文件地址
logpath=/usr/local/mongodb/logs/mongodb.log  # 日志文件地址
logappend=true   # 作用：当mongod或mongos重启时，如果为true，将日志追加到原来日志文件内容末尾；如果为false，将创建一个新的日志文件
bind_ip=0.0.0.0  # 绑定的ip,都可以连接
fork=true        # 
port=27017       # mongodb启动的端口
auth=false       # 是否需要用户认证
journal=true     # 
quiet=true
```

#### 第五步：启动mongodb
 ```$xslt
/usr/local/mongodb/bin/mongod -f /usr/local/mongodb/mongo.conf
```

#### 第六步：开启防火墙

```$xslt
firewall-cmd --get-active-zones
firewall-cmd --zone=public --add-port=27017/tcp --permanent
firewall-cmd --reload
```

#### 第七步：配置脚本快捷启动
```$xslt
vi start.sh # 编辑start.sh脚本，内容如下
nohup /usr/local/mongodb/bin/mongod -f /usr/local/mongodb/mongo.conf  &

start.sh  #执行命令启动mongdoDb
```


## 二：spring boot Demo

#### 第一步：引入jar包

```$xslt
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

#### 第二步：配置mongodb参数

```$xslt
编辑application.yml文件
server.port=8085
spring.data.mongodb.host=192.168.4.97
spring.data.mongodb.port=27017
#spring.data.mongodb.uri=mongodb://192.168.4.97:21017/hl
spring.data.mongodb.database=hl
```


#### 第三步：注入到项目中，使用
```$xslt
@Resource
private MongoTemplate mongoTemplate;
```


## 三：MongodDB语法学习

#### 知识储备

- 查询选择器
```$xslt
$eq 等于 
$lt 小于 
$gt 大于 
$lte 小于等于 
$gte 大于等于 
$in 判断元素是否在指定的集合范围里 
$all 判断数组中是否包含某几个元素,无关顺序 
$nin 判断元素是否不在指定的集合范围里 
$ne 不等于，不匹配参数条件 
$not 不匹配结果 
$or 有一个条件成立则匹配 
$nor 所有条件都不匹配 
$and 所有条件都必须匹配 
$exists 判断元素是否存在 
. 子文档匹配 
$regex 正则表达式匹配
``` 

#### 第一： 查询语法

- 查询语法
```$xslt
db.collection.find(query, projection)

参数说明：
query ：可选，使用查询操作符指定查询条件
projection ：可选，使用投影操作符指定返回的键。查询时返回文档中所有键值， 只需省略该参数即可（默认省略）。

注意：0表示字段排除，非0表示字段选择并排除其他字段，所有字段必须设置同样的值；

```

- 查询语句实例

```$xslt
1)in选择器示例：
db.users.find({"username":{"$in":["a", "b", "c"]}} ).pretty()
查询姓名为a、b和c这个范围的人
2)exists选择器示例：
db.users.find({"lenght":{"$exists":true}}).pretty()
判断文档有没有关心的字段
3)not选择器示例：
db.users.find({"lenght":{"$not":{"$gte":1.77}}}).pretty()
查询高度小于1.77或者没有身高的人
not语句 会把不包含查询语句字段的文档 也检索出来
4)字段映射
字段选择并排除其他字段：db.users.find({},{'username':1})
字段排除：db.users.find({},{'username':0})
0代表排除1：代表显示
5)排序
sort()：db.users.find().sort({"username":1}).pretty() 1：升序   -1：降序
6）跳过和限制

```

#### 第二： 更新语法


#### 第三： 删除语法


#### 第四： 插入语法

```$xslt

定义变量
var user1 = 
  {
      "_id": "1",
      "price": NumberInt("5"),
      "name": "hl",
      "info": "info",
      "publish": "publish",
      "createTime": ISODate("2019-12-19T08:56:34.512Z"),
      "updateTime": ISODate("2019-12-19T08:56:34.512Z"),
      "_class": "com.mongodb.model.Book"
  };
db.user.insert(user1)
```