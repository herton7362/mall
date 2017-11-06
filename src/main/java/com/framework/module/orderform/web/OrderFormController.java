package com.framework.module.orderform.web;

import com.framework.module.orderform.domain.OrderForm;
import com.framework.module.orderform.service.OrderFormService;
import com.kratos.common.AbstractCrudController;
import com.kratos.common.CrudService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "订单管理")
@RestController
@RequestMapping("/api/orderForm")
public class OrderFormController extends AbstractCrudController<OrderForm> {
    private final OrderFormService orderFormService;
    @Override
    protected CrudService<OrderForm> getService() {
        return orderFormService;
    }

    /**
     * 下订单
     */
    @ApiOperation(value="下订单")
    @RequestMapping(value = "/makeOrder", method = RequestMethod.POST)
    public ResponseEntity<OrderForm> makeOrder(@RequestBody OrderForm orderForm) throws Exception {
        return new ResponseEntity<>(orderFormService.makeOrder(orderForm), HttpStatus.OK);
    }

    /**
     * 支付
     */
    @ApiOperation(value="支付")
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public ResponseEntity<OrderForm> pay(@RequestBody OrderForm orderForm) throws Exception {
        return new ResponseEntity<>(orderFormService.pay(orderForm), HttpStatus.OK);
    }

    /**
     * 获取订单数量
     */
    @ApiOperation(value="获取订单数量")
    @RequestMapping(value = "/count/{memberId}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Integer>> getOrderCounts(@PathVariable String memberId) throws Exception {
        return new ResponseEntity<>(orderFormService.getOrderCounts(memberId), HttpStatus.OK);
    }

    /**
     * 获取订单所有状态及对应编码
     */
    @ApiOperation(value="获取订单所有状态及对应编码")
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, String>>> getOrderStatus() throws Exception {
        List<Map<String, String>> result = new ArrayList<>();
        Map<String, String> map;
        OrderForm.OrderStatus[] orderStatuses = OrderForm.OrderStatus.values();
        for (OrderForm.OrderStatus orderStatus : orderStatuses) {
            map = new HashMap<>();
            map.put("id", orderStatus.name().toLowerCase());
            map.put("text", orderStatus.getDisplayName());
            result.add(map);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Autowired
    public OrderFormController(OrderFormService orderFormService) {
        this.orderFormService = orderFormService;
    }
}
