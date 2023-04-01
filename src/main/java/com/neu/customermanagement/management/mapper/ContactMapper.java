package com.neu.customermanagement.management.mapper;

import com.neu.customermanagement.management.entity.Contact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ContactMapper extends BaseMapper<Contact> {

    public int updateContacts (Contact contact);

}
