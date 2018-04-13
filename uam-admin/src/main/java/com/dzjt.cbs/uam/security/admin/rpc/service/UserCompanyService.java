package com.dzjt.cbs.uam.security.admin.rpc.service;

import com.dzjt.cbs.uam.security.admin.biz.CompanyBiz;
import com.dzjt.cbs.uam.security.admin.biz.UserBiz;
import com.dzjt.cbs.uam.security.admin.entity.User;
import com.dzjt.cbs.uam.security.admin.entity.UserCompany;
import com.dzjt.cbs.uam.security.admin.vo.EasyUiTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserCompanyService {

    @Autowired
    private UserBiz userBiz;
    @Autowired
    private CompanyBiz companyBiz;

    public List<UserCompany> getCompanysByUsername(String username) throws Exception {
        User user = userBiz.getUserByUsername(username);
        return companyBiz.getCompanyIdByUserId(user.getId());
    }
    public List<UserCompany> getCompanysByUserId(int id) throws Exception {
        return companyBiz.getCompanyIdByUserId(id);
    }


    public Integer deleteUserCompanyByUsername(int companyId,String username) throws Exception {
        User user = userBiz.getUserByUsername(username);
        return companyBiz.deleteByUserCompanyId(companyId,user.getId());
    }
    public Integer deleteUserCompanyByUserId(int companyId,int id) throws Exception {
        return companyBiz.deleteByUserCompanyId(companyId,id);
    }

    public void updateUserCompanyId(List<UserCompany> companyList,String username) throws Exception {
        if (username == null) {
            return ;
        }
        User user = userBiz.getUserByUsername(username);
        companyBiz.updateUserCompanyId(companyList,user.getId());
    }

    public void updateUserCompanyId(List<UserCompany> companyList,int userId) throws Exception {
        companyBiz.updateUserCompanyId(companyList,userId);
    }

    public List<EasyUiTree> getAllCompanyNode(Long userId){
        return companyBiz.getAllCompanyNode(userId);
    }
    public Map<Integer,String> getAllCompanyList(){
        return companyBiz.getAllCompanyList();
    }
}
