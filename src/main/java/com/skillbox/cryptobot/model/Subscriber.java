package com.skillbox.cryptobot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscriber", schema = "crypto_bot")
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
public class Subscriber {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "uuid", columnDefinition = "INT")
  private Integer uuid;

  @Column(name = "telegram_id", nullable = false, columnDefinition = "BIGINT", unique = true)
  private Long telegramId;

  @Column(name = "price", columnDefinition = "DECIMAL")
  private Double price;
}
