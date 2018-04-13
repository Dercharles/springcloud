package com.dzjt.cbs.uam.security.common.msg.auth;

import com.dzjt.cbs.uam.security.common.constant.RestCodeConstants;
import com.dzjt.cbs.uam.security.common.msg.BaseResponse;

/**
 * Created by XT on 2018/2/25.
 */
public class TokenForbiddenResponse  extends BaseResponse {
    public TokenForbiddenResponse(String message) {
        super(RestCodeConstants.TOKEN_FORBIDDEN_CODE, message);
    }
}
