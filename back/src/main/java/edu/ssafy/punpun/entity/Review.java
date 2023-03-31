package edu.ssafy.punpun.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;
    private String content;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    @ToString.Exclude
    private Reservation reservation;
    @ManyToOne(targetEntity = Store.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
    @ManyToOne(targetEntity = Child.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    private Child child;
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ReviewKeyword> reviewKeywords = new ArrayList<>();

    public void setReviewKeywords(ReviewKeyword reviewKeyword) {
        this.reviewKeywords.add(reviewKeyword);
    }
}
