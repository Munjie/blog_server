package com.munjie.blog.dao;

import com.munjie.blog.pojo.AddressDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressMapper {


    int addAddress(AddressDO address);

    List<AddressDO> listAddress(String taskId);

}