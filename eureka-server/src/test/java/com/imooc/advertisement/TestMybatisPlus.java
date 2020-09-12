package com.imooc.advertisement;

import com.imooc.advertisement.dao.UserMapper;
import com.imooc.advertisement.entity.User;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: TestMybatisPlus
 * @Description: TODO
 * @author: yourName
 * @date: 2020年08月29日 14:04
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMybatisPlus {

    @Autowired
    private UserMapper userMapper;

    @Test
    @Ignore
    public void select() {
        List<User> users = userMapper.selectList(null);

        System.out.println(users);
    }

    @Test
    public void insert() {
        User user = new User();

        user.setName("刘明强");
        user.setAge(31);
        user.setManagerId(1087982257332887553L);
        user.setCreateTime(new Date());
        int insert = userMapper.insert(user);

        System.out.println("影响记录数：" + insert);
    }
}
