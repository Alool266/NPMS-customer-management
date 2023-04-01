package com.neu.customermanagement.management.service;

import com.neu.customermanagement.management.dto.common.EmpInfo;
import com.neu.customermanagement.management.dto.customer.*;
import com.neu.customermanagement.management.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ICustomerService extends IService<Customer> {

    public CusManagePageInfo getCusManagePageInfo(String emp_id);

    public List<EmpInfo> getEmpByDept(String dept_id);

    public CusDetail getCusDetail(String cus_id);

    public List<CusSearchResult> getCustomers(CusSearchCondition searchCondition);

    public String addCustomers(AddCustomerInfo addCustomerInfo);

    public String updateCustomers(UpdateCustomerInfo updateCustomerInfo);

    public int frozenCustomer(String cus_id, String cus_status);

    public List<Customer> getCustomerByName (String cus_name);


}
