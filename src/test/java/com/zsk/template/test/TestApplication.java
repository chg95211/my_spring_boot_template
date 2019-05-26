package com.zsk.template.test;

import com.zsk.template.Application;
import com.zsk.template.config.exception.ParameterException;
import com.zsk.template.config.mq.LogSearchSender;
import com.zsk.template.dao.TtestDao;
import com.zsk.template.dao.TtestSubDao;
import com.zsk.template.dao.es.TTestEsDao;
import com.zsk.template.model.SearchLog;
import com.zsk.template.model.Ttest;
import com.zsk.template.model.TtestSub;
import com.zsk.template.util.ResourcesUtil;
import com.zsk.template.util.SnowflakeId;
import com.zsk.template.util.jedis.JedisClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Date;

/**
 * @description:
 * @author: zsk
 * @create: 2019-05-11 16:45
 **/
@Slf4j
@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
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

    @Autowired
    private TTestEsDao tTestEsDao;

    @Test
    public void testCreateIndex(){
        System.out.println("需要有es dao和 model类才会自动创建");
    }


    @Test
    public void testSaveIndex()
    {
        Ttest ttestWithSubs2 = ttestDao.getTtestWithSubs2(1);
        tTestEsDao.save(ttestWithSubs2);
    }

    @Autowired
    private LogSearchSender logSearchSender;

    @Autowired
    private SnowflakeId snowflakeId;

    @Test
    public void testMq()
    {
        logSearchSender.sendLogSearchLogMsg(SearchLog.builder().id(String.valueOf(snowflakeId.nextId())).q("test").date(new Date()).build());
    }
}
