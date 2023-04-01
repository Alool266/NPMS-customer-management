package com.neu.customermanagement.management.service.impl;

import com.neu.customermanagement.management.entity.Contact;
import com.neu.customermanagement.management.mapper.ContactMapper;
import com.neu.customermanagement.management.service.IContactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact> implements IContactService {

}
