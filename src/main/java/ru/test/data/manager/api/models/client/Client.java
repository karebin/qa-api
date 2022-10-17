package ru.test.data.manager.api.models.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.test.data.manager.api.models.product.Product;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;


@Data
@Table(schema = "public", name = "client")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Client {

    @NonNull
    private long id;
    @NonNull
    private String firstName;
    private String lastName;
    private ContactInfo contactInfo;
    private List<Product> products;

    public Client(@NonNull long id, @NonNull String firstName, String lastName, ContactInfo contactInfo) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactInfo = contactInfo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id && firstName.equals(client.firstName) && Objects.equals(lastName, client.lastName) && Objects.equals(contactInfo, client.contactInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, contactInfo);
    }
}
