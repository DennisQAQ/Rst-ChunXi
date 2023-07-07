package com.ChunXi.service;

import com.ChunXi.dto.EmployeeDTO;
import com.ChunXi.dto.EmployeeLoginDTO;
import com.ChunXi.dto.EmployeePageQueryDTO;
import com.ChunXi.entity.Employee;
import com.ChunXi.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);


    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);


    void setEmeloyeeSatus(Integer status, Long id);
}
