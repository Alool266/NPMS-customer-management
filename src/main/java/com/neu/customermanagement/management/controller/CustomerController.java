package com.neu.customermanagement.management.controller;


import com.neu.customermanagement.management.dto.common.DeptInfo;
import com.neu.customermanagement.management.dto.common.EmpInfo;
import com.neu.customermanagement.management.dto.customer.*;
import com.neu.customermanagement.management.entity.Customer;
import com.neu.customermanagement.management.entity.Employee;
import com.neu.customermanagement.management.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cusManagement/customer")
public class CustomerController {

    @Autowired
    ICustomerService iCustomerService;
    // Initialize customer management page
    @GetMapping("getMainPage")
    public CusManagePageInfo getMainPage(@RequestParam String empId){
        return iCustomerService.getCusManagePageInfo(empId);
    }

    // Second-level linkage between sales department and account manager
    @GetMapping("getEmpByDept")
    public List<EmpInfo> getEmpByDept(@RequestParam String deptId){
        return iCustomerService.getEmpByDept(deptId);
    }

    // Click on the customer number to display customer details
    @GetMapping("getCusDetail")
    public CusDetail getCusDetail(@RequestParam String cusId){
        return iCustomerService.getCusDetail(cusId);
    }

    // customer inquiry
    @PostMapping("getCustomers")
    public List<CusSearchResult> getCustomers(@RequestBody CusSearchCondition condition){
        return iCustomerService.getCustomers(condition);
    }

    // new customer
    @PostMapping("addCustomers")
    public String addCustomers(@RequestBody AddCustomerInfo addCustomerInfo){
        String msg="";
        try {
            msg = iCustomerService.addCustomers(addCustomerInfo);
        }
        catch (Exception exception){
            msg = "System error, please contact administrator...";
            System.err.println(exception);
        }
        return msg;
    }


    // customer modification
    @PostMapping("updateCustomers")
    public String updateCustomers(@RequestBody UpdateCustomerInfo updateCustomerInfo){
        String msg="";
        try {
            msg = iCustomerService.updateCustomers(updateCustomerInfo);
        }
        catch (Exception exception){
            msg = "System error, please contact administrator...";
            System.err.println(exception);
        }
        return msg;
    }


    // Freeze customers (click the "Freeze" button)
    @PostMapping("frozenCustomer")
    public int frozenCustomer(@RequestBody Customer customer){
        String cus_id = customer.getCusId();
        String cus_status = customer.getCusStatus();
        return iCustomerService.frozenCustomer(cus_id, cus_status);
    }


    // Query customer id by customer name in the associated customer list
    @GetMapping("getCustomerByName")
    public List<Customer> getCustomerByName(@RequestParam String cusName){
        return iCustomerService.getCustomerByName(cusName);
    }


    // export
    @GetMapping("export")
    public String export(){
        return "Export succeeded!";
    }


}

