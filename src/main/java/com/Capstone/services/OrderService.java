package com.Capstone.services;

import com.Capstone.Model.OrderDTO;
import com.Capstone.Model.Order;
import com.Capstone.repository.AwsServiceRepository;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private AwsServiceRepository awsServiceRepository;

    public Order addOrderToTable(OrderDTO newOrder) {
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(Order.class);

        tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
        Order order = new Order();
        order.setOrderName(newOrder.getOrderName());
        order.setOrderStatus(newOrder.getStatus());
        order.setUserId(newOrder.getUserId());
        order.setQuantity(newOrder.getQuantity());
        order.setUnitPrice(newOrder.getUnitPrice());
        order.setCategory(newOrder.getCategory());
        return awsServiceRepository.save(order);

    }

    public List<String> retrieveOrders(String name) {
        List<String> finalResult = new ArrayList<String>();
        Map<String, AttributeValue> expressionAttributeValues =
                new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":val", new AttributeValue().withS(name));

        ScanRequest scanRequest = new ScanRequest()
                .withTableName("myOrders")
                .withFilterExpression("username = :val")
                .withExpressionAttributeValues(expressionAttributeValues);


        ScanResult result = amazonDynamoDB.scan(scanRequest);
        for (Map<String, AttributeValue> item : result.getItems()) {
            finalResult.add(item.toString());

        }

        return finalResult;


    }

    public String getAllOrders(Integer userId) {
        List<String> finalResult = new ArrayList<String>();
        Map<String, AttributeValue> expressionAttributeValues =
                new HashMap<String, AttributeValue>();
        expressionAttributeValues.put(":val", new AttributeValue().withN(userId.toString()));

        ScanRequest scanRequest = new ScanRequest()
                .withTableName("myOrders")
                .withFilterExpression("userId = :val")
                .withExpressionAttributeValues(expressionAttributeValues);


        ScanResult result = amazonDynamoDB.scan(scanRequest);
        for (Map<String, AttributeValue> item : result.getItems()) {
            finalResult.add(item.get("category").getS());
            System.out.println(item.get("category").toString());

        }
        HashMap<String, Integer> elementCountMap = new HashMap<>();

        for (int i = 0; i < finalResult.size(); i++)
        {
            if (elementCountMap.containsKey(finalResult.get(i)))
            {
                //If element is present in elementCountMap, increment its value by 1

                elementCountMap.put(finalResult.get(i), elementCountMap.get(finalResult.get(i))+1);
            }
            else
            {
                //If element is not present, insert this element with 1 as its value

                elementCountMap.put(finalResult.get(i), 1);
            }
        }

        //Create an ArrayList to hold sorted elements

        ArrayList<String> sortedElements = new ArrayList<>();

        //Java 8 code to sort elementCountMap by values in reverse order
        //and put keys into sortedElements list

        elementCountMap.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .forEach(entry -> {
                    for(int i = 1; i <= entry.getValue(); i++)
                        sortedElements.add(entry.getKey());
                });

        return sortedElements.get(0);
    }

    public OrderDTO getOrder(String orderId) {
        Optional<Order> result = awsServiceRepository.findById(orderId);
        Order order = result.get();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderName(order.getOrderName());
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setStatus(order.getOrderStatus());
        orderDTO.setUnitPrice(order.getUnitPrice());
        orderDTO.setUserId(order.getUserId());
        orderDTO.setCategory(order.getCategory());
        return orderDTO;
    }

    public void changeOrderStatus(String id, String status) {
        Optional<Order> result = awsServiceRepository.findById(id);
        Order order = result.get();
        order.setOrderStatus(status);
        awsServiceRepository.save(order);

    }

}

