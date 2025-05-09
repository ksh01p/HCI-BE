// SummaryResponse.java
package com.hci.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class SummaryResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 5000)
    private String summary;

    public SummaryResponse(Integer id, String summary) {
        this.id = id;
        this.summary = summary;
    }
}
