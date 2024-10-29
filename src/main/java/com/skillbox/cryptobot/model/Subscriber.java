package com.skillbox.cryptobot.model;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(
        name = "subscriber`",
        schema = "crypto_bot"
//        indexes = {
////                @Index(name = "idx_lemma_id_page_id", columnList = "lemma_id, page_id"),
////                @Index(name = "idx_rank_lemma_id_page_id", columnList = "rank,lemma_id, page_id")
////        }
)

@Builder
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uuid", columnDefinition = "INT")
    private Integer uuid;

    @Column(name = "tId", nullable = false, columnDefinition = "INT", unique = true)
    private Integer tId;

    @Column(columnDefinition = "TEXT")
    private String price;
}
