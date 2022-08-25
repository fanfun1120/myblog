package com.fanfun.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2022-03-27 10:50:29
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User implements Serializable{
    @TableId
    private Long id;

    
    private String avatar;
    
    private Date createTime;
    
    private String email;
    
    private String nickname;
    
    private String password;
    
    private Integer type;
    
    private Date updateTime;
    
    private String username;



}
