package com.munjie.blog.service;

import com.munjie.blog.pojo.AddressDTO;
import com.munjie.blog.pojo.Response;

public interface AddressService {

    AddressDTO listAddress(Integer pageSize, Integer pageNo,String taskId);

    Response deleteAddressByTaskId(String taskId);
}
