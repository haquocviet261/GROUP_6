package com.petshop.models.dto.response;

import com.petshop.models.entities.SubCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NamedNativeQuery;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NamedNativeQuery(
        name = "ProductDiscountResult",
        query = "SELECT TOP 8 p.product_id, p.sub_category_id, p.product_name, p.quantity, p.price, \" +\n" +
                "                    \"p.description, p.product_image, d.discount_value, s.category_id \" +\n" +
                "                    \"FROM products p LEFT JOIN discount d ON p.discount_id = d.discount_id inner join sub_categories s on p.sub_category_id = s.sub_category_id ORDER BY NEWID()",
        resultSetMapping = "ProductDiscountResultMapping"
)
@SqlResultSetMapping(
        name = "ProductDiscountResultMapping",
        classes = @ConstructorResult(
                targetClass = DiscountProductResponse.class,
                columns = {
                        @ColumnResult(name = "product_id", type = Long.class),
                        @ColumnResult(name = "category_id", type = Long.class),
                        @ColumnResult(name = "sub_category_id", type = Long.class),
                        @ColumnResult(name = "product_name", type = String.class),
                        @ColumnResult(name = "quantity", type = Integer.class),
                        @ColumnResult(name = "price", type = Double.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "product_image", type = String.class),
                        @ColumnResult(name = "discount_value", type = Double.class)
                }
        )
)
@Entity
public class DiscountProductResponse {
    @Id
    private  Long product_id;
    private Long category_id;
    private Long sub_category_id;
    private String product_name;
    private int quantity;
    private double price;
    private String description;
    private String product_image;
    private double discount_value;

}
