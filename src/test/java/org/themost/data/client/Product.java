package org.themost.data.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@EdmEntitySetProperty(name = "Products")
public class Product {

    public int id;
    public String category;
    public float price;
    public String model;
    public ZonedDateTime releaseDate;
    public String additionalType;
    public String name;
    public ZonedDateTime dateCreated;
    public ZonedDateTime dateModified;
}
