package com.dzjt.cbs.uam.security.common.exception.auth;


import com.dzjt.cbs.uam.security.common.constant.CommonConstants;
import com.dzjt.cbs.uam.security.common.exception.BaseException;

/**
 * Created by XT on 2018/2/12.
 */
public class ClientForbiddenException extends BaseException {
    public ClientForbiddenException(String message) {
        super(message, CommonConstants.EX_CLIENT_FORBIDDEN_CODE);
    }

}
