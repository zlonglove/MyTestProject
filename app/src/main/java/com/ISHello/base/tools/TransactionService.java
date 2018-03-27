package com.ISHello.base.tools;

import com.ISHello.base.net.HttpReqEntity;
import com.ISHello.base.net.HttpRespEntity;
import com.ISHello.base.net.HttpRespEntity.HttpRespResultType;
import com.ISHello.base.net.ICBCHttpClientUtil;

import java.util.HashMap;

/**
 * Created by zhanglong on 2017/2/9.
 */

public class TransactionService {
    // TransactionService请求类型
    public enum TransactionType {
        Normal, // 正常请求
    }

    public static HttpRespEntity executeRequest(HttpReqEntity req, boolean handleErrorFlag) {
        HttpRespEntity result = new HttpRespEntity(HttpRespResultType.HTTP_REQUEST_FAILED);
        try {
            //result = ICBCHttpClientUtil.post(req.getUrl(), req.getReqParams(),req.getTimeout(),false);
            result = ICBCHttpClientUtil.postToGetByte(req.getUrl(), req.getReqParams(), req.getTimeout());
            if (!handleErrorFlag) {
                // 不处理错误，统一返回OK
                result.setResult(HttpRespResultType.HTTP_REQUEST_OK);
                result.setErrorCode("");
                result.setErrorMessage("");
                return result;
            } else {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
