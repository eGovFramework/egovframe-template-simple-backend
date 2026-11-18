package egovframework.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 매퍼는 MyBatis 파라미터 표기만 쓴다.
 *
 * iBATIS 2 표기 {@code #name:TYPE#}는 MyBatis에서 파라미터로 해석되지 않고
 * 문장에 그대로 남는다.
 *
 * @author 최완택
 * @since 2026-08-30
 */
@DisplayName("매퍼 파라미터 표기")
class EgovMapperParameterSyntaxTest {

	private static final Pattern LEGACY_PARAMETER = Pattern.compile("#[A-Za-z_][A-Za-z0-9_]*(?::[A-Z]+)?#");

	@Test
	@DisplayName("iBATIS 표기를 쓰지 않는다")
	void mappersUseMyBatisParameterSyntax() throws IOException {
		List<String> found = new ArrayList<>();

		try (Stream<Path> paths = Files.walk(Paths.get("src/main/resources/egovframework/mapper"))) {
			for (Path path : paths.filter(Files::isRegularFile).collect(Collectors.toList())) {
				if (!path.getFileName().toString().endsWith(".xml")) {
					continue;
				}
				Matcher matcher = LEGACY_PARAMETER.matcher(new String(Files.readAllBytes(path), StandardCharsets.UTF_8));
				while (matcher.find()) {
					found.add(path.getFileName() + " " + matcher.group());
				}
			}
		}

		assertTrue(found.isEmpty(), "iBATIS 표기: " + found);
	}

}
