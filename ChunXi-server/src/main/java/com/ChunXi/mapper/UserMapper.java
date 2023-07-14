package com.ChunXi.mapper;


import com.ChunXi.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {

    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    void insert(User user);

    @Select("SELECT * from user where id=#{userId}")
    User getById(Long userId);


    Integer countByMap(Map map);
}
