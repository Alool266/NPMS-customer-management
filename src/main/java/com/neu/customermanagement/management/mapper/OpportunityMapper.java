package com.neu.customermanagement.management.mapper;

import com.neu.customermanagement.management.entity.Opportunity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface OpportunityMapper extends BaseMapper<Opportunity> {

}
