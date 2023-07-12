package com.ChunXi.mapper;

import com.ChunXi.annotation.AutoFill;
import com.ChunXi.dto.SetmealPageQueryDTO;
import com.ChunXi.entity.Setmeal;
import com.ChunXi.entity.SetmealDish;
import com.ChunXi.enumeration.OperationType;
import com.ChunXi.vo.DishItemVO;
import com.ChunXi.vo.SetmealVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    @AutoFill(value = OperationType.INSERT)
    void save(Setmeal setmeal);


     /**
           * 根据套餐id查询套餐和菜品的关联关系
           * @param setmealId
          * @return
         */

     @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);

    /**
     *
     * @param id
     */
    @Select("SELECT * FROM setmeal Where id = #{id} ")
    Setmeal getById(Long id);

    /**
     * 修改setmeal表
     * @param setmeal
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);

    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 批量删除套餐
     * @param setmealId
     */
    @Delete("delete from setmeal where id = #{setmealId}")
    void deleteById(Long setmealId);

    /**
     *  动态条件查询套餐
     * @param setmeal
     * @return
     */
   List<Setmeal>list (Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);
}


