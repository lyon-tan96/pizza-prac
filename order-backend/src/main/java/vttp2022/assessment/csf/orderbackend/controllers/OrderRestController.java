package vttp2022.assessment.csf.orderbackend.controllers;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;
import vttp2022.assessment.csf.orderbackend.repositories.Response;
import vttp2022.assessment.csf.orderbackend.services.OrderService;

@RestController
@RequestMapping("/api")
public class OrderRestController {

    @Autowired
    OrderService orderSvc;

    @PostMapping("/order")
    public ResponseEntity<String> createOrder(@RequestBody String payload) {

        Response resp = new Response();
        System.out.println(payload);
        
        Order order = Order.create(payload);
        orderSvc.createOrder(order);

        resp.setCode(200);
        resp.setMessage("Order created successfully!");

        return ResponseEntity.status(HttpStatus.CREATED).body(resp.toJson().toString());
    }

    @GetMapping("/order/{email}/all")
    public ResponseEntity<String> getOrderItems(@PathVariable String email) {

        List<OrderSummary> orderSummaries = orderSvc.getOrdersByEmail(email);

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for(OrderSummary orderSummary: orderSummaries) {
            arrayBuilder.add(orderSummary.toJson());
        }

        return ResponseEntity.ok(arrayBuilder.build().toString());
    }
}
