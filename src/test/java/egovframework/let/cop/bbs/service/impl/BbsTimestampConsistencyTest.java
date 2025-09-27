package egovframework.let.cop.bbs.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
@DisplayName("MySQL 타임스탬프 함수 동작 방식 비교")
public class BbsTimestampConsistencyTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	@DisplayName("증명 1: SYSDATE()는 하나의 SQL문 내에서도 시간이 변경된다.")
	void sysdate_is_inconsistent_in_a_single_statement() {
		// 단 하나의 SQL 문: SYSDATE()를 호출하고, 1초 쉬고, 다시 SYSDATE()를 호출
		String sql = "SELECT SYSDATE() AS time1, SLEEP(1), SYSDATE() AS time2";

		// SQL 실행 및 결과 저장
		Map<String, Object> result = jdbcTemplate.queryForMap(sql);
		LocalDateTime time1 = (LocalDateTime) result.get("time1");
		LocalDateTime time2 = (LocalDateTime) result.get("time2");

		// 결과 출력
		System.out.println("--- SYSDATE() 테스트 ---");
		System.out.println("첫번째 시간: " + time1);
		System.out.println("두번째 시간: " + time2);

		// 검증: 두 시간은 반드시 달라야 한다.
		assertNotEquals(time1, time2);
		System.out.println("✅ 결론: SYSDATE()는 일관성을 지키지 못함.");
	}

	@Test
	@DisplayName("증명 2: NOW()는 하나의 SQL문 내에서 시간이 고정된다.")
	void now_is_consistent_in_a_single_statement() {
		// 단 하나의 SQL 문: NOW()를 호출하고, 1초 쉬고, 다시 NOW()를 호출
		String sql = "SELECT NOW() AS time1, SLEEP(1), NOW() AS time2";

		// SQL 실행 및 결과 저장
		Map<String, Object> result = jdbcTemplate.queryForMap(sql);
		LocalDateTime time1 = (LocalDateTime) result.get("time1");
		LocalDateTime time2 = (LocalDateTime) result.get("time2");

		// 결과 출력
		System.out.println("\n--- NOW() 테스트 ---");
		System.out.println("첫번째 시간: " + time1);
		System.out.println("두번째 시간: " + time2);

		// 검증: 두 시간은 반드시 같아야 한다.
		assertEquals(time1, time2);
		System.out.println("✅ 결론: NOW() 일관성을 지킴.");
	}
}
