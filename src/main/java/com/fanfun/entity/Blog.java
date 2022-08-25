package com.fanfun.entity;

import java.util.Date;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Blog)表实体类
 *
 * @author makejava
 * @since 2022-03-26 21:01:43
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_blog")
public class Blog  implements Serializable{
    @TableId
    private Long id;

    
    private String commentabled;
    
    private String content;
    
    private Date createTime;
    
    private String description;
    
    private String firstPicture;
    
    private String flag;
    
    private String published;
    
    private String recommend;
    
    private String title;
    
    private Date updateTime;
    
    private Integer views;

    @TableField(exist = false)
    private Type type;

    private Long typeId;

    @TableField(exist = false)
    private User user;

    private Long userId;

    @TableField(exist = false)
    private List<Tag> tags;

    @TableField(exist = false)
    private String tagIds;

    public void init() {
        this.tagIds = tagsToIds(this.getTags());
    }

    private String tagsToIds(List<Tag> tags) {
        if (!tags.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return tagIds;
        }
    }


}
