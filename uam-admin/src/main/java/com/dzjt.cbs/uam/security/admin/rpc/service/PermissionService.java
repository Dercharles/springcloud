package com.dzjt.cbs.uam.security.admin.rpc.service;

import com.dzjt.cbs.uam.security.admin.biz.*;
import com.dzjt.cbs.uam.security.admin.constant.AdminCommonConstant;
import com.dzjt.cbs.uam.security.admin.entity.Element;
import com.dzjt.cbs.uam.security.admin.entity.Menu;
import com.dzjt.cbs.uam.security.admin.entity.User;
import com.dzjt.cbs.uam.security.admin.vo.FrontUser;
import com.dzjt.cbs.uam.security.admin.vo.MenuTree;
import com.dzjt.cbs.uam.security.api.vo.authority.PermissionInfo;
import com.dzjt.cbs.uam.security.api.vo.user.UserInfo;
import com.dzjt.cbs.uam.security.auth.client.jwt.UserAuthUtil;
import com.dzjt.cbs.uam.security.common.constant.CommonConstants;
import com.dzjt.cbs.uam.security.common.exception.BaseException;
import com.dzjt.cbs.uam.security.common.util.TreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by XT on 2018/2/12.
 */
@Service
public class PermissionService {
    @Autowired
    private UserBiz userBiz;
    @Autowired
    private MenuBiz menuBiz;
    @Autowired
    private ElementBiz elementBiz;
    @Autowired
    private UserAuthUtil userAuthUtil;
    @Autowired
    private GroupBiz groupBiz;
    @Autowired
    private CompanyBiz companyBiz;
    @Autowired
    private UserCompanyService userCompanyService;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public UserInfo getUserByUsername(String username) {
        UserInfo info = new UserInfo();
        User user = userBiz.getUserByUsername(username);
        BeanUtils.copyProperties(user, info);
        info.setId(user.getId().toString());
        return info;
    }

    public UserInfo validate(String username,String password){
        UserInfo info = new UserInfo();
        User user = userBiz.getUserByUsername(username);
        if (encoder.matches(password, user.getPassword())) {
            BeanUtils.copyProperties(user, info);
            info.setId(user.getId().toString());
        }
        return info;
    }

    public List<PermissionInfo> getAllPermission() {
        List<Menu> menus = menuBiz.selectListAll();
        List<PermissionInfo> result = new ArrayList<PermissionInfo>();
        menu2permission(menus, result);
        List<Element> elements = elementBiz.getAllElementPermissions();
        element2permission(result, elements);

        return result;
    }

    private void menu2permission(List<Menu> menus, List<PermissionInfo> result) {
        PermissionInfo info;
        for (Menu menu : menus) {
            if (StringUtils.isBlank(menu.getHref())) {
                menu.setHref("/" + menu.getCode());
            }
            info = new PermissionInfo();
            info.setCode(menu.getCode());
            info.setType(AdminCommonConstant.RESOURCE_TYPE_MENU);
            info.setName(AdminCommonConstant.RESOURCE_ACTION_VISIT);
            String uri = menu.getHref();
            if (!uri.startsWith("/")) {
                uri = "/" + uri;
            }
            info.setUri(uri);
            info.setMethod(AdminCommonConstant.RESOURCE_REQUEST_METHOD_GET);
            result.add(info);
            info.setMenu(menu.getTitle());
            info.setMenuId(menu.getId());
        }
    }

    public List<PermissionInfo> getPermissionByUsername(String username) {
        User user = userBiz.getUserByUsername(username);
        return this.getPermissionByUsername(user);
    }
    public List<PermissionInfo> getPermissionByUsername(int userId) {
        User user = userBiz.selectById(userId);
        return this.getPermissionByUsername(user);
    }
    private List<PermissionInfo> getPermissionByUsername(final User user) {
        List<Menu> menus = menuBiz.getUserAuthorityMenuByUserId(user.getId());
        List<PermissionInfo> result = new ArrayList<PermissionInfo>();
        menu2permission(menus, result);
        List<Element> elements = elementBiz.getAuthorityElementByUserId(user.getId() + "");
        element2permission(result, elements);
        Map<Integer,String> companyMap = userCompanyService.getAllCompanyList();
        result.parallelStream().forEach(p->{
            p.setCompanyId(user.getBaseOrgId());
            p.setCompanyName(companyMap.get(p.getCompanyId()));
        });
        return result;
    }

    private void element2permission(List<PermissionInfo> result, List<Element> elements) {
        PermissionInfo info;
        for (Element element : elements) {
            info = new PermissionInfo();
            info.setCode(element.getCode());
            info.setType(element.getType());
            info.setUri(element.getUri());
            info.setMethod(element.getMethod());
            info.setName(element.getName());
            info.setOptId(element.getId());
            info.setMenuId(element.getMenuId());
            result.add(info);
        }
    }


    private List<MenuTree> getMenuTree(List<Menu> menus, int root) {
        List<MenuTree> trees = new ArrayList<MenuTree>();
        MenuTree node ;
        for (Menu menu : menus) {
            node = new MenuTree();
            BeanUtils.copyProperties(menu, node);
            trees.add(node);
        }
        return TreeUtil.bulid(trees, root);
    }

    public FrontUser getUserInfo(String username) throws Exception {
        UserInfo user = this.getUserByUsername(username);
        return this.getUserInfo(user);
    }

    public FrontUser getUserInfo(int userId) {
        UserInfo info = new UserInfo();
        User user = userBiz.selectById(userId);
        if(user==null){
            throw new BaseException("id not exist!");
        }
        BeanUtils.copyProperties(user, info);
        info.setId(user.getId().toString());
        return this.getUserInfo(info);
    }

    private FrontUser getUserInfo(UserInfo user) {
        FrontUser frontUser = new FrontUser();
        BeanUtils.copyProperties(user, frontUser);
        List<PermissionInfo> permissionInfos = this.getPermissionByUsername(new Integer(user.getId()));
        Stream<PermissionInfo> menus = permissionInfos.parallelStream().filter((permission) -> {
            return permission.getType().equals(CommonConstants.RESOURCE_TYPE_MENU);
        });
        frontUser.setMenus(menus.collect(Collectors.toList()));
        Stream<PermissionInfo> elements = permissionInfos.parallelStream().filter((permission) -> {
            return !permission.getType().equals(CommonConstants.RESOURCE_TYPE_MENU);
        });
        frontUser.setElements(elements.collect(Collectors.toList()));

        frontUser.setGroups(groupBiz.getGroupsByUserId(new Integer(user.getId())));
        frontUser.setCompanyList(companyBiz.getCompanyIdByUserId(new Integer(user.getId())));
        return frontUser;
    }

    public List<MenuTree> getMenusByUsername(String token) throws Exception {
        String username = userAuthUtil.getInfoFromToken(token).getUniqueName();
        if (username == null) {
            return null;
        }
        User user = userBiz.getUserByUsername(username);
        List<Menu> menus = menuBiz.getUserAuthorityMenuByUserId(user.getId());
        return getMenuTree(menus,AdminCommonConstant.ROOT);
    }

    public boolean modifyGroupsMemberByToken(String token,List<Integer> groupIdList) throws Exception {
        String username = userAuthUtil.getInfoFromToken(token).getUniqueName();
        if (username == null) {
            return false;
        }
        User user = userBiz.getUserByUsername(username);
        return groupBiz.modifyGroupsMemberByUser(user.getId(),groupIdList);
    }


    public boolean modifyGroupsMemberByUser(int userId,List<Integer> groupIdList) throws Exception {
        return groupBiz.modifyGroupsMemberByUser(userId,groupIdList);
    }


}
