package com.dzjt.cbs.uam.security.gate.feign;

import com.dzjt.cbs.uam.security.api.vo.authority.PermissionInfo;
import com.dzjt.cbs.uam.security.api.vo.user.UserInfo;
import com.dzjt.cbs.uam.security.gate.fallback.UserServiceFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * ${DESCRIPTION}
 *
 * @author XT
 * @date 2017-06-21 8:11
 */
@FeignClient(value = "uam-admin",configuration = UserServiceFallback.class)
public interface IUserService {
  @RequestMapping(value="/api/user/un/{username}/permissions",method = RequestMethod.GET)
  List<PermissionInfo> getPermissionByUsername(@PathVariable("username") String username);
  @RequestMapping(value="/api/permissions",method = RequestMethod.GET)
  List<PermissionInfo> getAllPermissionInfo();
  @RequestMapping(value="/user/name/{username}",method = RequestMethod.GET)
  UserInfo findByUsername(@PathVariable("username") String username);
}
