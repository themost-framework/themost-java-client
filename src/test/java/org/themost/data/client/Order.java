package org.themost.data.client;

import java.time.ZonedDateTime;

public class Order {
    public int id;
    public float discount;
    public String discountCode;
    public String discountCurrency;
    public Boolean isGift;
    public ZonedDateTime orderDate;
    public String orderNumber;
    public ZonedDateTime paymentDue;
    public String paymentUrl;
    public Person customer;
    public Product orderedItem;
    public OrderStatus orderStatus;

}
