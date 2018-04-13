package com.dzjt.cbs.uam.security.auth.mapper;

import com.dzjt.cbs.uam.security.auth.entity.ClientService;
import com.dzjt.cbs.uam.security.auth.entity.ClientService;
import tk.mybatis.mapper.common.Mapper;

public interface ClientServiceMapper extends Mapper<ClientService> {
    void deleteByServiceId(int id);
}