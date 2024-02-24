package sn.esmt.mp2isi.ecommerce.orderservice.service.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Lob;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String reference;

    @Lob
    private String description;

    private String imageSrc;

    @NotNull
    @Min(value = 0L)
    private Long quantity;

    @NotNull
    @DecimalMin(value = "0")
    private Double price;

    private Boolean active;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", reference='" + getReference() + "'" +
            ", description='" + getDescription() + "'" +
            ", imageSrc='" + getImageSrc() + "'" +
            ", quantity=" + getQuantity() +
            ", price=" + getPrice() +
            ", active='" + getActive() +

            "}";
    }
}
