package com.munjie.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.munjie.blog.dao.AddressMapper;
import com.munjie.blog.pojo.AddressDO;
import com.munjie.blog.pojo.AddressDTO;
import com.munjie.blog.pojo.Response;
import com.munjie.blog.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public AddressDTO listAddress(Integer pageSize, Integer pageNo, String taskId) {
        PageHelper.startPage(pageNo, pageSize);
        List<AddressDO> addressDOList = addressMapper.listAddress(taskId);
        PageInfo<AddressDO> addressDOPageInfo = new PageInfo<>(addressDOList);
        return (new AddressDTO(addressDOPageInfo.getList(),addressDOPageInfo.getTotal()));
    }

    @Override
    public Response deleteAddressByTaskId(String taskId) {
        return null;
    }
}
