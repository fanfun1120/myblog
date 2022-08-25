package com.fanfun.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (BlogTags)表实体类
 *
 * @author makejava
 * @since 2022-03-30 22:34:50
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_blog_tags")
public class BlogTags implements Serializable{

    
    private Long blogsId;
    
    private Long tagsId;



}
