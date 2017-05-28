package uk.co.littlestickyleaves.bookprices.domain;

/**
 * Class to hold an ISBN
 * Could be extended to include other book info?
 */
public class ISBN {

    private String isbn;

    public ISBN() {
    }

    public ISBN(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
