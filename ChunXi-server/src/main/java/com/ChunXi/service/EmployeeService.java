package com.ChunXi.service;

import com.ChunXi.dto.EmployeeDTO;
import com.ChunXi.dto.EmployeeLoginDTO;
import com.ChunXi.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void save(EmployeeDTO employeeDTO);
}
