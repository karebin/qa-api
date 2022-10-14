package ru.test.data.manager.api.models.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.Nullable;
import ru.test.data.manager.api.models.product.Product;
import javax.persistence.Table;
import java.lang.annotation.ElementType;
import java.util.List;


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
}
