package com.ChunXi.controller.user;


import com.ChunXi.entity.Category;
import com.ChunXi.mapper.CategoryMapper;
import com.ChunXi.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "C端-分类接口")
@Slf4j
@RestController("userCategoryController")
@RequestMapping("/user/category")
public class CategoryController {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据分类id查询菜品
     * @param type
     * @return categoryList
     */
    @ApiOperation("根据分类id查询菜品")
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type){
        List<Category> categoryList = categoryMapper.list(type);
        return Result.success(categoryList);
    }
}
