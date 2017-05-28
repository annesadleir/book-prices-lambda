package uk.co.littlestickyleaves.bookprices.domain;

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
}
