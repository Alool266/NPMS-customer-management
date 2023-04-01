package com.neu.customermanagement.management.mapper;

import com.neu.customermanagement.management.dto.common.DeptInfo;
import com.neu.customermanagement.management.dto.common.EmpInfo;
import com.neu.customermanagement.management.dto.common.Relation;
import com.neu.customermanagement.management.dto.customer.CusSearchCondition;
import com.neu.customermanagement.management.dto.customer.CusSearchResult;
import com.neu.customermanagement.management.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CustomerMapper extends BaseMapper<Customer> {

    public List<DeptInfo> getDeptInfoByEmpId(String emp_id);

    public List<EmpInfo> getEmpInfoByEmpIdAndDeptId(String emp_id, String dept_id);

    public List<CusSearchResult> getCusSearchResult(String emp_id, String dept_id);

    public List<EmpInfo> getEmpByDept(String dept_id);

    public List<Relation> getRelationByCusId(String cus_id);

    public String getCusNameByCusId(String cus_id);

    public List<CusSearchResult> getCustomers(CusSearchCondition searchCondition);

    public String findMaxCusId();

    public int addCusRelated(Relation relation);

    public int judgeTaxId(String tax_id);

    public int judgeMultiFather1(String cus_id_son, String cus_id_father);

    public int judgeMultiFather2(String cus_id1, String cus_id2);

    public int deleteRelByIds(String id1, String id2);

    public int deleteConByNameAndId(String cus_id, String con_name);

    public int frozenCustomer(String cus_id, String cus_status);



}
