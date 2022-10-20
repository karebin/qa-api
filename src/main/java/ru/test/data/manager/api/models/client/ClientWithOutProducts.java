package ru.test.data.manager.api.models.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.persistence.Table;

@Data
@Table(schema = "public", name = "client")
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientWithOutProducts {

    @ApiModelProperty(name = "id",hidden = true)
    private long id;
    private String firstName;
    private String lastName;
    private ContactInfo contactInfo;

    public ClientWithOutProducts(long id, @NonNull String firstName, String lastName, ContactInfo contactInfo) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactInfo = contactInfo;
    }
}
