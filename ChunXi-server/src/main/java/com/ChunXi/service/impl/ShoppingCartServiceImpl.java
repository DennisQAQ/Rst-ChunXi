package com.ChunXi.service.impl;

import com.ChunXi.context.BaseContext;
import com.ChunXi.dto.ShoppingCartDTO;
import com.ChunXi.entity.Dish;
import com.ChunXi.entity.Setmeal;
import com.ChunXi.entity.ShoppingCart;
import com.ChunXi.mapper.DishMapper;
import com.ChunXi.mapper.SetmealMapper;
import com.ChunXi.mapper.ShoppingCartMapper;
import com.ChunXi.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMappers;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        log.info(shoppingCartDTO.toString());
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        log.info(shoppingCart.toString());

        //只能查询自己的购物车数据
        shoppingCart.setUserId(BaseContext.getCurrentId());

        log.info("保存用户id{}", shoppingCart.getUserId());
        //判断商品是否在购物车中
        List<ShoppingCart> shoppingCartlist = shoppingCartMapper.list(shoppingCart);
        //如果已经存在，就更新数量，数量加1
        if (shoppingCartlist != null && shoppingCartlist.size() == 1) {
            shoppingCart = shoppingCartlist.get(0);
            log.info("sshoppingCart的{}", shoppingCart);
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            log.info("sshoppingCart的数量{}", shoppingCart.getNumber());

            shoppingCartMapper.updateNumById(shoppingCart);
        } else {
            //判断当前添加到购物车的是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                //添加到购物车的是菜品>>>>>调用dishMapper中getById方法查询对应菜品数据
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
                log.info("sshoppingCar2的数据{}", shoppingCart);

            } else {
                //添加到购物车的是套餐>>>>>>调用setmealMapper中getById方法查询对应菜品数据
                Setmeal setmeal = setmealMappers.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
                log.info("sshoppingCart3的数据{}", shoppingCart);
            }

            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);

        }
    }

    /**
     * 查看购物车
     * @return
     */
    @Override
    public List<ShoppingCart> showShoppingCart() {

        return shoppingCartMapper.list(ShoppingCart.builder().userId(BaseContext.getCurrentId()).build());
    }

    /**
     * 清空购物车商品
     */
    public void cleanShoppingCart() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }

    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     */
    @Override
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //设置查询条件，查询当前登录用户的购物车数据
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        log.info("当前用户的id{}",shoppingCart.getUserId());
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);
        //当前商品在购物车中的份数为1，直接删除当前记录
        if (shoppingCarts!=null && shoppingCarts.size()>0){
            shoppingCart=shoppingCarts.get(0);
            log.info("111{}",shoppingCarts.get(0).toString());
            Integer number = shoppingCart.getNumber();
            //当前商品在购物车中的份数不为1，修改份数即可
            if (number == 1) {
                //当前商品在购物车中的份数为1，直接删除当前记录
                shoppingCartMapper.deleteByUserId(shoppingCart.getUserId());

            }else {
                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
                shoppingCartMapper.updateNumById(shoppingCart);
            }
        }
    }
}
