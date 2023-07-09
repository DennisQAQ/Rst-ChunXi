package com.ChunXi.service;

import com.ChunXi.dto.SetmealDTO;
import com.ChunXi.dto.SetmealPageQueryDTO;
import com.ChunXi.result.PageResult;
import com.ChunXi.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐
     * @param setmealDTO
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 根据主键获取管理的菜品信息
     * @param id
     * @return
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * 修改套餐
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteBatch(List<Long> ids);


    /**
     * 套餐起售停售
     * @param status
     * @param id
     */
    void setSetmealtatus(Integer status, long id);
}
