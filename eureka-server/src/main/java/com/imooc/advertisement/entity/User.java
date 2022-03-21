package com.imooc.advertisement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @ClassName: User
 * @Description: TODO
 * @author: yourName
 * @date: 2020年08月29日 13:49
 */
@Setter
@Getter
@ToString
@TableName("mp_user")
public class User {
    private static int aged = 0;

    public void agg() {
        System.out.println("aged ===== " + aged);

        aged++;
    }

    @TableId
    private Long userId;

    @TableField("real_name")
    private String name;

    private Integer age;

    private String email;

    private Long managerId;

    private Date createTime;
}
