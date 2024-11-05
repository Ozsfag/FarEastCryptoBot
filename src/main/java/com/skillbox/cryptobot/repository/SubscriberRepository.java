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

  @Query("select (count(s) > 0) from Subscriber s where s.telegramId = :telegramId")
  boolean existsByTelegramId(@Param("telegramId") Long telegramId);

  @Transactional
  @Modifying
  @Query("UPDATE Subscriber s SET s.price = :price WHERE s.telegramId = :telegramId")
  void updatePriceByTelegramId(@Param("price") Double price, @Param("telegramId") Long telegramId);

  @Query("select s.price from Subscriber s where s.telegramId = :telegramId")
  Double findByTelegramId(@Param("telegramId") Long telegramId);

}
