package com.dzjt.cbs.uam.security.admin.biz;

import com.alibaba.fastjson.JSON;
import com.dzjt.cbs.uam.security.admin.constant.OkHttpUtil;
import com.dzjt.cbs.uam.security.admin.entity.UserCompany;
import com.dzjt.cbs.uam.security.admin.mapper.UserCompanyMapper;
import com.dzjt.cbs.uam.security.admin.vo.Company;
import com.dzjt.cbs.uam.security.admin.vo.EasyUiTree;
import com.dzjt.cbs.uam.security.common.biz.BaseBiz;
import com.google.common.collect.Maps;
import com.uam.cache.annotation.Cache;
import com.uam.cache.annotation.CacheClear;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class CompanyBiz extends BaseBiz<UserCompanyMapper, UserCompany> {

    @Value("${client.bos.url}")
    private String bosUrl;

    @Cache(key = "permission:userCompany:u{1}")
    public List<UserCompany> getCompanyIdByUserId(int userId) {
        return mapper.getCompanyIdByUserId(userId);
    }


    @Override
    @CacheClear(keys={"permission:userCompany","permission"})
    public void insertSelective(UserCompany entity) {
        super.insertSelective(entity);
    }

    @CacheClear(keys={"permission:userCompany","permission"})
    public Integer deleteByUserCompanyId(Integer companyId,Integer userId) {
        return mapper.deleteByUserCompanyId(companyId,userId);
    }


    @CacheClear(keys={"permission:userCompany","permission"})
    public void updateUserCompanyId(List<UserCompany> companyList, Integer userId) {
        mapper.deleteByUserId(userId);
        companyList.stream().forEach(e->{

            UserCompany userCompany = new UserCompany();
            userCompany.setCompanyId(e.getCompanyId());
            userCompany.setCompanyName(e.getCompanyName());
            userCompany.setUserId(userId);
            userCompany.setCreateTime(new Date());
            userCompany.setCreateUser(userId);
            super.insertSelective(userCompany);
        });
    }


    @Cache(key="permission:companyId",expire=120)
    public Map<Integer,String> getAllCompanyList(){
        String url = bosUrl + "/api/dzjtcbs/bos/v1/theotherRestService/allOrgNameAndId";
        String parResult = OkHttpUtil.httpGet(url);
        List<Map> list = JSON.parseArray(parResult, Map.class);

        Map<Integer,String> map = Maps.newHashMap();
        list.parallelStream().forEach(m->{
            map.put((Integer)m.get("id"),(String)m.get("odescchs"));
        });

        return map;
    }

    @Cache(key="permission:company",expire=720)
    public List<EasyUiTree> getAllCompanyNode(Long userId){
        String url1 = bosUrl + "/api/dzjtcbs/bos/v1/manRestService/parorg"; // 获取父级公司的url
        String url2 = bosUrl + "/api/dzjtcbs/bos/v1/manRestService/chorg"; // 获取子公司的url
        String url3 = bosUrl + "/api/dzjtcbs/bos/v1/manRestService/orgnamebypguid?id="; //根据父ID 查询子公司
        String parResult = OkHttpUtil.httpGet(url1);

        List<Company> pList = JSON.parseArray(parResult, Company.class);

        List<EasyUiTree> ets = new ArrayList<>(); // 父类节点集合
        List<Integer> companyIds = new ArrayList<>(); // userId 所对应的 公司集合
        if(userId!=-1) {
            List<UserCompany> companyList = this.getCompanyIdByUserId(userId.intValue());
            if(!companyList.isEmpty()){
                companyIds = companyList.stream().map(c->c.getId()).collect(Collectors.toList());
            }
        }
        for(Company c : pList) {
            List<Map<String,Object>> cMaps = new ArrayList<>(); // 对应的孩子节点集合
            EasyUiTree et = new EasyUiTree(); // 父节点
            url3 = url3+c.getvGuid();
            String chResult = OkHttpUtil.httpGet(url2);
            List<Company> cList = JSON.parseArray(chResult, Company.class);
            if(cList!=null) {
                for(Company cc:cList) {

                    Map<String,Object> cmp = new HashMap<>();
                    cmp.put("id", cc.getId());
                    cmp.put("text", cc.getCompanyName());
                    if(companyIds.contains(cc.getId())) {
                        cmp.put("checked", true);  // 设置公司的选中事件
                    }
                    cMaps.add(cmp);
                }
            }
            et.setId(c.getId());
            et.setText(c.getCompanyName());
            et.setChildren(cMaps);
            ets.add(et);
        }

        return ets;

        //List<EasyUiTree>
    }
}
