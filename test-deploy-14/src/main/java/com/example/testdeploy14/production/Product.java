package com.example.testdeploy14.production;

import javax.persistence.*;

@Entity
@Table(name = "product")

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long id;

    private String name;

    private String linkImg;
    private int year;
    private String price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

//    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
//    private Set<OrderDetail> orderDetails;


    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getLinkImg() {
        return this.linkImg;
    }

    public void setLinkImg(String linkImg) {
        this.linkImg = linkImg;
    }

    public Product() {
    }

    public Product(String name, int year, String price, Category category, Brand brand, String linkImg) {
        super();
        this.name = name;
        this.year = year;
        this.price = price;
        this.category = category;
        this.brand = brand;
        this.linkImg = linkImg;
    }


}
