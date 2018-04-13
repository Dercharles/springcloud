package com.dzjt.cbs.uam.security.common.exception.auth;


import com.dzjt.cbs.uam.security.common.constant.CommonConstants;
import com.dzjt.cbs.uam.security.common.exception.BaseException;

/**
 * Created by XT on 2018/2/8.
 */
public class UserTokenException extends BaseException {
    public UserTokenException(String message) {
        super(message, CommonConstants.EX_USER_INVALID_CODE);
    }
}
