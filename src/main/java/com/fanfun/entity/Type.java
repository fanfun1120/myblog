package com.fanfun.entity;


import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Type)表实体类
 *
 * @author makejava
 * @since 2022-03-27 11:30:31
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_type")
public class Type  implements Serializable{
    @TableId
    private Long id;
    private String name;

    @TableField(exist = false)
    private List<Blog> blogs;

}
