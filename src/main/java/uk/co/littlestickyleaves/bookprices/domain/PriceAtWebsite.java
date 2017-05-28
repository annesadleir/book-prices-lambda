package uk.co.littlestickyleaves.bookprices.domain;

import java.util.Objects;

/**
 * Just holds a price in pounds and a url
 */
public class PriceAtWebsite {

    private String webAddress;

    private double price;

    public PriceAtWebsite(String webAddress, double price) {
        this.webAddress = webAddress;
        this.price = price;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "PriceAtWebsite{" +
                "webAddress='" + webAddress + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        PriceAtWebsite that = (PriceAtWebsite) o;
        return Double.compare(that.price, price) == 0 &&
                Objects.equals(webAddress, that.webAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(webAddress, price);
    }
}
