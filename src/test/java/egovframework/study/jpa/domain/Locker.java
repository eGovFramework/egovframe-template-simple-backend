package egovframework.study.jpa.domain;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * fileName       : Locker
 * author         : crlee
 * date           : 2023/04/28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/04/28        crlee       최초 생성
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Locker {
    @Id
    private Long id;

    private String name;

    @Builder.Default
    @OneToOne(mappedBy = "locker" , cascade = CascadeType.PERSIST)
    private Member member = new Member();
}