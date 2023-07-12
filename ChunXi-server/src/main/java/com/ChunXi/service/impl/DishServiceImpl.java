package com.ChunXi.service.impl;

import com.ChunXi.constant.MessageConstant;
import com.ChunXi.constant.StatusConstant;
import com.ChunXi.dto.DishDTO;
import com.ChunXi.dto.DishPageQueryDTO;
import com.ChunXi.entity.Dish;
import com.ChunXi.entity.DishFlavor;
import com.ChunXi.exception.DeletionNotAllowedException;
import com.ChunXi.mapper.DishFlavorMapper;
import com.ChunXi.mapper.DishMapper;
import com.ChunXi.mapper.SetmealDishMapper;
import com.ChunXi.result.PageResult;
import com.ChunXi.service.DishService;
import com.ChunXi.vo.DishVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
@Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 添加菜品
     * @param dishDTO
     * @return
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish =new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insert(dish);
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors!=null&&flavors.size()>0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品分页查询
     * @param queryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(),queryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(queryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     *
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        log.info("需要删除id{}",ids.toString());
        //判断当前菜品是否能够删除---是否存在启售中的菜品
        for (Long id:ids){
            Dish dish = dishMapper.getById(id);
//            log.info("1111{}",dish.toString());
            if (dish.getStatus()== StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //判断当前菜品是否被套餐管理
        List<Long> list = setmealDishMapper.getSetmealIdsByDishIds(ids);
        log.info("需要删除id list{}",list.toString());
        if (list!=null && list.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);

        }
        //删除菜品表中的菜品口味数据
        for (Long id: ids) {
            dishMapper.deleteById(id);
            dishFlavorMapper.deleteByDishId(id);
        }

    }

    /**
     * 根据id查询菜品和口味数据
     *
     * @param id
     * @return
     */
    @Override
    public DishVO geyByIdWithFlavors(Long id) {
        //根据id查询菜品数据
        Dish dish = new Dish();
        dish = dishMapper.getById(id);
        //根据菜品id查询口味数据
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);
        //将数据封装到VO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(flavors);

        return dishVO;
    }

    /**
     * 修改菜品信息
     * @param dishDTO
     */
    @Override
    public void updateWithFlavors(DishDTO dishDTO) {
        //更新菜品表基本消息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);

        //删除口味表数据
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        //插入新的口味表数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors!=null&&flavors.size()>0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public void setDishStatus(Integer status,Long id) {
        Dish dish = Dish.builder().status(status).id(id).build();
        dishMapper.update(dish);
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .status(StatusConstant.ENABLE)
                .categoryId(categoryId)
                .build();

        return dishMapper.list(dish);
    }

    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        //查询菜品表
        List<Dish> list = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d:list ){
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);
            //根据菜品id查询对应的口味
            List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(dishFlavors);
            dishVOList.add(dishVO);
        }
        return dishVOList;

    }
}
