package egovframework.let.main.service;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.ai.chat.client.ChatClient;
import java.util.*;
import java.time.Duration;

@Service
public class EgovRAGService {
	private final ChatClient chatClient;
	private final List<DocumentVector> store = new ArrayList<>();
	private static final long RESPONSE_TIMEOUT_SECONDS = 60;
	
	public EgovRAGService(ChatClient.Builder chatBuilder, RestTemplateBuilder restTemplateBuilder) {
		RestTemplate restTemplate = restTemplateBuilder
				.setConnectTimeout(Duration.ofSeconds(RESPONSE_TIMEOUT_SECONDS))
				.setReadTimeout(Duration.ofSeconds(RESPONSE_TIMEOUT_SECONDS))
				.build();
		
		this.chatClient = chatBuilder.build();
		indexDocuments(null);
	}

	public void indexDocuments(List<String> documents) {
		store.clear();
		List<String> docsToIndex = new ArrayList<>();
		if (documents != null) {
			docsToIndex.addAll(documents);
		}
		
		try {
			List<String> externalUrl = new ArrayList<>();
			externalUrl.add(0, "https://raw.githubusercontent.com/eGovFramework/egovframe-template-simple-react/refs/heads/main/README.md");
			externalUrl.add(1, "https://raw.githubusercontent.com/eGovFramework/egovframe-template-simple-backend/refs/heads/main/README.md");
			RestTemplate restTemplate = new RestTemplate();
			for (int i = 0; i < externalUrl.size(); i++) {
				String url = externalUrl.get(i);
				String urlContent = restTemplate.getForObject(url, String.class);
				if (urlContent != null && !urlContent.isBlank()) {
					docsToIndex.add(i, urlContent);
				}
			}
		} catch (Exception e) {
			System.err.println("외부 URL 파일 로드 실패: " + e.getMessage());
		}
		
		for (int i = 0; i < docsToIndex.size(); i++) {
			String doc = docsToIndex.get(i);
			store.add(new DocumentVector("doc-" + i, doc));
		}
	}

	// 모든 문서를 context로 제공하는 방식
	public String answer(String userQuery) {
		try {
			StringBuilder systemBuilder = new StringBuilder();
			systemBuilder.append("앞으로의 모든 답변은 반드시 질문한 언어와 동일한 언어로 해줘. 당신은 제공된 문서를 기반으로 답변하는 챗봇입니다. 다음 문서를 참고하여 질문에 답변하세요. ");
			systemBuilder.append("문서에 답이 없으면 모른다고 하고 환각 증상이 없다고 말하세요. ");
			systemBuilder.append("\n\nDocuments:\n");
			
			for (int i = 0; i < store.size(); i++) {
				String docText = store.get(i).text;
				systemBuilder.append(String.format("[DOC %d]\n%s\n\n", i + 1, docText));
			}
			String systemInstruction = systemBuilder.toString();
			
			String response = chatClient.prompt()
					.system(systemInstruction)
					.user(userQuery + " 앞으로의 모든 답변은 반드시 질문한 언어와 동일한 언어로 해줘.")
					.call()
					.content();
			
			return response;
		} catch (org.springframework.web.client.ResourceAccessException e) {
			System.err.println("AI 응답 타임아웃 (연결): " + e.getMessage());
			return "죄송합니다. AI 서비스 응답 시간이 초과되었습니다. 잠시 후 다시 시도해주세요.";
		} catch (Exception e) {
			System.err.println("AI 응답 처리 중 오류 발생: " + e.getMessage());
			e.printStackTrace();
			return "죄송합니다. 응답을 처리하는 중에 오류가 발생했습니다.";
		}
	}

	private static class DocumentVector {
		final String id;
		final String text;

		DocumentVector(String id, String text) {
			this.id = id;
			this.text = text;
		}
	}
}
