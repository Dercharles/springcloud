package com.dzjt.cbs.uam.security.admin.mapper;

import com.dzjt.cbs.uam.security.admin.entity.UserCompany;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserCompanyMapper extends Mapper<UserCompany> {

//    public List<UserCompany> selectCompanyIdByUserId (@Param("userId") int userId);


    @Select("select * from base_user_company where user_id = #{userId}")
    @Results({
            @Result(column="company_id", property="companyId"),
            @Result(column="company_name", property="companyName"),
    })
    public List<UserCompany> getCompanyIdByUserId(int userId);

    @Delete("delete from base_user_company where company_id = #{companyId} and user_id = #{userId}")
    public int deleteByUserCompanyId(@Param(value = "companyId") int companyId,@Param(value = "userId") int userId);

    @Delete("delete from base_user_company where user_id = #{userId}")
    public int deleteByUserId(int userId);

}
