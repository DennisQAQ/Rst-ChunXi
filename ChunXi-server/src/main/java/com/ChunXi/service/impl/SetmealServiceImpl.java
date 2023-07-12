package com.ChunXi.service.impl;

import com.ChunXi.constant.MessageConstant;
import com.ChunXi.constant.StatusConstant;
import com.ChunXi.dto.SetmealDTO;
import com.ChunXi.dto.SetmealPageQueryDTO;
import com.ChunXi.entity.Dish;
import com.ChunXi.entity.Setmeal;
import com.ChunXi.entity.SetmealDish;
import com.ChunXi.exception.DeletionNotAllowedException;
import com.ChunXi.exception.SetmealEnableFailedException;
import com.ChunXi.mapper.DishMapper;
import com.ChunXi.mapper.SetmealDishMapper;
import com.ChunXi.mapper.SetmealMapper;
import com.ChunXi.result.PageResult;
import com.ChunXi.service.SetmealService;
import com.ChunXi.vo.DishItemVO;
import com.ChunXi.vo.SetmealVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;
    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page setmealVO = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(setmealVO.getTotal(),setmealVO.getResult());
    }

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    public void saveWithDish(SetmealDTO setmealDTO) {
        //添加setmeal基本信息
        Setmeal setmeal = new Setmeal();

        BeanUtils.copyProperties(setmealDTO,setmeal);

        setmealMapper.save(setmeal);

        Long id = setmeal.getId();

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes!=null&&setmealDishes.size()>0){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(id);
            });
        }
        setmealMapper.insertBatch(setmealDishes);

    }

    /**
     * 根据主键获取管理的菜品信息
     * @param id
     * @return
     */
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        //查询setmeal表基本信息
        Setmeal setmeal=setmealMapper.getById(id);
        SetmealVO setmealVO = new SetmealVO();

        List<SetmealDish> setmealDish = setmealMapper.getBySetmealId(id);
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDish);

        return setmealVO;
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {

        //1、修改套餐表，执行update
        Setmeal setmeal =new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
        //套餐id
        Long setmealId = setmeal.getId();
        //2、删除套餐和菜品的关联关系，操作setmeal_dish表，执行delete
        setmealDishMapper.deleteDishBysetmealId(setmealId);
        //3、重新插入套餐和菜品的关联关系，操作setmeal_dish表，执行insert
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setDishId(setmealId);
        });
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    public void deleteBatch(List<Long> ids) {

        for (Long id: ids) {
            log.info("要删除的id是{}",id);
            Setmeal setmeal = new Setmeal();
            Setmeal byId = setmealMapper.getById(id);
            if (byId.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }
        }
        for (Long id: ids) {
            log.info("要删除的id是{}",id);
            //删除套餐表中的数据
            setmealMapper.deleteById(id);
            //删除套餐菜品关系表中的数据
            setmealDishMapper.deleteBySetmealId(id);
        }
    }

    /**
     * 套餐起售停售
     * @param status
     * @param id
     */
    @Override
    public void setSetmealtatus(Integer status, long id) {
        //起售套餐时，判断套餐内是否有停售菜品，有停售菜品提示"套餐内包含未启售菜品，无法启售"
        if (status==StatusConstant.ENABLE){
            //select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id =
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            if (dishList != null && dishList.size()>0){
                dishList.forEach(dish -> {
                    if (StatusConstant.DISABLE==dish.getStatus()){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        Setmeal setmeal = Setmeal.builder().status(status).id(id).build();
        setmealMapper.update(setmeal);

    }

    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
