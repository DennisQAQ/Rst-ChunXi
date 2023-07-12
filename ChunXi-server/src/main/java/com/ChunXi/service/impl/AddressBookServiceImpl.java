package com.ChunXi.service.impl;

import com.ChunXi.constant.MessageConstant;
import com.ChunXi.context.BaseContext;
import com.ChunXi.entity.AddressBook;
import com.ChunXi.exception.DeletionNotAllowedException;
import com.ChunXi.mapper.AddressBookMapper;
import com.ChunXi.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 新增地址
     * @param addressBook
     */
    @Override
    public void save(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        //设置为非默认
        addressBook.setIsDefault(0);
        addressBookMapper.save(addressBook);
    }


    /**
     * 查询当前登录用户的所有地址信息
     * @param addressBook
     * @return
     */
    @Override
    public List<AddressBook> list(AddressBook addressBook) {
        List<AddressBook>addressBookList=addressBookMapper.list(addressBook);
        return addressBookList;
    }

    /**
     * 设置默认地址
     * @param addressBook
     */
    @Override
    public void setDefault(AddressBook addressBook) {
        //1、将当前用户的所有地址修改为非默认地址 update address_book set is_default = ? where user_id = ?
        addressBook.setIsDefault(0);
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.updateIsDefaultByUserId(addressBook);
        //2、将当前地址改为默认地址 update address_book set is_default = ? where id = ?
        addressBook.setIsDefault(1);

        addressBookMapper.update(addressBook);
    }

    /**
     * 查询默认地址
     * @return
     */
    @Override
    public List<AddressBook> getDefault() {

        return null;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return address_book
     */
    public AddressBook getById(Long id) {
        AddressBook addressBook = addressBookMapper.getById(id);
        return addressBook;
    }

    /**
     * 根据id修改地址
     *
     * @param addressBook
     */
    @Override
    public void update(AddressBook addressBook) {

        addressBookMapper.update(addressBook);

    }

    /**
     * 根据id删除地址
     * @param id
     */
    @Override
    public void delete(Long id) {
        //判断要删除的地址否是默认地址，默认地址不予删除
        AddressBook addressBook = addressBookMapper.getById(id);
        Integer isDefault = addressBook.getIsDefault();

        if (isDefault==null){
            throw new DeletionNotAllowedException(MessageConstant.DELETE_IS_DEFAULT);
        }
        addressBookMapper.delete(id);
    }
}
