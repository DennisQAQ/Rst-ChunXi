package com.ChunXi.controller.user;

import com.ChunXi.context.BaseContext;
import com.ChunXi.entity.AddressBook;
import com.ChunXi.result.Result;
import com.ChunXi.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "C端-用户地址簿功能")
@RestController
@RequestMapping("/user/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;


    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    @ApiOperation("新增地址")
    @PostMapping()
    public Result save(@RequestBody AddressBook addressBook){

        log.info("前端传参{}",addressBook);
        addressBookService.save(addressBook);
        return Result.success();
    }

    /**
     * 查询当前登录用户的所有地址信息
     * @return
     */
    @ApiOperation("查询当前登录用户的所有地址信息")
    @GetMapping("/list")
    public Result<List<AddressBook>> list() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> addressBookList = addressBookService.list(addressBook);
        return Result.success(addressBookList);
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @ApiOperation("设置默认地址")
    @PutMapping("/default")
    public Result setDefault(@RequestBody AddressBook addressBook){
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * 查询默认地址
     * @return
     */
    @ApiOperation("查询默认地址")
    @GetMapping("/default")
    public Result <AddressBook> getDefault(){
        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(1);
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);

        if (list != null && list.size() == 1) {
            return Result.success(list.get(0));
        }

        return Result.error("没有查询到默认地址");
    }


    /**
     * 根据id修改地址
     * @param addressBook
     * @return
     */
    @ApiOperation("根据id修改地址")
    @PutMapping
    public Result updateById(@RequestBody AddressBook addressBook){
        //TODO
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * 根据id 查询地址
     * @param id
     * @return address_book
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }


    /**
     * 根据id删除地址
     * @param id
     * @return
     */
    @DeleteMapping
    public Result delete (Long id){
        log.info("要删除的id{}",id);
        addressBookService.delete(id);
        return Result.success();
    }
}
