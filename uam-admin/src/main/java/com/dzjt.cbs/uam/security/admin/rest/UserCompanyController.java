package com.dzjt.cbs.uam.security.admin.rest;

import com.dzjt.cbs.uam.security.admin.biz.CompanyBiz;
import com.dzjt.cbs.uam.security.admin.entity.UserCompany;
import com.dzjt.cbs.uam.security.admin.rpc.service.UserCompanyService;
import com.dzjt.cbs.uam.security.admin.vo.EasyUiTree;
import com.dzjt.cbs.uam.security.common.exception.auth.ClientInvalidException;
import com.dzjt.cbs.uam.security.common.msg.ObjectRestResponse;
import com.dzjt.cbs.uam.security.common.rest.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("company")
public class UserCompanyController extends BaseController<CompanyBiz,UserCompany> {

    @Autowired
    private UserCompanyService userCompanyService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<UserCompany> getCompanysByUserId(Integer id) throws Exception {
        if(id == null){    //当前用户
            return userCompanyService.getCompanysByUsername(getCurrentUserName());
        }else {
            return userCompanyService.getCompanysByUserId(id);
        }
    }

    @RequestMapping(value = "/update/batch",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<UserCompany> updateByUserId(Integer id,@RequestBody List<UserCompany> companyList) throws Exception{
        if(companyList.isEmpty()){
            throw new ClientInvalidException("companyMap is null!");
        }else {
            if(companyList.stream().anyMatch(c->c.getCompanyId()==null||c.getCompanyName()==null||c.getCompanyName().isEmpty())){
                throw new ClientInvalidException("companyMap.companyName is null!");
            }
        }
        if(id == null){    //当前用户
            userCompanyService.updateUserCompanyId(companyList,getCurrentUserName());
        }else {
            userCompanyService.updateUserCompanyId(companyList, id);
        }
        return new ObjectRestResponse<UserCompany>();
    }

    @RequestMapping(value = "/delete/{companyId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ObjectRestResponse<UserCompany> deleteCompanysByUsername(Integer id,@PathVariable Integer companyId) throws Exception {
        if(id == null) {    //当前用户
            userCompanyService.deleteUserCompanyByUsername(companyId, getCurrentUserName());
        }else {
            userCompanyService.deleteUserCompanyByUserId(companyId, id);
        }
        return new ObjectRestResponse<UserCompany>();
    }

    @RequestMapping(value = "/department/nodes", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> showCompanyDepartmentNodes(Long userId){
        //response.addHeader("Access-Control-Allow-Origin", "*");
        //List<EasyUiTree> eusts = companyService.getAllFunctionNode(roleId);
        List<EasyUiTree> ets=userCompanyService.getAllCompanyNode(userId);
        // 有序的map  children必须在 id和 text之后
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", 0);
        map.put("text", "大众交通集团");
        map.put("children", ets);
        //map.put("state", "closed");
        // 注意 easyui tree 返回的数据 均是List形式  因为不止一个节点
        List<Map<String,Object>> nodes =  new ArrayList<>();
        nodes.add(map);
        return nodes;
    }
}
