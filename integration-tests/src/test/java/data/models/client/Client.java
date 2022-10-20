package data.models.client;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import data.models.product.Product;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Client {

    private long id;
    private String firstName;
    private String lastName;
    private ContactInfo contactInfo;
    private List<Product> products;

    public Client(long id, String firstName, String lastName, ContactInfo contactInfo) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactInfo = contactInfo;
    }

    public Client() {

    }

    public Client(long id, String firstName, String lastName, ContactInfo contactInfo, List<Product> products) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactInfo = contactInfo;
        this.products = products;
    }
}
