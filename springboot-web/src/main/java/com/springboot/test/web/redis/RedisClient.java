package com.springboot.test.web.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * redis基本功能类
 * @author liufei
 * @date 2019/1/10 10:46
 * redis命令参考：http://redisdoc.com/index.html
 */
public class RedisClient {
    private Logger logger = LoggerFactory.getLogger(RedisClient.class);
    /**
     * 非切片的客户端连接
     */
    private Jedis jedis;
    /**
     * 非切片连接池
     */
    private JedisPool jedisPool;
    /**
     * 切片的客户端连接--分布式调用
     */
    private ShardedJedis shardedJedis;
    /**
     * 切片连接池---分布式调用
     */
    private ShardedJedisPool shardedJedisPool;

    public void show() throws Exception {
        initPool();
        initShardedPool();
        KeyOperate();//key
        StringOperation();//string
        ListOperation();//list
        SetOperation();//set
        SortedSetOperation();//zset
        HashOperation();
    }

    /**
     * 初始化非切片池
     */
    private void initPool(){
        jedisPool = new JedisPool(initConfig(),"localhost",6379);
        jedis = jedisPool.getResource();
    }

    /**
     * 初始化切片池
     */
    private void initShardedPool(){
        /**
         * slave链接
         */
        List<JedisShardInfo> shards = new ArrayList<>();
        shards.add(new JedisShardInfo("127.0.0.1", 6379, "master"));
        //shards.add(new JedisShardInfo("127.0.0.1", 6378, "branch"));
        shardedJedisPool = new ShardedJedisPool(initConfig(),shards);
        shardedJedis = shardedJedisPool.getResource();
    }

