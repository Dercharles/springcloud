package com.dzjt.cbs.uam.security.admin.rest;

import com.dzjt.cbs.uam.security.admin.biz.ElementBiz;
import com.dzjt.cbs.uam.security.admin.biz.GroupBiz;
import com.dzjt.cbs.uam.security.admin.biz.MenuBiz;
import com.dzjt.cbs.uam.security.admin.biz.UserBiz;
import com.dzjt.cbs.uam.security.admin.constant.AdminCommonConstant;
import com.dzjt.cbs.uam.security.admin.entity.Element;
import com.dzjt.cbs.uam.security.admin.entity.Menu;
import com.dzjt.cbs.uam.security.admin.vo.AuthorityMenuTree;
import com.dzjt.cbs.uam.security.admin.vo.MenuTree;
import com.dzjt.cbs.uam.security.common.rest.BaseController;
import com.dzjt.cbs.uam.security.common.util.TreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author XT
 */
@Controller
@RequestMapping("menu")
public class MenuController extends BaseController<MenuBiz, Menu> {

    @Autowired
    private UserBiz userBiz;
    @Autowired
    private ElementBiz elementBiz;
    @Autowired
    private GroupBiz groupBiz;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Menu> list(String title) {
        Example example = new Example(Menu.class);
        if (StringUtils.isNotBlank(title)) {
            example.createCriteria().andLike("title", "%" + title + "%");
        }
        return baseBiz.selectByExample(example);
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public List<MenuTree> getTree(String title){
        Example example = new Example(Menu.class);
        if (StringUtils.isNotBlank(title)) {
            example.createCriteria().andLike("title", "%" + title + "%");
        }
        return getMenuTree(baseBiz.selectByExample(example), AdminCommonConstant.ROOT);
    }


    @RequestMapping(value = "/element/tree", method = RequestMethod.GET)
    @ResponseBody
    public List<MenuTree> getElementTree(@RequestParam(defaultValue = "false") boolean checked,Integer groupId) {
        List<Integer> checkedMenuIdList = null, checkedElementIdList = null;
        if(checked && groupId!=null){
            checkedMenuIdList = groupBiz.getAuthorityElement(groupId,AdminCommonConstant.RESOURCE_TYPE_MENU);
            checkedElementIdList =  groupBiz.getAuthorityElement(groupId,AdminCommonConstant.RESOURCE_TYPE_BTN);
        }
        return getMenuTree(baseBiz.selectListAll(), AdminCommonConstant.ROOT,
                elementBiz.selectListAll(),checkedMenuIdList,checkedElementIdList);
    }

    @RequestMapping(value = "/system", method = RequestMethod.GET)
    @ResponseBody
    public List<Menu> getSystem() {
        Menu menu = new Menu();
        menu.setParentId(AdminCommonConstant.ROOT);
        return baseBiz.selectList(menu);
    }

    @RequestMapping(value = "/menuTree", method = RequestMethod.GET)
    @ResponseBody
    public List<MenuTree> listMenu(Integer parentId) {
        try {
            if (parentId == null) {
                parentId = this.getSystem().get(0).getId();
            }
        } catch (Exception e) {
            return new ArrayList<MenuTree>();
        }
        List<MenuTree> trees = new ArrayList<MenuTree>();
        MenuTree node = null;
        Example example = new Example(Menu.class);
        Menu parent = baseBiz.selectById(parentId);
        example.createCriteria().andLike("path", parent.getPath() + "%").andNotEqualTo("id",parent.getId());
        return getMenuTree(baseBiz.selectByExample(example), parent.getId());
    }

    @RequestMapping(value = "/authorityTree", method = RequestMethod.GET)
    @ResponseBody
    public List<AuthorityMenuTree> listAuthorityMenu() {
        List<AuthorityMenuTree> trees = new ArrayList<AuthorityMenuTree>();
        AuthorityMenuTree node = null;
        for (Menu menu : baseBiz.selectListAll()) {
            node = new AuthorityMenuTree();
            node.setText(menu.getTitle());
            BeanUtils.copyProperties(menu, node);
            trees.add(node);
        }
        return TreeUtil.bulid(trees, AdminCommonConstant.ROOT);
    }

    @RequestMapping(value = "/user/authorityTree", method = RequestMethod.GET)
    @ResponseBody
    public List<MenuTree> listUserAuthorityMenu(Integer parentId){
        int userId = userBiz.getUserByUsername(getCurrentUserName()).getId();
        try {
            if (parentId == null) {
                parentId = this.getSystem().get(0).getId();
            }
        } catch (Exception e) {
            return new ArrayList<MenuTree>();
        }
        return getMenuTree(baseBiz.getUserAuthorityMenuByUserId(userId),parentId);
    }

    @RequestMapping(value = "/user/system", method = RequestMethod.GET)
    @ResponseBody
    public List<Menu> listUserAuthoritySystem() {
        int userId = userBiz.getUserByUsername(getCurrentUserName()).getId();
        return baseBiz.getUserAuthoritySystemByUserId(userId);
    }

    private List<MenuTree> getMenuTree(List<Menu> menus, int root) {
        return this.getMenuTree(menus,root,null,null,null);
    }
    private List<MenuTree> getMenuTree(List<Menu> menus, int root, List<Element> elementList,
                                       List<Integer> checkedMenuIdList,List<Integer> checkedElementIdList) {
        List<MenuTree> trees = new ArrayList<MenuTree>();
        MenuTree node = null;
        for (Menu menu : menus) {
            node = new MenuTree();
            BeanUtils.copyProperties(menu, node);
            node.setLabel(menu.getTitle());
            trees.add(node);
        }

        MenuBiz.MergerMenuElement(trees,elementList,checkedMenuIdList,checkedElementIdList);

        return TreeUtil.bulid(trees,root) ;
    }


}
