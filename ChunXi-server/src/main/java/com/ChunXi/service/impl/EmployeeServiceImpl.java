package com.ChunXi.service.impl;

import com.ChunXi.constant.MessageConstant;
import com.ChunXi.constant.PasswordConstant;
import com.ChunXi.constant.StatusConstant;
import com.ChunXi.context.BaseContext;
import com.ChunXi.dto.EmployeeDTO;
import com.ChunXi.dto.EmployeeLoginDTO;
import com.ChunXi.dto.EmployeePageQueryDTO;
import com.ChunXi.entity.Employee;
import com.ChunXi.exception.AccountLockedException;
import com.ChunXi.exception.AccountNotFoundException;
import com.ChunXi.exception.PasswordErrorException;
import com.ChunXi.mapper.EmployeeMapper;
import com.ChunXi.result.PageResult;
import com.ChunXi.result.Result;
import com.ChunXi.service.EmployeeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //需要进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee  =new Employee();
        //对象拷贝，将前端提交的值拷贝到实体类
        BeanUtils.copyProperties(employeeDTO,employee);
        //为新增员工设置默认密码 123456
        employee.setPassword(
                //md5加密
                DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes())
        );
        //设置新增员工状态，默认为1，enable状态
        employee.setStatus(StatusConstant.ENABLE);

        employeeMapper.insert(employee);

    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {

        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());

        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        long total = page.getTotal();
        List<Employee> list = page.getResult();

        return new PageResult(total,list);
    }

    /**
     * 修改员工状态禁用或者启用
     * @param status
     * @param id
     */
    @Override
    public void setEmeloyeeSatus(Integer status, Long id) {

        Employee employee = Employee.builder().status(status).id(id).build();

        employeeMapper.update(employee);
    }

    /**
     * 修改员工账号状态
     * @param id
     * @return
     */
    @Override
    public Employee getById(Long id) {
        Employee employee=employeeMapper.getById(id);

        employee.setPassword("******");
        return employee;
    }

    /**
     * 修改员工信息
     * @param employeeDTO
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employeeMapper.update(employee);
    }


}
