package com.neu.customermanagement.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.neu.customermanagement.management.dto.common.DeptInfo;
import com.neu.customermanagement.management.dto.common.EmpInfo;
import com.neu.customermanagement.management.dto.handover.HandoverInfo;
import com.neu.customermanagement.management.dto.handover.HandoverInfoSearch;
import com.neu.customermanagement.management.dto.handover.HandoverPageInfo;
import com.neu.customermanagement.management.dto.opportunity.Competitor;
import com.neu.customermanagement.management.dto.opportunity.OppDetail;
import com.neu.customermanagement.management.dto.opportunity.OpportunityBasicInfo;
import com.neu.customermanagement.management.entity.*;
import com.neu.customermanagement.management.mapper.*;
import com.neu.customermanagement.management.service.IHandoverlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class HandoverlogServiceImpl extends ServiceImpl<HandoverlogMapper, Handoverlog> implements IHandoverlogService {

    @Autowired
    HandoverlogMapper handoverlogMapper;
    @Autowired
    OpportunityMapper opportunityMapper;
    @Autowired
    SubOpportunityMapper subOpportunityMapper;
    @Autowired
    PayerMapper payerMapper;
    @Autowired
    CustomerMapper customerMapper;

    @Override
    public HandoverPageInfo getHandoverPage() {
        List<DeptInfo> deptInfoList = handoverlogMapper.getAllDept();
        List<EmpInfo> empInfoList = handoverlogMapper.getAllEmp();
        HandoverPageInfo handoverPageInfo = new HandoverPageInfo();
        handoverPageInfo.setDeptInfoList(deptInfoList);
        handoverPageInfo.setEmpInfoList(empInfoList);
        return handoverPageInfo;
    }

    @Override
    public List<Customer> searchOutCusInfo(HandoverInfoSearch handoverInfoSearch) {
        return handoverlogMapper.searchOutCusInfo(handoverInfoSearch);
    }

    @Override
    public OppDetail showOppDetail(String opp_id) {

        OpportunityBasicInfo opportunityBasicInfo = handoverlogMapper.getOppById(opp_id);
        QueryWrapper<SubOpportunity> qw1 = Wrappers.query();
        qw1.eq("sub_opp_opp_id", opp_id);
        List<SubOpportunity> subOpportunityList =subOpportunityMapper.selectList(qw1);
        QueryWrapper<Payer> qw2 = Wrappers.query();
        qw2.eq("p_opp_id", opp_id);
        List<Payer> payerList = payerMapper.selectList(qw2);
        List<Competitor> competitorList = handoverlogMapper.getCompetitorsByOppId(opp_id);

        OppDetail oppDetail = new OppDetail();
        oppDetail.setOpportunityBasicInfo(opportunityBasicInfo);
        oppDetail.setSubOpportunityList(subOpportunityList);
        oppDetail.setCompetitorList(competitorList);
        oppDetail.setPayerList(payerList);

        return oppDetail;
    }

    @Override
    public List<Opportunity> getOppByCusId(String cus_id) {
        QueryWrapper<Opportunity> qw = Wrappers.query();
        qw.eq("opp_cus_id", cus_id);
        List<Opportunity> opportunityList = opportunityMapper.selectList(qw);
        return opportunityList;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public List<Customer> handover(HandoverInfo handoverInfo) {
        List<Customer> customerList = handoverInfo.getCustomerList();
        for (Customer cus : customerList) {
            cus.setCusSalesDeptId(handoverInfo.getInDeptId());
            cus.setCusCustomerManagerId(handoverInfo.getInCusMgrId());
            customerMapper.updateById(cus);
            QueryWrapper<Opportunity> qw = Wrappers.query();
            qw.eq("opp_cus_id", cus.getCusId());
            List<Opportunity> opportunityList = opportunityMapper.selectList(qw);
            for (Opportunity opp : opportunityList) {
                opp.setOppSalesDept(handoverInfo.getInDeptId());
                opp.setOppCustomerManagerId(handoverInfo.getInCusMgrId());
                opportunityMapper.updateById(opp);
                Handoverlog handoverlog = new Handoverlog();
                handoverlog.sethCusId(cus.getCusId());
                handoverlog.sethOppId(opp.getOppId());
                handoverlog.sethOutCusManagerId(handoverInfo.getOutCusMgrId());
                handoverlog.sethInCusManagerId(handoverInfo.getInCusMgrId());
                handoverlogMapper.insert(handoverlog);
            }
        }

        return customerList;
    }
}
