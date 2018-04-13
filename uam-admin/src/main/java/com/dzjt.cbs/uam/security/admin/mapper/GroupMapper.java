package com.dzjt.cbs.uam.security.admin.mapper;

import com.dzjt.cbs.uam.security.admin.entity.Group;
import com.dzjt.cbs.uam.security.admin.entity.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface GroupMapper extends Mapper<Group> {
    public void deleteGroupMembersById (@Param("groupId") int groupId);
    public void deleteGroupLeadersById (@Param("groupId") int groupId);
    public void insertGroupMembersById (@Param("groupId") int groupId,@Param("userId") int userId);
    public void insertGroupLeadersById (@Param("groupId") int groupId,@Param("userId") int userId);

    public void deleteGroupMembersByUserId (@Param("userId") int userId);

    public List<Group> selectGroupByMemberId(@Param("userId") int userId);
}