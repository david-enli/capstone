package com.Capstone.endpoints;


import com.Capstone.Model.OrderDTO;
import com.Capstone.services.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Order Management System", description = "Operations pertaining to orders")
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @ApiOperation(value = "insert an order into table")
    @RequestMapping(value = "/addOrder", method = RequestMethod.POST)
    public String addOrder(@RequestBody OrderDTO order ) throws Exception {
        return orderService.addOrderToTable(order).getId();
    }

    @ApiOperation(value = "retrieve an item that contains the given name")
    @RequestMapping(value = "/getOrders", method = RequestMethod.GET)
    public List<String> getOrders( @ApiParam(value = "given item name", required = true)@RequestParam String itemName) {
        return orderService.retrieveOrders(itemName);
    }

    @ApiOperation(value = "retrieve an order given orderId")
    @RequestMapping(value = "/getOrder", method = RequestMethod.GET)
    public OrderDTO getOrder(@ApiParam(value = "given orderId", required = true)@RequestParam String orderId) {
        return orderService.getOrder(orderId);
    }


    @ApiOperation(value = "modify status of a given order")
    @RequestMapping(value = "/updateStatus/{orderID}/{status}", method = RequestMethod.PUT)
    public void updateStatus(@PathVariable String orderID, @PathVariable String status) {
        orderService.changeOrderStatus(orderID, status);
    }

    @ApiOperation(value = "get most bought item category for given user")
    @RequestMapping(value = "/getTopCategory", method = RequestMethod.GET)
    public String getTopCategory(@ApiParam(value = "given userId", required = true) @RequestParam Integer userId) {
        return orderService.getAllOrders(userId);
    }

}