package com.neu.customermanagement.management.mapper;

import com.neu.customermanagement.management.dto.common.DeptInfo;
import com.neu.customermanagement.management.dto.common.EmpInfo;
import com.neu.customermanagement.management.dto.handover.HandoverInfoSearch;
import com.neu.customermanagement.management.dto.opportunity.Competitor;
import com.neu.customermanagement.management.dto.opportunity.OpportunityBasicInfo;
import com.neu.customermanagement.management.entity.Customer;
import com.neu.customermanagement.management.entity.Handoverlog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neu.customermanagement.management.entity.Opportunity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface HandoverlogMapper extends BaseMapper<Handoverlog> {

    public List<DeptInfo> getAllDept();
    public List<EmpInfo> getAllEmp();
    public List<Customer> searchOutCusInfo (HandoverInfoSearch handoverInfoSearch);
    public List<Competitor> getCompetitorsByOppId(String opp_id);
    public OpportunityBasicInfo getOppById(String opp_id);

}
