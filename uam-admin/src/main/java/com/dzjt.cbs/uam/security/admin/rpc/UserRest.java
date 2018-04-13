package com.dzjt.cbs.uam.security.admin.rpc;

import com.dzjt.cbs.uam.security.admin.rpc.service.PermissionService;
import com.dzjt.cbs.uam.security.admin.vo.FrontUser;
import com.dzjt.cbs.uam.security.api.vo.authority.PermissionInfo;
import com.dzjt.cbs.uam.security.api.vo.user.UserInfo;
import com.uam.cache.annotation.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author XT
 */
@RestController
@RequestMapping("api")
public class UserRest {
    @Autowired
    private PermissionService permissionService;

    @Cache(key="permission")
    @RequestMapping(value = "/permissions", method = RequestMethod.GET)
    public @ResponseBody
    List<PermissionInfo> getAllPermission(){
        return permissionService.getAllPermission();
    }

    @Cache(key="permission:u{1}")
    @RequestMapping(value = "/user/un/{username}/permissions", method = RequestMethod.GET)
    public @ResponseBody List<PermissionInfo> getPermissionByUsername(@PathVariable("username") String username){
        return permissionService.getPermissionByUsername(username);
    }

    @RequestMapping(value = "/user/validate", method = RequestMethod.POST)
    public @ResponseBody UserInfo validate(String username,String password){
        return permissionService.validate(username,password);
    }

    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    public @ResponseBody FrontUser info(Integer userId){
        return permissionService.getUserInfo(userId);
    }

}
