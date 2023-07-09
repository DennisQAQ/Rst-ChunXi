package com.ChunXi.service;

import com.ChunXi.dto.DishDTO;
import com.ChunXi.dto.DishPageQueryDTO;
import com.ChunXi.result.PageResult;
import com.ChunXi.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 添加菜品
     * @param dishDTO
     * @return
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param queryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO queryDTO);

    /**
     * 菜品批量删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询菜品和口味数据
     *
     * @param id
     * @return
     */
    DishVO geyByIdWithFlavors(Long id);

    /**
     * 修改菜品信息
     * @param dishDTO
     */
    void updateWithFlavors(DishDTO dishDTO);

    void setDishStatus(Integer status,Long id);
}
