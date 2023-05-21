package egovframework.study.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.study.jpa.domain.Member;
import egovframework.study.jpa.domain.QMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * fileName       : QueryDslTest
 * author         : crlee
 * date           : 2023/05/21
 * description    : Query DSL 테스트 코드
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/05/21        crlee       최초 생성
 */

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // 데이터 JPA 테스트 어노테이션
@Transactional // 트랜잭션 관리 어노테이션
public class QueryDslTest {

    @Autowired
    private TestEntityManager entityManager; // EntityManager를 테스트하기 위한 객체

    private JPAQueryFactory queryFactory; // Querydsl을 사용하기 위한 QueryFactory

    @BeforeEach
    public void setup() {
        queryFactory = new JPAQueryFactory(entityManager.getEntityManager());
    }

    @Test
    public void testQuerydsl() {
        // Given
        for (int i=1 ; i<30 ; i++ ){
            entityManager.persist(Member.builder()
                    .id("member_id"+i)
                    .username("member_name"+i)
                    .age(i)
                    .build());
        }
        // When

        QMember member = QMember.member;
        List<Member> members = queryFactory
                .selectFrom(member)
                .where(member.age.between(10, 15))
                .orderBy(member.username.asc())
                .fetch();

        // Then
        assertThat(members.size()).isEqualTo(6);
        assertThat(members.get(0).getUsername()).isEqualTo("member_name10");
        assertThat(members.get(5).getUsername()).isEqualTo("member_name15");
    }
}