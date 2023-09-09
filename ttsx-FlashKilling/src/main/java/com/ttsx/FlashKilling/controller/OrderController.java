package com.ttsx.FlashKilling.controller;

import com.ttsx.FlashKilling.Service.IOrderInfoService;
import com.ttsx.constant.Constant;
import com.ttsx.entity.pojo.SecKillOrderInfo;
import com.ttsx.feignApi.AlipayFeignApi;
import com.ttsx.msg.CommonCodeMsg;
import com.ttsx.msg.SeckillCodeMsg;
import com.ttsx.utils.JWTUtils;
import com.ttsx.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/fk/order")
public class OrderController {
    @Resource
    private IOrderInfoService iOrderInfoService;
    @Resource
    private AlipayFeignApi alipayFeignApi;
    @Value("${alipay.errorUrl}")
    private String errorUrl;
    @Value("${alipay.frontEndPayUrl}")
    private String frontEndPayUrl;

    @GetMapping("find")
    public R find(@RequestParam("orderNo") Object orderNo, HttpServletRequest request) {
        String token = request.getHeader(Constant.TOKEN_NAME);
        String userid = (String) JWTUtils.getTokenInfo(token).get("userid");
        SecKillOrderInfo orderInfo = iOrderInfoService.getById((Serializable) orderNo);
        if (orderInfo == null) {
            return R.error(CommonCodeMsg.ILLEGAL_OPERATION.getMsg());
        }
        if (!userid.equals(String.valueOf(orderInfo.getUserId()))) {
            return R.error(CommonCodeMsg.ILLEGAL_OPERATION.getMsg());
        }
        return R.success(orderInfo);

    }

    @GetMapping("pay")
    public R<String> pay(@RequestParam("orderNo") Object orderNo,
                 @RequestParam(value = "type", required = false) Object type) {
        String html = null;
        try {
            if(SecKillOrderInfo.PAYTYPE_INTERGRAL.equals(type)){
                iOrderInfoService.payIntergral(String.valueOf(orderNo));
                return R.success("操作成功");
            }else{
                html = iOrderInfoService.payOnline(orderNo);
                return R.success(html);
            }
        } catch (Exception e) {
            log.error("支付异常,msg:{}", e.getMessage());
            return R.error(SeckillCodeMsg.PAY_ERROR.getMsg());
        }
    }


    @RequestMapping("/refund")
    public R<String> refund(String orderNo) {
        SecKillOrderInfo orderInfo = iOrderInfoService.getById(orderNo);
        try {
            if (SecKillOrderInfo.PAYTYPE_ONLINE.equals(orderInfo.getPayType())) {
                //现金支付
                iOrderInfoService.refundOnline(orderInfo);
            } else {   // 积分退款
                iOrderInfoService.refundIntergral(orderInfo);
            }
        } catch (Exception e) {
            log.error("退款异常，msg{}", e.getMessage());
            return R.error(SeckillCodeMsg.REFUND_ERROR.getMsg());
        }
        return R.success("操作成功");
    }

    @RequestMapping("/notify_url")
    public String notifyUrl(@RequestParam Map<String, String> params) {
        System.out.println("异步回调");
        try {
            R<Boolean> result = alipayFeignApi.rsaCheckV1(params);
            if (result == null || result.getCode() == 0) {
                return "fail";
            }
            boolean signVerified = result.getData();
            if (signVerified) {
                String orderNo = params.get("out_trade_no");
                iOrderInfoService.paySuccess(orderNo);
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception e) {
            log.error("支付异步回调异常,msg: {}", e.getMessage());
            return "fail";
        }
    }

    @RequestMapping("/return_url")
    public void returnUrl(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
        System.out.println("同步回调");
        try {
            R<Boolean> result = alipayFeignApi.rsaCheckV1(params);
            if (result == null || result.getCode() == 0) {
                response.sendRedirect(errorUrl);
                return;
            }
            boolean signVerified = result.getData();
            if (signVerified) {
                String orderNo = params.get("out_trade_no");
                response.sendRedirect(frontEndPayUrl + orderNo);
            } else {
                response.sendRedirect(errorUrl);
            }
        } catch (Exception e) {
            log.error("支付同步回调异常,msg : {}", e.getMessage());
            response.sendRedirect(errorUrl);
        }

    }

}
