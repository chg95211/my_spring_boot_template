package com.zsk.template.test;

import com.google.common.base.Charsets;
import com.google.common.hash.*;
import com.zsk.template.Application;
import com.zsk.template.config.aop.annotation.CostTime;
import com.zsk.template.config.exception.ParameterException;
//import com.zsk.template.config.mq.LogSearchSender;
import com.zsk.template.config.ratelimit.RateLimiter;
import com.zsk.template.config.ratelimit.RateLimiterFactory;
import com.zsk.template.constant.TtestStatus;
import com.zsk.template.dao.TtestDao;
import com.zsk.template.dao.TtestSubDao;
//import com.zsk.template.dao.es.TTestEsDao;
import com.zsk.template.model.Ttest;
import com.zsk.template.model.TtestSub;
import com.zsk.template.service.TestService;
import com.zsk.template.util.ResourcesUtil;
import com.zsk.template.util.jedis.JedisClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static junit.framework.TestCase.assertTrue;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-11 16:45
 **/
@Slf4j
//@SpringBootTest(classes = Application.class)
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//@WebMvcTest
public class TestApplication
{


    @Resource
    DataSource dataSource;

    @Test
    public void testDataSource() throws SQLException
    {
        System.out.println(dataSource.getConnection());
    }

    @Resource
    JavaMailSender mailSender;

    @Test
    public void testMail()
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailSender.send(mailMessage);
    }


    @Test
    public void testException()
    {
        ParameterException exception = new ParameterException("test");
        log.error("系统错误", exception);
    }


    @Autowired
    JedisClient jedisSingleClient;

    @Autowired
    JedisClient jedisClusterClient;

    @Test
    public void testRedis()
    {
        jedisSingleClient.set("test1", "jedisSingle");
        System.out.println("==========================");
        System.out.println(jedisSingleClient.get("test1"));

        jedisClusterClient.set("test2", "jedisCluster");
        System.out.println("==========================");
        System.out.println(jedisClusterClient.get("test2"));
    }


    @Autowired
    ResourcesUtil resourcesUtil;

    @Test
    public void testResource()
    {
        System.out.println(resourcesUtil.getMessage("welcom.msg", "zsk"));
    }

    @Autowired
    private TtestDao ttestDao;

    @Test
    public void testEnum()
    {
        Ttest ttest = this.ttestDao.selectByPrimaryKey(1);
        System.out.println(ttest);
    }


//    @Test
//    public void testEnum2()
//    {
//        Ttest ttest = new Ttest();
//        ttest.setId(4);
//        ttest.setStatus(TtestStatus.closed);
//        this.ttestDao.insertSelective(ttest);
//
//    }
//
//
//    @Test
//    public void testEnum3()
//    {
//        Ttest ttest = new Ttest();
//        ttest.setId(5);
//        ttest.setStatus(TtestStatus.closed);
//        this.ttestDao.insertRecode(ttest);
//
//    }

    @Test
    public void testPostgreJson()
    {
        Ttest ttestWithSubs = ttestDao.getTtestWithSubs(3);
        System.out.println(ttestWithSubs);
    }

    @Test
    public void testMybatisCollections()
    {
        Ttest ttestWithSubs = ttestDao.getTtestWithSubs2(1);
        System.out.println(ttestWithSubs);
    }

    @Autowired
    private TtestSubDao subDao;

    @Test
    public void testMybatisAssociation()
    {
        TtestSub subWithTest = subDao.getSubWithTest(1);
        System.out.println(subWithTest);
    }

//    @Autowired
//    private TTestEsDao tTestEsDao;

    @Test
    public void testCreateIndex(){
        System.out.println("需要有es dao和 model类才会自动创建");
    }

//    @Autowired
//    private ElasticsearchTemplate elasticsearchTemplate;

//    @Test
//    public void testElasticsearchTemplate()
//    {
//        elasticsearchTemplate.refresh(Ttest.class);
//    }

//    @Test
//    public void testSaveIndex()
//    {
//        Ttest ttestWithSubs2 = ttestDao.getTtestWithSubs2(1);
//        tTestEsDao.save(ttestWithSubs2);
//    }

//    @Autowired
//    private LogSearchSender logSearchSender;
//
//    @Autowired
//    private SnowflakeId snowflakeId;
//
//    @Test
//    public void testMq()
//    {
//        logSearchSender.sendLogSearchLogMsg(SearchLog.builder().id(String.valueOf(snowflakeId.nextId())).q("test").date(new Date()).build());
//    }

