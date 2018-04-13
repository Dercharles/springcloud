package com.dzjt.cbs.uam.security.common.msg.auth;

import com.dzjt.cbs.uam.security.common.constant.RestCodeConstants;
import com.dzjt.cbs.uam.security.common.msg.BaseResponse;

/**
 * Created by XT on 2018/2/23.
 */
public class TokenErrorResponse extends BaseResponse {
    public TokenErrorResponse(String message) {
        super(RestCodeConstants.TOKEN_ERROR_CODE, message);
    }
}
