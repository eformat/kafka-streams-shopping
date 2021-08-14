package org.acme.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Reward {

    private String customerId;
    private Integer points;
    private Double spend;

    public Reward(Purchase purchase) {
        this.customerId = purchase.getCustomerId();
        BigDecimal s = new BigDecimal(purchase.getPrice() * purchase.getQuantity()).setScale(2, RoundingMode.HALF_UP);
        this.spend = s.doubleValue();
        BigDecimal p = new BigDecimal(this.spend).setScale(0, RoundingMode.HALF_UP);
        this.points = p.intValue() * 10;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Double getSpend() {
        return spend;
    }

    public void setSpend(Double spend) {
        this.spend = spend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reward reward = (Reward) o;
        return Objects.equals(customerId, reward.customerId) && Objects.equals(points, reward.points) && Objects.equals(spend, reward.spend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, points, spend);
    }

    @Override
    public String toString() {
        return "Reward{" +
                "customerId='" + customerId + '\'' +
                ", points=" + points +
                ", spend=" + spend +
                '}';
    }
}