//    @Test
//    public void testSqlRef()
//    {
//        Ttest ttest = new Ttest();
//        ttest.setStatus(TtestStatus.open);
//        this.ttestDao.insert2(ttest);
//    }


    @Autowired
    private TestService testService;
    /*先看二级缓存，没有再去一级缓存缓存找？？？？*/
    @Test
    public void testCache1()
    {
        //缓存失效的情况----------|
        //                      |-----sqlsession不同。在springboot中必须用@Transactional开启一级缓存
        //                      |-----用了不同的查询条件
        //                      |-----两次查询之间执行增删改
        //                      |-----手动调用sqlsession清空缓存
//
        testService.getById(1);
        testService.getById(1);

    }

    @Test
    public void testCache2()
    {
//        @CacheNamespace mapper上开启二级缓存功能，每个mapper对应一个
//缓存失效的情况
//          |-----mapper中flushCache=true会清空一级和二级缓存
//          |-----useCache=false，不使用二级缓存，仍使用一级缓存
//          |-----两次查询之间执行增删改

        Ttest ttest = this.ttestDao.getTtestById(1);
        ttestDao.getTtestById(1);
//        this.ttestDao.getTtestByStatus("open");

    }

    @Autowired
    private RateLimiterFactory limiterFactory;

    @Test
    public void testRedisRateLimiter() throws Exception
    {
        RateLimiter limiter = limiterFactory.build("testRedisKey", 10.0, 1);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++)
        {
            limiter.acquire();
//            System.out.println(System.currentTimeMillis() + ":"  + i + ": 正在访问");
        }
        long end = System.currentTimeMillis();

        System.out.println(end - start);//13581，加锁耗时？？

        com.google.common.util.concurrent.RateLimiter limiter1 = com.google.common.util.concurrent.RateLimiter.create(10.0);
        for (int i = 0; i < 100; i++)
        {
            limiter1.acquire();
//            System.out.println(System.currentTimeMillis() + ":"  + i + ": 正在访问");
        }

        long end2 = System.currentTimeMillis();
        System.out.println(end2 - end);//9906

        RateLimiter limiter3 = limiterFactory.build("testRedisKey2", 10.0, 1);

        CyclicBarrier cyclicBarrier = new CyclicBarrier(100);
        CountDownLatch latch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++)
        {
            Thread thread = new Thread(()->{
                try
                {
                    cyclicBarrier.await();
                    limiter3.acquire();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }finally
                {
                    latch.countDown();
                }

            });
            thread.start();
        }
        latch.await();
        long end3 = System.currentTimeMillis();
        System.out.println(end3 - end2);//10511
    }

    @Test
    public void testBloomFilter()
    {
        BloomFilter<Integer> integerBloomFilter = BloomFilter.create(Funnels.integerFunnel(), 1024 * 1024 * 32, 0.0000001d);
        integerBloomFilter.put(1);
        integerBloomFilter.put(2);
        integerBloomFilter.put(3);

        boolean c4 = integerBloomFilter.mightContain(4);
        boolean c3 = integerBloomFilter.mightContain(3);
        System.out.println(c4);
        System.out.println(c3);

    }

    private static final int TOTAL = 10000;
    private static final double FPP = 0.0005;

    @Test
    public void testMyRedisBloomFilter()
    {
        RedisBloomFilter redisBloomFilter = RedisBloomFilter.create(TOTAL, FPP);
        redisBloomFilter.resetBitmap();

        BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), TOTAL, FPP);

        IntStream.range(0, TOTAL).boxed().map(i -> Hashing.md5().hashInt(i).toString()).collect(toList()).forEach(s -> {
            redisBloomFilter.put(s);
            bloomFilter.put(s);
        });

        String str1 = Hashing.md5().hashInt(99999).toString();
        String str2 = Hashing.md5().hashInt(9999).toString();
        String str3 = "abcdefghijklmnopqrstuvwxyz123456";
        System.out.println(redisBloomFilter.mightContain(str1) + ":" + bloomFilter.mightContain(str1));
        System.out.println(redisBloomFilter.mightContain(str2) + ":" + bloomFilter.mightContain(str2));
        System.out.println(redisBloomFilter.mightContain(str3) + ":" + bloomFilter.mightContain(str3));
    }

    public  Future<String> calculateAsync() throws InterruptedException
    {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(500);
            completableFuture.complete("Hello");
            return null;
        });
        return completableFuture;
    }

    @Test
    public void testFuture() throws InterruptedException, ExecutionException
    {
        Future<String> future = calculateAsync();
        System.out.println(future.get());
    }

    @Test
    @CostTime
    public void testMultiFuture() throws ExecutionException, InterruptedException
    {
        System.out.println("start runnning............");
        long start = System.currentTimeMillis();
        CompletableFuture<String> future1
                = CompletableFuture.supplyAsync(() ->
                {
                    try
                    {
                        TimeUnit.SECONDS.sleep(5);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    System.out.println("Hello" + Thread.currentThread().getName());
                    return "Hello";
                }
        );
        CompletableFuture<String> future2
                = CompletableFuture.supplyAsync(() ->
                {
                    try
                    {
                        TimeUnit.SECONDS.sleep(8);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    System.out.println("Beautiful" + Thread.currentThread().getName());

                    return "Beautiful";
                }
        );
        CompletableFuture<String> future3
                = CompletableFuture.supplyAsync(() ->
                {
                    try
                    {
                        TimeUnit.SECONDS.sleep(10);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    System.out.println("World" + Thread.currentThread().getName());

                    return "World";
                }
        );

        CompletableFuture<Void> combinedFuture
                = CompletableFuture.allOf(future1, future2, future3);


        combinedFuture.get();

        long end = System.currentTimeMillis();

        System.out.println("finish run...time is " + (end-start));

        assertTrue(future1.isDone());
        assertTrue(future2.isDone());
        assertTrue(future3.isDone());

        System.out.println(future1.get());
        System.out.println(future2.get());
        System.out.println(future3.get());

    }

    @Test
    public void testCompletableFuture1() throws ExecutionException, InterruptedException
    {
        CompletableFuture<String> stringCompletableFuture = new CompletableFuture<>();

        new Thread(()->{
            try
            {
                TimeUnit.SECONDS.sleep(5);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

           stringCompletableFuture.complete("手动完成任务");
        }).run();

        String s = stringCompletableFuture.get();
        System.out.println(s);
    }

    @Test
    public void testCompletableFuture2() throws ExecutionException, InterruptedException
    {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try
            {
                TimeUnit.SECONDS.sleep(5);
            }
            catch (InterruptedException e)
            {
                throw new IllegalStateException(e);
            }
            System.out.println("后台任务完成");
        });

        future.get();
    }

    @Test
    public void testCompletableFuture3() throws ExecutionException, InterruptedException
    {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try
            {
                TimeUnit.SECONDS.sleep(5);
            }
            catch (InterruptedException e)
            {
                throw new IllegalStateException(e);
            }
           return "后台任务完成";
        });

        String s = future.get();
        System.out.println(s);
    }

    @Test
    public void testCompletableFuture4() throws ExecutionException, InterruptedException
    {
        System.out.println("start");
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try
            {
                TimeUnit.SECONDS.sleep(5);
            }
            catch (InterruptedException e)
            {
                throw new IllegalStateException(e);
            }
            return "后台任务完成";
        });

        future.thenAccept(System.out::println);

        System.out.println("主线程继续执行并且休眠10s");

        TimeUnit.SECONDS.sleep(10);
    }


    @Test
    public void testCompletableFuture5() throws ExecutionException, InterruptedException
    {
        System.out.println("start");
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try
            {
                TimeUnit.SECONDS.sleep(5);
            }
            catch (InterruptedException e)
            {
                throw new IllegalStateException(e);
            }
            return "后台任务完成";
        });

        future.thenApply(s->{
            System.out.println(Thread.currentThread().getName() + "s");
            return s;
        }).thenApply(s->{
            System.out.println(Thread.currentThread().getName() + "s");
            return s;
        });

        System.out.println("主线程继续执行并且休眠10s");

        TimeUnit.SECONDS.sleep(10);
    }
    
    
    @Test
    public void testExcetion() throws ExecutionException, InterruptedException
    {
        CompletableFuture<Object> future = CompletableFuture.supplyAsync(() -> {
                throw new IllegalArgumentException("Age can not be negative");
        }).exceptionally(ex -> {
            System.out.println("Oops! We have an exception - " + ex.getMessage());
            return "Unknown!";
        });

        System.out.println(future.get());
    }

}
