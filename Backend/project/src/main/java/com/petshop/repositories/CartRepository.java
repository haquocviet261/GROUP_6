package com.petshop.repositories;

import com.petshop.models.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public interface CartRepository  extends JpaRepository<Cart,Long> {
//    @Query("select c from Cart c where c.product.product_id = :id")
//    public Cart getCartByProductId(@Param("id") Long id);

    @Query("select c from Cart c where c.user.UserId  = :user_id")
    public Cart findCartByUserId(@Param("user_id") Long user_id);
    @Query("select c from Cart c join c.items item where item.item_id = :item_id")
    public Cart findCartByItemId(@Param("item_id") Long item_id);
}
