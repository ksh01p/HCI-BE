package com.hci.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TextRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 5000)
    private String content;

    public TextRequest(Integer id, String content) {
        this.id = id;
        this.content = content;
    }

}
