package com.ChunXi.mapper;

import com.ChunXi.annotation.AutoFill;
import com.ChunXi.dto.EmployeePageQueryDTO;
import com.ChunXi.entity.Employee;
import com.ChunXi.enumeration.OperationType;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     *新增员工
     * @param employee
     */
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into employee (name,username,password,phone,sex,id_number,create_time,update_time,create_user,update_user,status)" +
            " values " +
            "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser},#{status})")
    void insert(Employee employee);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 修改员工状态禁用或者启用
     * @param employee
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Employee employee);


    /**
     * 根据id获取员工信息
     * @param id
     * @return
     */
    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);
}
