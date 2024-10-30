package com.skillbox.cryptobot.repository;

import com.skillbox.cryptobot.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Integer> {



    @Transactional
    @Modifying
    @Query("update Subscriber s set s.price = :price")
    void updatePriceBy(@Param("price") Double price);

}
