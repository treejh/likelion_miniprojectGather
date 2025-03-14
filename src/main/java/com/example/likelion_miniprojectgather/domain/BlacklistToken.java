
package com.example.likelion_miniprojectgather.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="blacklist_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String value;

    //true 만료, false 유효
    private LocalDateTime expired;

    public BlacklistToken(String value, LocalDateTime expired) {
        this.value = value;
        this.expired = expired;
    }

}