    /**
     * 初始化池的基本配置
     * @return
     */
    private JedisPoolConfig initConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        /**
         * maxActive，maxWait在高版本中已被废弃，替换为maxTotal何maxWaitMillis
         * maxActive  ==>  maxTotal
         * maxWait    ==>  maxWaitMillis
         * 在指定时刻通过pool能够获取到的最大的连接的jedis个数
         * 默认值为8
         */
        config.setMaxTotal(20);
        /**
         * 在容器中的最小的闲置连接数，仅仅在此值为正数且timeBetweenEvictionRunsMillis值大于0时有效
         * 确保在对象逐出线程工作后确保线程池中有最小的实例数量，如果该值设定大于maxidle的值，此值不会生效，maxidle的值会生效
         * 默认值为0
         */
        config.setMinIdle(0);
        /**
         * 最大能够保持idle的数量，控制一个pool最多有多少个状态为idle(闲置)的jedis实例
         * 默认值为8
         */
        config.setMaxIdle(5);
        /**
         * 当连接池内的连接耗尽时，getBlockWhenExhausted为true时，连接会阻塞，超过了阻塞的时间（设定的maxWaitMillis，单位毫秒）时会报错
         * 默认值为-1
         */
        config.setMaxWaitMillis(1000l);
        /**
         * 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
         * 默认是false
         */
        config.setTestOnBorrow(false);
        /**
         * 如果为true，表示有一个idle object evitor线程对idle object进行扫描，如果validate失败，此object会被从pool中drop掉；
         * 这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
         * 默认是false
         */
        config.setTestWhileIdle(false);
        /**
         * 表示idle object evitor两次扫描之间要sleep的毫秒数，逐出扫描的时间间隔（毫秒），如果为负数，则不运行逐出线程
         * 默认为-1
         */
        config.setTimeBetweenEvictionRunsMillis(-1);
        /**
         * 设置的逐出策略类名
         * 默认就是DefaultEvictionPolicy（当连接超过最大空闲时间时或连接数超过最大空闲连接数）
         */
        config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
        return config;
    }

    /**
     * key功能测试方法
     * 命令参考：http://redisdoc.com/key/index.html
     */
    private void KeyOperate() throws Exception {
        logger.info("******************************************************************");
        logger.info("************************key operation*****************************");
        logger.info("******************************************************************");
        logger.info("清空redis库中所有数据：{}",jedis.flushDB());
        logger.info("判断key【{}】键是否存在：{}","key999",shardedJedis.exists("key999"));
        logger.info("新增key【{}】,value【{}】的键值对：{}","key001","value0001",shardedJedis.set("key001","value001"));
        logger.info("判断key【{}】键是否存在：{}","key001",shardedJedis.exists("key001"));
        logger.info("新增key【{}】,value【{}】的键值对：{}","key002","value0002",shardedJedis.set("key002","value002"));
        logger.info("新增key【{}】,value【{}】的键值对：{}","key003","value0003",shardedJedis.set("key003","value003"));
        logger.info("系统中所有键如下；");
        Set<String> keys = jedis.keys("*");
        Iterator<String> it = keys.iterator();
        while (it.hasNext()){
            String key = it.next();
            logger.info("key【{}】，value【{}】",key,jedis.get(key));
        }
        logger.info("删除key【{}】，如果key不存在，则忽略该命令，操作结果：{}","key002",jedis.del("key002"));
        logger.info("判断key【{}】键是否存在：{}","key002",shardedJedis.exists("key002"));
        logger.info("删除key【{}】，如果key不存在，则忽略该命令，操作结果：{}","key002",jedis.del("key002"));
        logger.info("设置key【{}】的过期时间为【{}】秒，操作结果：{}","key001",5,jedis.expire("key001",5));
        Long expireTime = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-10").getTime()/1000;
        logger.info("设置key【{}】的过期时间为【{}】，操作结果：{}","key003",expireTime,jedis.expireAt("key003",expireTime));
        logger.info("判断key【{}】键是否存在：{}","key001",shardedJedis.exists("key001"));

        Thread.sleep(1*1000);
        logger.info("key【{}】的生存时间为【{}】秒，【{}】毫秒","key001",jedis.ttl("key001"),jedis.pttl("key001"));
        logger.info("key【{}】的生存时间为【{}】秒，【{}】毫秒","key003",jedis.ttl("key003"),jedis.pttl("key003"));

        logger.info("移除key【{}】的剩余生存时间，将数据转换为持久有效，操作结果：{}","key001",jedis.persist("key001"));
        Thread.sleep(1*1000);
        logger.info("key【{}】的生存时间为【{}】秒，【{}】毫秒","key001",jedis.ttl("key001"),jedis.pttl("key001"));
        logger.info("key【{}】的生存时间为【{}】秒，【{}】毫秒","key003",jedis.ttl("key003"),jedis.pttl("key003"));

        logger.info("key【{}】存储的值的数据类型：【{}】,value【{}】","key001",jedis.type("key001"),jedis.get("key001"));
        /**
         * 一些其他方法：
         * 1、修改键名：jedis.rename("key6", "key0");
         * 2. 将当前db的key移动到给定的db当中：jedis.move("foo", 1)
         */
    }

    /**
     * String功能
     */
    private void StringOperation() throws InterruptedException {
        logger.info("******************************************************************");
        logger.info("************************jedis string operation********************");
        logger.info("******************************************************************");
        logger.info("清空redis库中所有数据：{}",jedis.flushDB());
        logger.info("==============  增  ================");
        jedis.set("key001","value001");
        jedis.set("key002","value002");
        jedis.set("key003","value003");
        logger.info("新增的3个键值对如下；");
        logger.info("key【{}】，value【{}】","key001",jedis.get("key001"));
        logger.info("key【{}】，value【{}】","key002",jedis.get("key002"));
        logger.info("key【{}】，value【{}】","key003",jedis.get("key003"));

        logger.info("==============  删  ================");
        logger.info("删除【{}】键值对：【{}】","key003",jedis.del("key003"));
        logger.info("获取【{}】键对应的值：【{}】","key003",jedis.get("key003"));

        logger.info("==============  改  ================");
        logger.info("直接覆盖【{}】原来的数据【{}】","key001",jedis.set("key001","value001-update"));
        logger.info("获取【{}】对应的新值【{}】","key001",jedis.get("key001"));
        logger.info("在【{}】原来的值后面追加【{}】","key002",jedis.append("key002","+appendString"));
        logger.info("获取【{}】对应的新值【{}】","key002",jedis.get("key002"));

        logger.info("=======  增、删、差(多个)  =========");
        /**
         * mset,mget同时新增，修改，查询多个键值对
         * 等价于：
         * jedis.set("name","ssss");
         * jedis.set("jarorwar","xxxx");
         */
        logger.info("一次性新增key201,key202,key203,key204及其对应值：【{}】",jedis.mset("key201","value201","key202","value202","key203","value203","key204","value204"));
        logger.info("一次性获取key201,key202,key203,key204各自对应的值：【{}】",jedis.mget("key201","key202","key203","key204"));
        logger.info("一次性删除key201,key202：【{}】",jedis.del(new String[]{"key201", "key202"}));
        logger.info("一次性获取key201,key202,key203,key204各自对应的值：【{}】",jedis.mget("key201","key202","key203","key204"));

        logger.info("******************************************************************");
        logger.info("*****************shardedJedis string operation********************");
        logger.info("*********jedis具备的功能shardedJedis中也可直接使用****************");
        logger.info("清空redis库中所有数据：{}",jedis.flushDB());
        logger.info("新增键值对时防止覆盖原先值");
        logger.info("【{}】不存在时，新增key【{}】：【{}】","key301","key301",shardedJedis.setnx("key301","value301"));
        logger.info("【{}】不存在时，新增key【{}】：【{}】","key302","key302",shardedJedis.setnx("key302","value302"));
        logger.info("当【{}】存在时，尝试新增key【{}】：【{}】","key302","key302",shardedJedis.setnx("key302","value302_new"));
        logger.info("key【{}】，value【{}】","key301",jedis.get("key301"));
        logger.info("key【{}】，value【{}】","key302",jedis.get("key302"));
        logger.info("=====  超过有效期键值对被删除  =====");
        logger.info("新增key303，并指定过期时间为2秒",shardedJedis.setex("key303",2,"value303-2seconds"));
        logger.info("获取key303对应的值：【{}】",shardedJedis.get("key303"));
        Thread.sleep(3000);
        logger.info("3秒之后，获取key303对应的值：【{}】",shardedJedis.get("key303"));
        logger.info("============获取原值，更新为新值一步完成==================");
        logger.info("key302原值：【{}】",shardedJedis.getSet("key302","value302-after-getset"));
        logger.info("key302新值：【{}】",shardedJedis.get("key302"));
        logger.info("============获取子字符串==================");
        logger.info("获取key302对应值中的子字符串：【{}】",shardedJedis.getrange("key302",5,7));
    }

    /**
     * List功能
     */
    private void ListOperation(){
        logger.info("******************************************************************");
        logger.info("****************************list operation************************");
        logger.info("******************************************************************");
        logger.info("清空redis库中所有数据：{}",jedis.flushDB());

        logger.info("==============  增  ================");
        shardedJedis.lpush("stringlists", "vector");
        shardedJedis.lpush("stringlists", "ArrayList");
        shardedJedis.lpush("stringlists", "vector");
        shardedJedis.lpush("stringlists", "vector");
        shardedJedis.lpush("stringlists", "LinkedList");
        shardedJedis.lpush("stringlists", "MapList");
        shardedJedis.lpush("stringlists", "SerialList");
        shardedJedis.lpush("stringlists", "HashList");
        shardedJedis.lpush("numberlists", "3");
        shardedJedis.lpush("numberlists", "1");
        shardedJedis.lpush("numberlists", "5");
        shardedJedis.lpush("numberlists", "2");

        logger.info("所有元素-stringlists：【{}】",shardedJedis.lrange("stringlists",0,-1));
        logger.info("所有元素-numberlists：【{}】",shardedJedis.lrange("numberlists",0,-1));

        logger.info("==============  删  ================");
        // 删除列表指定的值 ，第二个参数为删除的个数（有重复时），后add进去的值先被删，类似于出栈
        logger.info("成功删除指定元素个数-stringlists：【{}】",shardedJedis.lrem("stringlists", 2, "vector"));
        logger.info("删除指定元素之后-stringlists：【{}】",shardedJedis.lrange("stringlists", 0, -1));

        // 删除区间以外的数据
        logger.info("删除下标0-3区间之外的元素：【{}】",shardedJedis.ltrim("stringlists", 0, 3));
        logger.info("删除指定区间之外元素后-stringlists：【{}】",shardedJedis.lrange("stringlists", 0, -1));

        // 列表元素出栈 lpop：从左侧开始，rpop：从右侧开始
        logger.info("出栈元素：【{}】",shardedJedis.lpop("stringlists"));
        logger.info("元素出栈后-stringlists：【{}】",shardedJedis.lrange("stringlists", 0, -1));

        logger.info("==============  改  ================");
        // 修改列表中指定下标的值
        shardedJedis.lset("stringlists", 0, "hello list!");
        logger.info("下标为0的值修改后-stringlists：【{}】",shardedJedis.lrange("stringlists", 0, -1));

        logger.info("==============  查  ================");
        // 数组长度
        logger.info("长度-stringlists：【{}】",shardedJedis.llen("stringlists"));
        logger.info("长度-numberlists：【{}】",shardedJedis.llen("numberlists"));
        // 排序
        /*
         * list中存字符串时必须指定参数为alpha，如果不使用SortingParams，而是直接使用sort("list")，
         * 会出现"ERR One or more scores can't be converted into double"
         */
        SortingParams sortingParameters = new SortingParams();
        sortingParameters.alpha();
        sortingParameters.limit(0, 3);
        logger.info("返回排序后的结果-stringlists：【{}】",shardedJedis.sort("stringlists",sortingParameters));
        logger.info("返回排序后的结果-numberlists：【{}】",shardedJedis.sort("numberlists"));
        // 子串：  start为元素下标，end也为元素下标；-1代表倒数一个元素，-2代表倒数第二个元素，从右往左开始计数
        logger.info("子串-第二个开始到结束：【{}】",shardedJedis.lrange("stringlists", 1, -1));
        // 获取列表指定下标的值
        logger.info("获取下标为2的元素：【{}】",shardedJedis.lindex("stringlists", 2));
    }

    /**
     * Set功能
     */
    private void SetOperation(){
        logger.info("******************************************************************");
        logger.info("*****************************set operation************************");
        logger.info("******************************************************************");
        logger.info("清空redis库中所有数据：{}",jedis.flushDB());

        logger.info("==============  增  ================");
        logger.info("向sets集合中加入元素element001："+jedis.sadd("sets", "element001"));
        logger.info("向sets集合中加入元素element002："+jedis.sadd("sets", "element002"));
        logger.info("向sets集合中加入元素element003："+jedis.sadd("sets", "element003"));
        logger.info("向sets集合中加入元素element004："+jedis.sadd("sets", "element004"));
        logger.info("向sets集合中加入重复元素element004："+jedis.sadd("sets", "element004"));
        logger.info("查看sets集合中的所有元素:"+jedis.smembers("sets"));

        logger.info("==============  删  ================");
        logger.info("集合sets中删除元素element003："+jedis.srem("sets", "element003"));
        logger.info("查看sets集合中的所有元素:"+jedis.smembers("sets"));
        /*logger.info("sets集合中任意位置的元素出栈："+jedis.spop("sets"));//注：出栈元素位置不定？--无实际意义
        logger.info("查看sets集合中的所有元素:"+jedis.smembers("sets"));*/

        logger.info("==============  查  ================");
        logger.info("判断element001是否在集合sets中："+jedis.sismember("sets", "element001"));
        logger.info("循环查询获取sets中的每个元素：");
        Set<String> set = jedis.smembers("sets");
        Iterator<String> it=set.iterator() ;
        while(it.hasNext()){
            String obj=it.next();
            logger.info(obj);
        }

        logger.info("============ 集合运算  ==============");
        logger.info("sets1中添加元素element001："+jedis.sadd("sets1", "element001"));
        logger.info("sets1中添加元素element002："+jedis.sadd("sets1", "element002"));
        logger.info("sets1中添加元素element003："+jedis.sadd("sets1", "element003"));
        logger.info("sets1中添加元素element002："+jedis.sadd("sets2", "element002"));
        logger.info("sets1中添加元素element003："+jedis.sadd("sets2", "element003"));
        logger.info("sets1中添加元素element004："+jedis.sadd("sets2", "element004"));
        logger.info("查看sets1集合中的所有元素:"+jedis.smembers("sets1"));
        logger.info("查看sets2集合中的所有元素:"+jedis.smembers("sets2"));
        logger.info("sets1和sets2交集："+jedis.sinter("sets1", "sets2"));
        logger.info("sets1和sets2并集："+jedis.sunion("sets1", "sets2"));
        logger.info("sets1和sets2差集："+jedis.sdiff("sets1", "sets2"));//差集：set1中有，set2中没有的元素
    }

    /**
     * SortedSet功能（有序集合） zset
     */
    private void SortedSetOperation(){
        logger.info("******************************************************************");
        logger.info("*************************sorted set operation*********************");
        logger.info("******************************************************************");
        logger.info("清空redis库中所有数据：{}",jedis.flushDB());

        logger.info("==============  增  ================");
        logger.info("zset中添加元素element001："+shardedJedis.zadd("zset", 7.0, "element001"));
        logger.info("zset中添加元素element002："+shardedJedis.zadd("zset", 8.0, "element002"));
        logger.info("zset中添加元素element003："+shardedJedis.zadd("zset", 2.0, "element003"));
        logger.info("zset中添加元素element004："+shardedJedis.zadd("zset", 3.0, "element004"));
        logger.info("zset集合中的所有元素："+shardedJedis.zrange("zset", 0, -1));//按照权重值排序

        logger.info("==============  删  ================");
        logger.info("zset中删除元素element002："+shardedJedis.zrem("zset", "element002"));
        logger.info("zset集合中的所有元素："+shardedJedis.zrange("zset", 0, -1));

        logger.info("==============  查  ================");
        logger.info("统计zset集合中的元素中个数："+shardedJedis.zcard("zset"));
        logger.info("统计zset集合中权重某个范围内（1.0——5.0），元素的个数："+shardedJedis.zcount("zset", 1.0, 5.0));
        logger.info("查看zset集合中element004的权重："+shardedJedis.zscore("zset", "element004"));
        logger.info("查看下标1到2范围内的元素值："+shardedJedis.zrange("zset", 1, 2));
    }

    /**
     * Hash功能
     */
    private void HashOperation(){
        logger.info("******************************************************************");
        logger.info("****************************hash operation************************");
        logger.info("******************************************************************");
        logger.info("清空redis库中所有数据：{}",jedis.flushDB());

        logger.info("==============  增  ================");
        logger.info("hashs中添加key001和value001键值对："+shardedJedis.hset("hashs", "key001", "value001"));
        logger.info("hashs中添加key002和value002键值对："+shardedJedis.hset("hashs", "key002", "value002"));
        logger.info("hashs中添加key003和value003键值对："+shardedJedis.hset("hashs", "key003", "value003"));
        logger.info("新增key004和4的整型键值对："+shardedJedis.hincrBy("hashs", "key004", 4l));
        logger.info("hashs中的所有值："+shardedJedis.hvals("hashs"));

        logger.info("==============  删  ================");
        logger.info("hashs中删除key002键值对："+shardedJedis.hdel("hashs", "key002"));
        logger.info("hashs中的所有值："+shardedJedis.hvals("hashs"));

        logger.info("==============  改  ================");
        logger.info("key004整型键值的值增加100："+shardedJedis.hincrBy("hashs", "key004", 100l));
        logger.info("hashs中的所有值："+shardedJedis.hvals("hashs"));

        logger.info("==============  查  ================");
        logger.info("判断key003是否存在："+shardedJedis.hexists("hashs", "key003"));
        logger.info("获取key004对应的值："+shardedJedis.hget("hashs", "key004"));
        logger.info("批量获取key001和key003对应的值："+shardedJedis.hmget("hashs", "key001", "key003"));
        logger.info("获取hashs中所有的key："+shardedJedis.hkeys("hashs"));
        logger.info("获取hashs中所有的value："+shardedJedis.hvals("hashs"));
    }
}
