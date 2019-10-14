package generator.config;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author liufei
 * @date 2019/5/17 10:02
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseControllerTest {
    private static Logger logger = LoggerFactory.getLogger(BaseControllerTest.class);

    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mvc;

    protected MockHttpSession session;

    /**
     * 初始化MockMvc对象
     */
    @Before
    public void initMockMvc(){
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        session = new MockHttpSession();
        /*User user =new User("root","root");
        session.setAttribute("user",user); //拦截器那边会判断用户是否登录，所以这里注入一个用户*/
        System.out.println("测试开始==================");
    }

    @After
    public void afterTest(){
        System.out.println("测试结束*******************");
    }
}
