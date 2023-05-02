package egovframework.study.jpa.domain;

import lombok.*;

import javax.persistence.*;

/**
 * fileName       : Member
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
@ToString(exclude = { "team" , "locker"})
@Entity
public class Member {
    @Id
    private String id;

    private String username;

    private int age;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="TEAM_ID")
    private Team team;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="LOCKER_ID")
    private Locker locker;

}
