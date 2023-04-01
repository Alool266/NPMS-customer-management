package com.neu.customermanagement.management.mapper;

import com.neu.customermanagement.management.dto.common.Role;
import com.neu.customermanagement.management.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EmployeeMapper extends BaseMapper<Employee> {
    public List<Role> getRoleById(String emp_id);
    public String getEmpNameById(String emp_id);
    public String getDeptNameById(String dept_id);
}
