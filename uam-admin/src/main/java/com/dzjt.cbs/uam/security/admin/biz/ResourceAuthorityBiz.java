package com.dzjt.cbs.uam.security.admin.biz;

import com.dzjt.cbs.uam.security.admin.entity.ResourceAuthority;
import com.dzjt.cbs.uam.security.admin.mapper.ResourceAuthorityMapper;
import com.dzjt.cbs.uam.security.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by XT on 2018/2/19.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceAuthorityBiz extends BaseBiz<ResourceAuthorityMapper,ResourceAuthority> {
}
