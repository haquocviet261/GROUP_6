package com.petshop.repositories;

import com.petshop.models.entities.OnlineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OnlineStatusRepository extends JpaRepository<OnlineStatus,Long> {
    @Query("select o from OnlineStatus o where o.user.user_id =:user_id")
    public OnlineStatus getOnlineStatusByUserID(@Param("user_id")Long user_id);
}
