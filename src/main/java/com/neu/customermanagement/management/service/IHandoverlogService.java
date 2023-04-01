package com.neu.customermanagement.management.service;

import com.neu.customermanagement.management.dto.handover.HandoverInfo;
import com.neu.customermanagement.management.dto.handover.HandoverInfoSearch;
import com.neu.customermanagement.management.dto.handover.HandoverPageInfo;
import com.neu.customermanagement.management.dto.opportunity.OppDetail;
import com.neu.customermanagement.management.entity.Customer;
import com.neu.customermanagement.management.entity.Handoverlog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.customermanagement.management.entity.Opportunity;

import java.util.List;

public interface IHandoverlogService extends IService<Handoverlog> {
    public HandoverPageInfo getHandoverPage();
    public List<Customer> searchOutCusInfo(HandoverInfoSearch handoverInfoSearch);
    public OppDetail showOppDetail(String opp_id);
    public List<Opportunity> getOppByCusId(String cus_id);
    public List<Customer> handover(HandoverInfo handoverInfo);
}
