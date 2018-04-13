package com.dzjt.cbs.uam.security.auth.feign;

import com.dzjt.cbs.uam.security.api.vo.user.UserInfo;
import com.dzjt.cbs.uam.security.auth.configuration.FeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * ${DESCRIPTION}
 *
 * @author XT
 * @date 2017-06-21 8:11
 */
@FeignClient(value = "uam-admin",configuration = FeignConfiguration.class)
public interface IUserService {
  @RequestMapping(value = "/api/user/validate", method = RequestMethod.POST)
  public UserInfo validate(@RequestParam("username") String username, @RequestParam("password") String password);
}
