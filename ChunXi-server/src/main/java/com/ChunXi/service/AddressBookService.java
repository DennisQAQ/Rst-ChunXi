package com.ChunXi.service;

import com.ChunXi.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    /**
     * 新增地址
     * @param addressBook
     */
    void save(AddressBook addressBook);

    /**
     * 查询当前登录用户的所有地址信息
     * @param addressBook
     * @return
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 设置默认地址
     * @param addressBook
     */
    void setDefault(AddressBook addressBook);

    /**
     * 查询默认地址
     * @return
     */
    List<AddressBook> getDefault();


    /**
     * 根据id 查询地址
     * @param id
     * @return address_book
     */
    AddressBook getById(Long id);

    void update(AddressBook addressBook);

    void delete(Long id);
}
