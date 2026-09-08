package egovframework.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import egovframework.let.cop.bbs.domain.model.BoardVO;

/** DB에 연결하지 않고 실제 매퍼의 검색 조건 생성과 파라미터 바인딩을 검증한다. */
@DisplayName("게시글 작성자 검색 매퍼")
class EgovBoardAuthorSearchMapperTest {

	private static final List<String> DB_TYPES = List.of("hsql", "mysql", "oracle", "tibero", "altibase", "cubrid");
	private static final List<String> STATEMENTS = List.of("selectBoardArticleList", "selectBoardArticleListCnt");
	private static final String SEARCH_WORD = "표시작성자' OR 1=1 --";

	@ParameterizedTest(name = "{0}: {1}")
	@MethodSource("mapperStatements")
	@DisplayName("작성자 검색은 현재 회원명을 우선하고 없으면 글에 저장된 이름을 사용한다")
	void authorSearchUsesDisplayedName(String dbType, String statementId) throws Exception {
		MappedStatement statement = mappedStatement(dbType, statementId);
		BoardVO board = search("2");
		BoundSql boundSql = statement.getBoundSql(board);
		String sql = normalize(boundSql.getSql());
		String nameExpression = (List.of("hsql", "mysql").contains(dbType) ? "IFNULL" : "NVL")
				+ "(B.USER_NM, A.NTCR_NM)";

		assertTrue(sql.contains("AND " + nameExpression + " LIKE "), sql);
		assertEquals(1, occurrences(sql, " LIKE "), sql);
		if (statementId.equals("selectBoardArticleList")) {
			assertTrue(sql.contains(nameExpression + " AS FRST_REGISTER_NM"), sql);
		}
		assertFalse(sql.contains(" OR "), "과거 작성자명을 별도의 OR 조건으로 검색하면 안 된다");
		assertBoundSearchWord(statement, board, boundSql);
	}

	@ParameterizedTest(name = "{0}: {1}, searchCnd={2}")
	@MethodSource("titleAndBodySearches")
	@DisplayName("제목과 내용 검색에는 해당 검색 조건만 적용한다")
	void titleAndBodySearchKeepTheirOwnCondition(String dbType, String statementId, String searchCnd,
			String column) throws Exception {
		MappedStatement statement = mappedStatement(dbType, statementId);
		BoardVO board = search(searchCnd);
		BoundSql boundSql = statement.getBoundSql(board);
		String sql = normalize(boundSql.getSql());

		assertTrue(sql.contains("AND " + column + " LIKE "), sql);
		assertEquals(1, occurrences(sql, " LIKE "), sql);
		assertBoundSearchWord(statement, board, boundSql);
	}

	@ParameterizedTest(name = "{0}: {1}")
	@MethodSource("mapperStatements")
	@DisplayName("검색 조건을 지정하지 않으면 검색어 조건과 바인딩을 추가하지 않는다")
	void noSearchConditionDoesNotAddSearchPredicate(String dbType, String statementId) throws Exception {
		BoundSql boundSql = mappedStatement(dbType, statementId).getBoundSql(search(null));
		String sql = normalize(boundSql.getSql());

		assertFalse(sql.contains(" LIKE "), sql);
		assertFalse(parameterProperties(boundSql).contains("searchWrd"));
		assertTrue(sql.contains("A.USE_AT = 'Y'"), sql);
		assertTrue(sql.contains("A.BBS_ID = ?"), sql);
	}

	private static Stream<Arguments> mapperStatements() {
		return DB_TYPES.stream().flatMap(dbType -> STATEMENTS.stream()
				.map(statement -> Arguments.of(dbType, statement)));
	}

	private static Stream<Arguments> titleAndBodySearches() {
		return DB_TYPES.stream().flatMap(dbType -> STATEMENTS.stream().flatMap(statement -> Stream.of(
				Arguments.of(dbType, statement, "0", "A.NTT_SJ"),
				Arguments.of(dbType, statement, "1", "A.NTT_CN"))));
	}

	private static MappedStatement mappedStatement(String dbType, String statementId) throws Exception {
		String resource = "egovframework/mapper/let/cop/bbs/EgovBoard_SQL_" + dbType + ".xml";
		Configuration configuration = new Configuration();
		try (InputStream input = Resources.getResourceAsStream(resource)) {
			new XMLMapperBuilder(input, configuration, resource, configuration.getSqlFragments()).parse();
		}
		return configuration.getMappedStatement("BBSManageDAO." + statementId);
	}

	private static BoardVO search(String condition) {
		BoardVO board = new BoardVO();
		board.setBbsId("BBSMSTR_AAAAAAAAAAAA");
		board.setSearchCnd(condition);
		board.setSearchWrd(SEARCH_WORD);
		return board;
	}

	private static void assertBoundSearchWord(MappedStatement statement, BoardVO board, BoundSql boundSql)
			throws Exception {
		List<String> properties = parameterProperties(boundSql);
		assertEquals(1, properties.stream().filter("searchWrd"::equals).count());
		assertFalse(boundSql.getSql().contains(SEARCH_WORD));
		PreparedStatement preparedStatement = mock(PreparedStatement.class);
		statement.getConfiguration().newParameterHandler(statement, board, boundSql).setParameters(preparedStatement);
		verify(preparedStatement).setString(properties.indexOf("searchWrd") + 1, SEARCH_WORD);
	}

	private static List<String> parameterProperties(BoundSql boundSql) {
		return boundSql.getParameterMappings().stream().map(ParameterMapping::getProperty).toList();
	}

	private static String normalize(String sql) {
		return sql.replaceAll("\\s+", " ").trim().toUpperCase(Locale.ROOT);
	}

	private static int occurrences(String text, String token) {
		return (text.length() - text.replace(token, "").length()) / token.length();
	}
}
