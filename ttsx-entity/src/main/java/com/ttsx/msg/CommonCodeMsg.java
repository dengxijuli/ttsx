package com.ttsx.msg;

public class CommonCodeMsg extends CodeMsg {
    private CommonCodeMsg(Integer code, String msg){
        super(code,msg);
    }
    public static final CommonCodeMsg ILLEGAL_OPERATION = new CommonCodeMsg(-1,"非法操作");
    public static final CommonCodeMsg TOKEN_INVALID = new CommonCodeMsg(-2,"登录超时,请重新登录");
}
