package com.dzjt.cbs.uam.security.admin.biz;

import com.dzjt.cbs.uam.security.admin.constant.AdminCommonConstant;
import com.dzjt.cbs.uam.security.admin.entity.Element;
import com.dzjt.cbs.uam.security.admin.entity.Menu;
import com.dzjt.cbs.uam.security.admin.mapper.MenuMapper;
import com.dzjt.cbs.uam.security.admin.vo.ElementVo;
import com.dzjt.cbs.uam.security.admin.vo.MenuTree;
import com.dzjt.cbs.uam.security.common.biz.BaseBiz;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uam.cache.annotation.Cache;
import com.uam.cache.annotation.CacheClear;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author XT
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MenuBiz extends BaseBiz<MenuMapper, Menu> {


    @Override
    @Cache(key="permission:menu")
    public List<Menu> selectListAll() {
        return super.selectListAll();
    }

    @Override
    @CacheClear(keys={"permission:menu","permission"})
    public void insertSelective(Menu entity) {
        if (AdminCommonConstant.ROOT == entity.getParentId()) {
            entity.setPath("/" + entity.getCode());
        } else {
            Menu parent = this.selectById(entity.getParentId());
            entity.setPath(parent.getPath() + "/" + entity.getCode());
        }
        super.insertSelective(entity);
    }

    @Override
    @CacheClear(keys={"permission:menu","permission"})
    public void updateById(Menu entity) {
        if (AdminCommonConstant.ROOT == entity.getParentId()) {
            entity.setPath("/" + entity.getCode());
        } else {
            Menu parent = this.selectById(entity.getParentId());
            entity.setPath(parent.getPath() + "/" + entity.getCode());
        }
        super.updateById(entity);
    }

    @Override
    @CacheClear(keys={"permission:menu","permission"})
    public void updateSelectiveById(Menu entity) {
        super.updateSelectiveById(entity);
    }

    /**
     * 获取用户可以访问的菜单
     *
     * @param id
     * @return
     */
    @Cache(key = "permission:menu:u{1}")
    public List<Menu> getUserAuthorityMenuByUserId(int id) {
        return mapper.selectAuthorityMenuByUserId(id);
    }


    /**
     * 根据用户获取可以访问的系统
     *
     * @param id
     * @return
     */
    public List<Menu> getUserAuthoritySystemByUserId(int id) {
        return mapper.selectAuthoritySystemByUserId(id);
    }


    public static void MergerMenuElement(List<MenuTree> menus, List<Element> elementList,
                                         List<Integer> checkedMenuIdList,List<Integer> checkedElementIdList){
        if(menus==null||elementList==null){
            return;
        }
        final Map<Integer,List<ElementVo>> map = Maps.newHashMap();
        elementList.stream().forEach(e->{
            ElementVo vo = new ElementVo();
            BeanUtils.copyProperties(e, vo);
            if(checkedElementIdList!=null){
                if(checkedElementIdList.contains(vo.getId())) {
                    vo.setChecked(true);
                }else {
                    vo.setChecked(false);
                }
            }
            if(!map.containsKey(e.getMenuId())) {
                map.put(e.getMenuId(), Lists.newArrayList(vo));
            }else{
                map.get(e.getMenuId()).add(vo);
            }
        });
        if(!map.isEmpty()) {
            menus.parallelStream().forEach(m -> {
                m.setElementList(map.get(m.getId()));
                if(checkedMenuIdList!=null){
                    if(checkedMenuIdList.contains(m.getId())) {
                        m.setChecked(true);
                    }else {
                        m.setChecked(false);
                    }
                }
            });
        }

    }
}
