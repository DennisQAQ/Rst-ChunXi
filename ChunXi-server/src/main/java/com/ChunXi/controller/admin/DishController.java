package com.ChunXi.controller.admin;

import com.ChunXi.constant.MessageConstant;
import com.ChunXi.dto.DishDTO;
import com.ChunXi.dto.DishPageQueryDTO;
import com.ChunXi.entity.Dish;
import com.ChunXi.result.PageResult;
import com.ChunXi.result.Result;
import com.ChunXi.service.DishService;
import com.ChunXi.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@Api(tags = "菜品管理功能")
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 添加菜品
     * @param dishDTO
     * @return
     */
    @ApiOperation("添加菜品")
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO){
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    @ApiOperation("菜品分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO queryDTO){
        PageResult result = dishService.pageQuery(queryDTO);
        return Result.success(result);
    }

    @ApiOperation("菜品批量删除")
    @DeleteMapping
    public Result delete (@RequestParam List<Long> ids){

        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @ApiOperation("根据id查询菜品")
    @GetMapping("/{id}")
    public Result<DishVO> getByid(@PathVariable Long id){
        DishVO dishVO = dishService.geyByIdWithFlavors(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品信息
     * @param dishDTO
     * @return
     */
    @ApiOperation("修改菜品信息")
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){
        dishService.updateWithFlavors(dishDTO);
        return Result.success(MessageConstant.UPDATE_SUCCSESS);
    }

    @ApiOperation("修改菜品售卖状态")
    @PostMapping("/status/{status}")
    public Result setDishStatus (@PathVariable Integer status,Long id){
        log.info("id是{}",id);
        dishService.setDishStatus(status,id);

        return Result.success(MessageConstant.UPDATE_SUCCSESS);
    }

    @ApiOperation("根据分类id查询菜品")
    @GetMapping("/list")
    public Result<List<Dish>> list( Long categoryId){
        log.info("id是{}",categoryId);

        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }
}
