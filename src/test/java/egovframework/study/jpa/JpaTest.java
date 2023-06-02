package egovframework.study.jpa;

import egovframework.study.jpa.domain.Locker;
import egovframework.study.jpa.domain.Member;
import egovframework.study.jpa.domain.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * fileName       : jpaTest
 * author         : crlee
 * date           : 2023/04/28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/04/28        crlee       최초 생성
 */
@DataJpaTest
@TestInstance(TestInstance. Lifecycle.PER_CLASS)
public class JpaTest {

    @PersistenceUnit
    EntityManagerFactory emf; //엔티티 매니저 팩토리 생성
    EntityManager em;
    EntityTransaction tx;

    @BeforeAll
    public void init(){
        this.em = emf.createEntityManager();
    }
    @BeforeEach
    public void open(){
        this.tx = em.getTransaction();
        this.tx.begin(); //트랜잭션 시작
        this.setData();
    }
    @AfterEach
    public void close(){
        this.tx.rollback();
    }
    @AfterAll
    public void end(){
        this.em.close(); //엔티티 매니저 종료
        this.emf.close();
    }
    void setData(){
        Team team1 = Team.builder()
                .id("team1")
                .name("team_name1")
                .build();
        em.persist(team1);

        for (int i=1 ; i<30 ; i++ ){

            em.persist(
                    Member.builder()
                            .id("member_id"+i)
                            .username("member_name"+i)
                            .age(2)
                            .team(team1)
                            .build()
            );

        }
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("기본 select 작업을 테스트")
    void testSelectById(){
        Member findMember = em.find(Member.class,"member_id1");
        Assertions.assertThat( findMember.getUsername() ).isEqualTo( "member_name1" );
    }


    @Test
    @DisplayName("기본 update 작업 테스트")
    void testUpdate(){
        Member updateMember = Member.builder()
                .id("member_id1")
                .username("UPDATE_NAME1")
                .age(20)
                .build();
        em.persist( updateMember );

        Member findMember = em.find(Member.class,"member_id1");
        Assertions.assertThat( findMember.getUsername() ).isEqualTo( updateMember.getUsername() );
    }

    @Test
    @DisplayName("변경감지 Update Test")
    void testDirtyChecking(){
        Member updateMember = em.find(Member.class,"member_id1");
        updateMember.setUsername("UPDATE_NAME");

        Member findMember = em.find(Member.class,"member_id1");
        Assertions.assertThat( findMember.getUsername() ).isEqualTo( updateMember.getUsername() );
    }

    @Test
    @DisplayName("객체 그래프 Test")
    void fetchTest(){
        Member findMember = em.find(Member.class,"member_id1");
        Assertions.assertThat( findMember.getTeam().getName() ).isEqualTo( "team_name1" );

        Team findTeam = em.find(Team.class,"team1");
        Assertions.assertThat( findTeam.getName() ).isEqualTo( "team_name1" );
        Assertions.assertThat( findTeam.getMembers().size() ).isEqualTo( 29 );
    }

    @Test
    @DisplayName("cascade Test")
    public void cascadeTest(){
        Member member1 = Member.builder()
                .id("mem1")
                .username("name1")
                .age(1)
                .build();
        Locker locker1 = Locker.builder().id(1L).name("LockerNm1").member(member1).build();
        em.persist(locker1);
        // Member는 persist()하지 않았지만 영속상태
        Assertions.assertThat( em.contains(member1) ).isTrue();
        Assertions.assertThat( em.contains(locker1) ).isTrue();
    }

    @Test
    @DisplayName("select * from member;")
    void ParameterTest(){
        TypedQuery query = em.createQuery( "select m from Member m where m.username = :username" , Member.class );
        query.setParameter("username" , "member_name1" );
        List<Member> resultList = query.getResultList();
        Assertions.assertThat( resultList.size() ).isEqualTo(1);
        Assertions.assertThat( resultList.get(0).getAge() ).isEqualTo(2);
        Assertions.assertThat( resultList.get(0).getId() ).isEqualTo("member_id1");
    }

    @Test
    @DisplayName("select username from member;")
    void projectionTest(){
        Query query = em.createQuery( "select m.username from Member m where m.username = :username");
        query.setParameter("username" , "member_name1" );
        List<String> resultList = query.getResultList();
        Assertions.assertThat( resultList.get(0) ).isEqualTo("member_name1");
    }

    @Test
    @DisplayName("Criteria Test ")
    void CriteriaTest(){
        // 사용 준비
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Member> query = criteriaBuilder.createQuery(Member.class);

        // 루트 클래스( 조회를 시작할 클래스 )
        Root<Member> memberRoot = query.from(Member.class);

        // 쿼리 생성
        CriteriaQuery<Member> criteriaQuery = query.select(memberRoot).where( criteriaBuilder.equal( memberRoot.get("age"),2 ) );
        List<Member> resultList = em.createQuery(criteriaQuery).getResultList();
        Assertions.assertThat( resultList.size() ).isEqualTo( 29 );
    }
}
