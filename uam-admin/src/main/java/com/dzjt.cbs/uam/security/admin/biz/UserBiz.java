package com.dzjt.cbs.uam.security.admin.biz;

import com.dzjt.cbs.uam.security.admin.entity.User;
import com.dzjt.cbs.uam.security.admin.mapper.UserMapper;
import com.dzjt.cbs.uam.security.common.biz.BaseBiz;
import com.dzjt.cbs.uam.security.common.constant.UserConstant;
import com.uam.cache.annotation.Cache;
import com.uam.cache.annotation.CacheClear;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ${DESCRIPTION}
 *
 * @author XT
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserBiz extends BaseBiz<UserMapper,User> {


    @Override
    public void insertSelective(User entity) {
        String password = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(entity.getPassword());
        entity.setPassword(password);
        super.insertSelective(entity);
    }

    @Override
    @CacheClear(pre="user{1.username}")
    public void updateSelectiveById(User entity) {
        if(entity.getPassword()!=null&&!entity.getPassword().isEmpty()) {
            String password = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(entity.getPassword());
            entity.setPassword(password);
        }
        super.updateSelectiveById(entity);
    }

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    @Cache(key="user{1}")
    public User getUserByUsername(String username){
        User user = new User();
        user.setUsername(username);
        return mapper.selectOne(user);
    }


    /**
     * 根据用户名获取用户信息
     * @param userId
     * @return
     */
    @Cache(key="userPhone{1}")
    public String getUserPhoneByUserId(int userId){
        User user = super.selectById(userId);
        if(user==null){
            return null;
        }
        return user.getMobilePhone();
    }

}
