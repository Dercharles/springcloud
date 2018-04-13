package com.dzjt.cbs.uam.security.admin.rest;

import com.dzjt.cbs.uam.security.admin.biz.GateLogBiz;
import com.dzjt.cbs.uam.security.admin.biz.UserBiz;
import com.dzjt.cbs.uam.security.admin.entity.GateLog;
import com.dzjt.cbs.uam.security.admin.entity.User;
import com.dzjt.cbs.uam.security.common.msg.TableResultResponse;
import com.dzjt.cbs.uam.security.common.rest.BaseController;
import com.dzjt.cbs.uam.security.common.util.Query;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Optional;

/**
 * ${DESCRIPTION}
 *
 * @author XT
 */
@Controller
@RequestMapping("gateLog")
public class GateLogController extends BaseController<GateLogBiz,GateLog> {

    @Autowired
    private UserBiz userBiz;

    @Override
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "操作记录分页查询", httpMethod = "GET", response = GateLog.class,
            notes = "login=true：登录日志；login=false：操作日志； key前缀含义：LIKE_：模糊查询；NEQ_：不等于；GT_：大于；LT_：小与；GTE_：大于等于；LT_：小与等于；NULL_：空；NOTNULL_：非空；IN_：集合查询")
    public TableResultResponse<GateLog> list(@RequestParam Map<String, Object> params){
        String login = (String) params.get("login");
        if(login!=null){
            if(login.equals("true")){
                params.put("uri","/auth/jwt/token");

                String phone=(String)params.get("mobilePhone");
                if(phone!=null&&!phone.isEmpty()){
                    User user = new User();
                    user.setMobilePhone(phone);
                    Optional optional = userBiz.selectList(user).stream().map(u->u.getId().toString()).reduce((a, b)->a+","+b);
                    if(optional!=null && optional.isPresent()){
                        params.put("IN_crtUser",optional.get());
                    }
                    params.remove("mobilePhone");
                }
            }else {
                params.put("NEQ_uri","/auth/jwt/token");
            }
            params.remove("login");
        }

        //查询列表数据
        Query query = new Query(params, "id desc");
        TableResultResponse<GateLog> page = baseBiz.selectByQuery(query);
        if("true".equals(login) && page.getData()!=null && page.getData().getRows()!=null) {
            page.getData().getRows().stream().forEach(l -> l.setMobilePhone(userBiz.getUserPhoneByUserId(new Integer(l.getCrtUser()))));
        }
        return page;
    }
}
