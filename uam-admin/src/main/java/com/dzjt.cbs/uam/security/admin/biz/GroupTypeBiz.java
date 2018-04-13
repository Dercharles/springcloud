package com.dzjt.cbs.uam.security.admin.biz;

import org.springframework.stereotype.Service;

import com.dzjt.cbs.uam.security.admin.entity.GroupType;
import com.dzjt.cbs.uam.security.admin.mapper.GroupTypeMapper;
import com.dzjt.cbs.uam.security.common.biz.BaseBiz;
import org.springframework.transaction.annotation.Transactional;

/**
 * ${DESCRIPTION}
 *
 * @author XT
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupTypeBiz extends BaseBiz<GroupTypeMapper,GroupType> {
}
