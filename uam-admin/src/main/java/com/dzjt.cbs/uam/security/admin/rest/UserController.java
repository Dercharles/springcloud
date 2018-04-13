package com.dzjt.cbs.uam.security.admin.rest;

import com.dzjt.cbs.uam.security.admin.biz.UserBiz;
import com.dzjt.cbs.uam.security.admin.constant.AdminCommonConstant;
import com.dzjt.cbs.uam.security.admin.entity.User;
import com.dzjt.cbs.uam.security.admin.rpc.service.PermissionService;
import com.dzjt.cbs.uam.security.admin.rpc.service.UserCompanyService;
import com.dzjt.cbs.uam.security.admin.vo.FrontUser;
import com.dzjt.cbs.uam.security.admin.vo.MenuTree;
import com.dzjt.cbs.uam.security.api.vo.user.UserInfo;
import com.dzjt.cbs.uam.security.auth.client.annotation.IgnoreUserToken;
import com.dzjt.cbs.uam.security.auth.client.config.UserAuthConfig;
import com.dzjt.cbs.uam.security.common.msg.ObjectRestResponse;
import com.dzjt.cbs.uam.security.common.rest.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author XT
 */
@RestController
@RequestMapping("user")
//@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class UserController extends BaseController<UserBiz,User> {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserAuthConfig userAuthConfig;
    @Autowired
    private UserCompanyService userCompanyService;


    @Override
    @RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<User> add(@RequestBody User entity){
        if(entity.getBaseOrgId()==null){
            entity.setBaseOrgId(AdminCommonConstant.DEFAULT_ORG_ID);
        }
        baseBiz.insertSelective(entity);
        return new ObjectRestResponse<User>();
    }

    @RequestMapping(value = "/front/info", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getUserInfo(Integer id) throws Exception {
        FrontUser userInfo;
        if(id == null) {    //当前用户
            userInfo = permissionService.getUserInfo(getCurrentUserName());
        }else {
            userInfo = permissionService.getUserInfo(id);
        }
        if(userInfo==null) {
            return ResponseEntity.status(401).body(false);
        } else {
            return ResponseEntity.ok(userInfo);
        }
    }

    @RequestMapping(value = "/front/menus", method = RequestMethod.GET)
    @ResponseBody
    public List<MenuTree> getMenusByUsername() throws Exception {
        String token = request.getHeader(userAuthConfig.getTokenHeader());
        return permissionService.getMenusByUsername(token);
    }


//    @RolesAllowed({"USER", "ADMIN"})
    @RequestMapping(value = "/group/update", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<?> groupUpdateSelf(@RequestBody List<Integer> groupIdList) throws Exception {
        String token = request.getHeader(userAuthConfig.getTokenHeader());
        permissionService.modifyGroupsMemberByToken(token,groupIdList);
        return new ObjectRestResponse<User>();
    }


//    @RolesAllowed("ADMIN")
    @RequestMapping(value = "/group/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<?> groupUpdate(@PathVariable int id,@RequestBody List<Integer> groupIdList) throws Exception {
        permissionService.modifyGroupsMemberByUser(id,groupIdList);
        return new ObjectRestResponse<User>();
    }

    @IgnoreUserToken
    @RequestMapping(value = "/name/{username}", method = RequestMethod.GET)
    @ResponseBody
    public UserInfo findByUsername(@PathVariable String username) {
        User user = baseBiz.getUserByUsername(username);
        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(user, info);
        info.setId(user.getId().toString());
        Map<Integer,String> companyMap = userCompanyService.getAllCompanyList();
        info.setCompanyName(companyMap.get(user.getBaseOrgId()));
        return info;
    }

}
