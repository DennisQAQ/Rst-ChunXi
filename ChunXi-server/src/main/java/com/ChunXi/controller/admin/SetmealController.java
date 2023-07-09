package com.ChunXi.controller.admin;

import com.ChunXi.constant.MessageConstant;
import com.ChunXi.dto.SetmealDTO;
import com.ChunXi.dto.SetmealPageQueryDTO;
import com.ChunXi.result.PageResult;
import com.ChunXi.result.Result;
import com.ChunXi.service.SetmealService;
import com.ChunXi.vo.SetmealVO;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "套餐管理")
@RestController
@RequestMapping("admin/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @ApiOperation("/套餐分页查询")
    @GetMapping("/page")
    public Result <PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @ApiOperation("新增套餐")
    @PostMapping
    public Result save (@RequestBody SetmealDTO setmealDTO){
        setmealService.saveWithDish(setmealDTO);
        return Result.success(MessageConstant.SAVE_SUCCESS);
    }

    /**
     * 根据主键获取管理的菜品信息
     * @param id
     * @return
     */
    @ApiOperation("根据主键获取管理的菜品信息")
    @GetMapping("/{id}")
    public Result<SetmealVO> list (@PathVariable Long id){
        log.info("id{}",id);
        SetmealVO dtoList = setmealService.getByIdWithDish(id);
        return Result.success(dtoList);
    }

    /**
     * 修改套餐
     * @param setmealDTO
     * @return
     */
    @ApiOperation("修改套餐")
    @PutMapping
    public Result  update (@RequestBody SetmealDTO setmealDTO){
        setmealService.update(setmealDTO);
        return Result.success();
    }

    @ApiOperation("批量删除套餐")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        log.info("要删除的id是{}",ids.toString());
        setmealService.deleteBatch(ids);
        return Result.success();

    }
    @ApiOperation("套餐起售停售")
    @PostMapping("/status/{status}")
    public  Result setSetmealStatus(@PathVariable Integer status,long id){
        log.info("修改的id是{}状态{}",id,status);
        setmealService.setSetmealtatus(status,id);
        return Result.success(MessageConstant.UPDATE_SUCCSESS);
    }
}
