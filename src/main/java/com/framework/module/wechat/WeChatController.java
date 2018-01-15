package com.framework.module.wechat;

import com.framework.module.orderform.domain.OrderForm;
import com.framework.module.orderform.service.OrderFormService;
import com.kratos.common.utils.XmlUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "订单管理")
@Controller
@RequestMapping("/wechat")
public class WeChatController {
    private final OrderFormService orderFormService;
    /**
     * 支付结果的异步消息接收
     */
    @RequestMapping(value = "/pay/notify", method = RequestMethod.POST)
    public void payNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServletInputStream in = request.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder reqData = new StringBuilder();
        String itemStr;// 作为输出字符串的临时串，用于判断是否读取完毕
        while (null != (itemStr = reader.readLine())) {
            reqData.append(itemStr);
        }
        String outTradeNo = (String) XmlUtils.xmltoMap(reqData.toString()).get("out_trade_no");
        Map<String, String[]> params = new HashMap<>();
        params.put("orderNumber", new String[] {outTradeNo});
        List<OrderForm> orderForm = orderFormService.findAll(params);
        orderFormService.pay(orderForm.get(0));
        String resultMsg = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml> ";
        PrintWriter out = response.getWriter();
        out.write(resultMsg);
        out.flush();
        out.close();
    }

    @Autowired
    public WeChatController(OrderFormService orderFormService) {
        this.orderFormService = orderFormService;
    }
}
