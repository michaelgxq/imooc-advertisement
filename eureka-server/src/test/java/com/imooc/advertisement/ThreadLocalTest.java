package com.imooc.advertisement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName: ThreadLocalTest
 * @Description: TODO
 * @author: yourName
 * @date: 2021年12月04日 9:55
 */
@SpringBootTest

@RunWith(SpringRunner.class)
public class ThreadLocalTest {

    @Test
    public void test() throws IOException {
        File file = new File("D:\\", "abc.ok");

        boolean newFile = file.createNewFile();
    }
}
