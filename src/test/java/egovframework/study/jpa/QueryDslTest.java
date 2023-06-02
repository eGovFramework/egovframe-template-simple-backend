package egovframework.study.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import egovframework.study.jpa.domain.Member;
import egovframework.study.jpa.domain.QMember;
import org.junit.jupiter.api.*;
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

@DataJpaTest
@Transactional
public class QueryDslTest {

    @Autowired
    private TestEntityManager entityManager;

    private JPAQueryFactory queryFactory;

    @BeforeEach
    public void setup() {
        queryFactory = new JPAQueryFactory(entityManager.getEntityManager());
    }

    @Test
    @DisplayName("QueryDSL로 selectList")
    public void testSelectList() {
        insertTestMembers(30);

        QMember member = QMember.member;
        List<Member> members = queryFactory
                .selectFrom(member)
                .where(member.age.between(10, 15))
                .orderBy(member.username.asc())
                .fetch();

        assertThat(members.size()).isEqualTo(6);
        assertThat(members.get(0).getUsername()).isEqualTo("member_name10");
        assertThat(members.get(5).getUsername()).isEqualTo("member_name15");
    }

    @Test
    @DisplayName("QueryDSL로 selectList - 동적")
    public void testSelectListDynamic() throws CloneNotSupportedException {
        insertTestMembers(30);

        QMember member = QMember.member;
        BooleanBuilder builder = createAgeNameFilter(member, 10);

        List<Member> members = queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();

        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getUsername()).isEqualTo("member_name10");
        assertThat(members.get(0).getAge()).isEqualTo(10);
    }
    private BooleanBuilder createAgeNameFilter(QMember member, int age) {
        BooleanBuilder builder = new BooleanBuilder();

        if (age == 10) {
            builder.and(member.age.eq(10));
        } else {
            builder.and(member.age.eq(15)).or(member.username.contains("member_name"));
        }
        return builder;
    }
    @Test
    @DisplayName("QueryDSL로 selectOne")
    public void testSelectOne() {
        insertTestMembers(5);

        QMember member = QMember.member;
        Member members = queryFactory
                .selectFrom(member)
                .where(member.age.eq(3))
                .fetchOne();

        assertThat(members.getUsername()).isEqualTo("member_name3");
        assertThat(members.getAge()).isEqualTo(3);
    }

    private void insertTestMembers(int count) {
        for (int i = 1; i <= count; i++) {
            entityManager.persist(Member.builder()
                    .id("member_id" + i)
                    .username("member_name" + i)
                    .age(i)
                    .build());
        }
    }


}
