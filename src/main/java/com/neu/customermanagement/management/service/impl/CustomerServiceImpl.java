package com.neu.customermanagement.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.neu.customermanagement.management.dto.common.DeptInfo;
import com.neu.customermanagement.management.dto.common.EmpInfo;
import com.neu.customermanagement.management.dto.common.Relation;
import com.neu.customermanagement.management.dto.customer.*;
import com.neu.customermanagement.management.entity.Contact;
import com.neu.customermanagement.management.entity.Customer;
import com.neu.customermanagement.management.entity.Employee;
import com.neu.customermanagement.management.mapper.ContactMapper;
import com.neu.customermanagement.management.mapper.CustomerMapper;
import com.neu.customermanagement.management.mapper.EmployeeMapper;
import com.neu.customermanagement.management.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    ContactMapper contactMapper;


    @Override
    public CusManagePageInfo getCusManagePageInfo(String emp_id) {
        Employee employee = employeeMapper.selectById(emp_id);
        String emp_position = employee.getEmpPositionId();

        List<DeptInfo> deptInfoList = new ArrayList<>();
        List<EmpInfo> empInfoList = new ArrayList<>();
        List<CusSearchResult> cusSearchResultList = new ArrayList<>();

        if (emp_position.equals("10000030")){
            deptInfoList = customerMapper.getDeptInfoByEmpId(emp_id);
            empInfoList = customerMapper.getEmpInfoByEmpIdAndDeptId(null,deptInfoList.get(0).getDeptId());
            cusSearchResultList = customerMapper.getCusSearchResult(null, deptInfoList.get(0).getDeptId());
            for (CusSearchResult cr : cusSearchResultList) {
                cr.setCusSalesDeptName(deptInfoList.get(0).getDeptName());
                cr.setCusCustomerManagerName(employeeMapper.getEmpNameById(cr.getCusCustomerManagerId()));
            }
        }
        if (emp_position.equals("30000010")){
            deptInfoList = customerMapper.getDeptInfoByEmpId(emp_id);
            empInfoList = customerMapper.getEmpInfoByEmpIdAndDeptId(null,deptInfoList.get(0).getDeptId());
            cusSearchResultList = customerMapper.getCusSearchResult(emp_id, null);
            for (CusSearchResult cr : cusSearchResultList) {
                cr.setCusSalesDeptName(deptInfoList.get(0).getDeptName());
                cr.setCusCustomerManagerName(employeeMapper.getEmpNameById(cr.getCusCustomerManagerId()));
            }
        }
        if (emp_position.equals("30000030")){
            deptInfoList = customerMapper.getDeptInfoByEmpId(emp_id);
            empInfoList = customerMapper.getEmpInfoByEmpIdAndDeptId(emp_id, null);
            cusSearchResultList = customerMapper.getCusSearchResult(emp_id, null);
            for (CusSearchResult cr : cusSearchResultList) {
                cr.setCusSalesDeptName(deptInfoList.get(0).getDeptName());
                cr.setCusCustomerManagerName(empInfoList.get(0).getEmpName());
            }
        }

        if (emp_position.equals("20000010") || emp_position.equals("20000020") || emp_position.equals("50000000")){
            deptInfoList = customerMapper.getDeptInfoByEmpId(null);
            empInfoList = customerMapper.getEmpInfoByEmpIdAndDeptId(null,null);
            cusSearchResultList = new ArrayList<>();
        }

        CusManagePageInfo cusManagePageInfo = new CusManagePageInfo();
        cusManagePageInfo.setDeptInfoList(deptInfoList);
        cusManagePageInfo.setEmpInfoList(empInfoList);
        cusManagePageInfo.setCusSearchResultList(cusSearchResultList);

        return cusManagePageInfo;
    }


    @Cacheable(cacheNames="cus_getEmpByDept")
    @Override
    public List<EmpInfo> getEmpByDept(String dept_id) {

        return customerMapper.getEmpByDept(dept_id);
    }


    @Override
    public CusDetail getCusDetail(String cus_id) {

        Customer customer = null;
        List<Contact> contactList = new ArrayList<>();
        List<Relation> relationList = new ArrayList<>();
        String cus_name = customerMapper.getCusNameByCusId(cus_id);

        customer = customerMapper.selectById(cus_id);

        QueryWrapper<Contact> qw = Wrappers.query();
        qw.eq("con_customer_id", cus_id);
        contactList = contactMapper.selectList(qw);

        relationList = customerMapper.getRelationByCusId(cus_id);
        for (Relation relation : relationList) {
            relation.setCusrelCusName(cus_name);
            relation.setCusrelCusRelatedCusName(customerMapper.getCusNameByCusId(relation.getCusrelCusRelatedCusId()));
        }

        CusDetail cusDetail = new CusDetail();
        cusDetail.setCustomer(customer);
        cusDetail.setContactList(contactList);
        cusDetail.setRelationList(relationList);

        return cusDetail;
    }


    @Override
    public List<CusSearchResult> getCustomers(CusSearchCondition searchCondition) {
        List<CusSearchResult> cusSearchResults = customerMapper.getCustomers(searchCondition);
        for (CusSearchResult csr : cusSearchResults) {
            csr.setCusSalesDeptName(employeeMapper.getDeptNameById(csr.getCusSalesDeptId()));
            csr.setCusCustomerManagerName(employeeMapper.getEmpNameById(csr.getCusCustomerManagerId()));
        }
        return cusSearchResults;
    }


    @Override
    @Transactional(rollbackFor=Exception.class)
    public String addCustomers(AddCustomerInfo addCustomerInfo) {
        Customer customer = addCustomerInfo.getCustomer();
        List<Contact> contactList = addCustomerInfo.getContactList();
        List<Relation> relationList = addCustomerInfo.getRelationList();
        String msg = "Customer added successfully!";
        if (customer.getCusTaxpayerId()!=null && customerMapper.judgeTaxId(customer.getCusTaxpayerId()) > 0){
            return "Duplicate TIN!";
        }

        String cus_id_max = customerMapper.findMaxCusId();
        String cus_id = Integer.parseInt(cus_id_max)+1+"";

        customer.setCusId(cus_id);
        for (Contact contact : contactList) {
            contact.setConCustomerId(cus_id);
        }
        for (Relation relation : relationList) {
            relation.setCusrelCusId(cus_id);
        }

        for (int i = 0; i < relationList.size(); i++) {
            for (int j = i+1; j < relationList.size(); j++) {
                int k = customerMapper.judgeMultiFather2(relationList.get(i).getCusrelCusRelatedCusId(),relationList.get(j).getCusrelCusRelatedCusId());
                if (k > 0) {
                    return "Linking multi-level parent nodes is not allowed！";
                }
            }
        }

        customer.setCusStatus("10");
        try {
            customerMapper.insert(customer);
            for (Contact c : contactList) {
                contactMapper.insert(c);
            }
            for (Relation r : relationList) {
                customerMapper.addCusRelated(r);
            }
        }catch (Exception e){
            System.out.println("insert fail!");
            throw e;
        }
        System.out.println("insert success!");
        return msg;
    }


    @Override
    @Transactional(rollbackFor=Exception.class)
    public String updateCustomers(UpdateCustomerInfo updateCustomerInfo) {
        Customer customer = updateCustomerInfo.getCustomer();
        List<Contact> addContactsList = updateCustomerInfo.getAddContactsList();
        List<Contact> updateContactsList = updateCustomerInfo.getUpdateContactsList();
        List<String> deleteContactsName = updateCustomerInfo.getDeleteContactsName();
        List<Relation> addRelationList = updateCustomerInfo.getAddRelationList();
        List<String> deleteRelationCusId = updateCustomerInfo.getDeleteRelationCusId();
        String msg = "Customer modified successfully!";

        Employee emp = employeeMapper.selectById(customer.getCusCustomerManagerId());
        customer.setCusSalesDeptId(emp.getEmpDeptId());

        if (!customer.getCusStatus().equals("10")){
            return "Frozen status cannot modify or delete customer contact information!";
        }

        for (String s : deleteRelationCusId) {
            customerMapper.deleteRelByIds(customer.getCusId(), s);
        }

        for (Relation relation : addRelationList) {
            relation.setCusrelCusId(customer.getCusId());
        }

        for (int i = 0; i < addRelationList.size(); i++) {
            for (int j = i+1; j < addRelationList.size(); j++) {
                int k = customerMapper.judgeMultiFather2(addRelationList.get(i).getCusrelCusRelatedCusId(),addRelationList.get(j).getCusrelCusRelatedCusId());
                if (k > 0) {
                    return "Linking multi-level parent nodes is not allowed!";
                }
            }
        }
        for (Relation rel : addRelationList) {
            int k = customerMapper.judgeMultiFather1(rel.getCusrelCusId(), rel.getCusrelCusRelatedCusId());
            System.out.println("k= "+k);
            if (k > 0) {
                return "Linking multi-level parent nodes is not allowed!";
            }
        }

        try{
            customerMapper.updateById(customer);
            for (Contact contact : addContactsList) {
                contact.setConCustomerId(customer.getCusId());
                contactMapper.insert(contact);
            }
            for (String s : deleteContactsName) {
                customerMapper.deleteConByNameAndId(customer.getCusId(), s);
            }
            for (Contact contact : updateContactsList) {
                contact.setConCustomerId(customer.getCusId());
                contactMapper.updateContacts(contact);
            }
            for (Relation relation : addRelationList) {
                customerMapper.addCusRelated(relation);
            }

        }catch (Exception e){
            System.out.println("update fail!");
            throw e;
        }

        System.out.println("update success!");
        return msg;
    }


    @Override
    public int frozenCustomer(String cus_id, String cus_status) {
        return customerMapper.frozenCustomer(cus_id, cus_status);
    }

    @Override
    public List<Customer> getCustomerByName(String cus_name) {
        QueryWrapper<Customer> qw = Wrappers.query();
        qw.eq("cus_name",cus_name);
        return customerMapper.selectList(qw);
    }


}
