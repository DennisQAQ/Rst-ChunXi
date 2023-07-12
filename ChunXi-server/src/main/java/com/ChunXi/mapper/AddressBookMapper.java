package com.ChunXi.mapper;

import com.ChunXi.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    /**
     * 新增地址
     * @param addressBook
     */
    @Insert("insert into address_book" +
            "(user_id, consignee, phone, sex, province_code, province_name, city_code, city_name, district_code," +
            "district_name, detail, label, is_default)" +
            "values (#{userId}, #{consignee}, #{phone}, #{sex}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}," +
            "#{districtCode}, #{districtName}, #{detail}, #{label}, #{isDefault})")
    void save(AddressBook addressBook);

    /**
     * 查询当前登录用户的所有地址信息---根据id查询address_book
     * @param addressBook
     * @return
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 设置默认地址
     * @param addressBook
     */
    @Update("update address_book set is_default=#{isDefault} where user_id= #{userId}")
    void updateIsDefaultByUserId(AddressBook addressBook);

    /**
     * 根据id修改
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     * 根据id查询
     *
     * @param id
     * @return address_book
     */
    @Select("select  * from address_book where id = #{id}")
    AddressBook getById(Long id);

    /**
     * 根据id删除地址
     * @param id
     */
    @Delete("delete from address_book where id = #{id} ")
    void delete(Long id);
}
