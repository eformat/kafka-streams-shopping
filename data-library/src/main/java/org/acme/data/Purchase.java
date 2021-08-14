package org.acme.data;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class Purchase {

    private String customerId;
    private String item;
    private int quantity;
    private double price;

    private Date transactionDate;
    private PurchaseKey purchaseKey;

    public Purchase(String customerId, String item, int quantity, double price) {
        this.item = item;
        this.quantity = quantity;
        this.price = price;
        this.customerId = customerId;
        this.transactionDate = Date.from(Instant.now());
        this.purchaseKey = new PurchaseKey(customerId, this.transactionDate);
    }

    public PurchaseKey getPurchaseKey() {
        return purchaseKey;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }


    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase purchase = (Purchase) o;
        return quantity == purchase.quantity && Double.compare(purchase.price, price) == 0 && Objects.equals(customerId, purchase.customerId) && Objects.equals(item, purchase.item) && Objects.equals(transactionDate, purchase.transactionDate) && Objects.equals(purchaseKey, purchase.purchaseKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, item, quantity, price, transactionDate, purchaseKey);
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "customerId='" + customerId + '\'' +
                ", item='" + item + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", transactionDate=" + transactionDate +
                ", purchaseKey=" + purchaseKey +
                '}';
    }
}
