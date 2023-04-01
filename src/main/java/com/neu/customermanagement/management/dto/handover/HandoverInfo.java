package com.neu.customermanagement.management.dto.handover;

import com.neu.customermanagement.management.entity.Customer;

import java.io.Serializable;
import java.util.List;

public class HandoverInfo implements Serializable {
    private String outDeptId;
    private String outCusMgrId;
    private String inDeptId;
    private String inCusMgrId;
    private List<Customer> customerList;

    public String getOutDeptId() {
        return outDeptId;
    }

    public void setOutDeptId(String outDeptId) {
        this.outDeptId = outDeptId;
    }

    public String getOutCusMgrId() {
        return outCusMgrId;
    }

    public void setOutCusMgrId(String outCusMgrId) {
        this.outCusMgrId = outCusMgrId;
    }

    public String getInDeptId() {
        return inDeptId;
    }

    public void setInDeptId(String inDeptId) {
        this.inDeptId = inDeptId;
    }

    public String getInCusMgrId() {
        return inCusMgrId;
    }

    public void setInCusMgrId(String inCusMgrId) {
        this.inCusMgrId = inCusMgrId;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    @Override
    public String toString() {
        return "HandoverInfo{" +
                "outDeptId='" + outDeptId + '\'' +
                ", outCusMgrId='" + outCusMgrId + '\'' +
                ", inDeptId='" + inDeptId + '\'' +
                ", inCusMgrId='" + inCusMgrId + '\'' +
                ", customerList=" + customerList +
                '}';
    }
}
