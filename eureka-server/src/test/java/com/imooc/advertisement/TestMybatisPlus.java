package com.imooc.advertisement;

import com.imooc.advertisement.dao.UserMapper;
import com.imooc.advertisement.entity.User;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.MappedByteBuffer;
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
    public void insert() throws IOException {
//        User user = new User();
//
//        user.setName("刘明强");
//        user.setAge(31);
//        user.setManagerId(1087982257332887553L);
//        user.setCreateTime(new Date());
////        int insert = userMapper.insert(user);
////
////        System.out.println("影响记录数：" + insert);
//
//        this.changeName(user);
//
//        System.out.println(user.getName());


//        RandomAccessFile rw = new RandomAccessFile(new File("C:\\Users\\24318\\Desktop\\test.txt"), "rw");
//        RandomAccessFile rw1 = new RandomAccessFile(new File("C:\\Users\\24318\\Desktop\\test_1.txt"), "rw");
//
//        String s = rw.readLine();
//        String encode = URLEncoder.encode(s, "UTF-8");
////        System.out.println("str ============= " +  URLDecoder.decode(encode, "UTF-8"));
//        System.out.println("str ============= " +  encode);
//        System.out.println("str ============= " +  new String(encode.getBytes(), "UTF-8"));
//        System.out.println("str ============= " +  URLDecoder.decode(encode, "UTF-8"));

        saveAsUTF8("C:\\\\Users\\\\24318\\\\Desktop\\\\test.txt","C:\\\\Users\\\\24318\\\\Desktop\\\\test_1.txt");
//        System.out.println("str ========== " + new String(s.getBytes("GB2312"),"UTF8"));
//        rw1.writeBytes(s);
//        rw1.writeBytes("\n");
//        s = rw.readLine();
//        rw1.seek(rw1.length());
//        rw1.writeBytes(s);
    }

    public static void saveAsUTF8(String inputFileUrl, String outputFileUrl) throws IOException {
        System.out.println("inputFileEncode===" + "GB2312");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFileUrl), "GB2312"));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileUrl), "UTF-8"));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            bufferedWriter.write(line + "\r\n");
        }
        bufferedWriter.close();
        bufferedReader.close();
//        String outputFileEncode = EncodingDetect.getJavaEncode(outputFileUrl);
//        System.out.println("outputFileEncode===" + outputFileEncode);
        System.out.println("txt文件格式转换完成");
    }

    private void changeName(User user) {
        user.setName("哈哈哈");
    }
}
