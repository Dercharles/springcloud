package com.dzjt.cbs.uam.security.admin.biz;

import com.dzjt.cbs.uam.security.admin.entity.GateLog;
import com.dzjt.cbs.uam.security.admin.mapper.GateLogMapper;
import com.dzjt.cbs.uam.security.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ${DESCRIPTION}
 *
 * @author XT
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GateLogBiz extends BaseBiz<GateLogMapper,GateLog> {

    @Override
    public void insert(GateLog entity) {
        mapper.insert(entity);
    }

    @Override
    public void insertSelective(GateLog entity) {
        mapper.insertSelective(entity);
    }
}
