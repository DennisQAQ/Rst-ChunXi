package com.ChunXi.mapper;

import com.ChunXi.annotation.AutoFill;
import com.ChunXi.dto.DishPageQueryDTO;
import com.ChunXi.entity.Dish;
import com.ChunXi.enumeration.OperationType;
import com.ChunXi.vo.DishVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);



    /**
     * 添加菜品
     * @param
     * @return
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分页查询
     * @param queryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO queryDTO);

    /**
     * 根据主键查询数据
     *
     * @param id
     * @return
     */
    @Select("select * from dish where id=#{id}")
    Dish getById(Long id);

    /**
     *  根据主键删除
     * @param id
     */
    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    /**
     * 更新dish表基本信息
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /**
     *动态条件查询菜品
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

    @Select("select d.* from dish d left outer join setmeal_dish s on d.id=s.dish_id where s.setmeal_id=#{id}")
    List<Dish>getBySetmealId(long id);

    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
