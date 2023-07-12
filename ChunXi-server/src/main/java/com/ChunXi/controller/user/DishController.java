package com.ChunXi.controller.user;

import com.ChunXi.constant.StatusConstant;
import com.ChunXi.entity.Dish;
import com.ChunXi.result.Result;
import com.ChunXi.service.DishService;
import com.ChunXi.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "C端-菜品浏览接口")
@Slf4j
@RestController("userDishController")
@RequestMapping("/user/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    @Cacheable(cacheNames = "setmealCache",key = "#categoryId") //key: setmealCache::100
    public Result<List<DishVO>> list(Long categoryId) {
        //构造redis中的key
//        String key = "dish"+categoryId;
        //查询reids中是否有缓存数据
//        List<DishVO>list = (List<DishVO>) redisTemplate.opsForValue().get(key);
//        if (list!=null&&list.size()>0){
            //如果缓存中存在数，直接返回
//            return Result.success(list);
//        }
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);
        //如果不存在，查询数据库并将结果存入redis缓存中
        List<DishVO>list= dishService.listWithFlavor(dish);
//        redisTemplate.opsForValue().set(key,list);
        return  Result.success(list);
    }
}
