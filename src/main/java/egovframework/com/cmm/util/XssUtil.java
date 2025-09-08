package egovframework.com.cmm.util;

import org.springframework.stereotype.Component;

/**
 * XSS(크로스 사이트 스크립팅) 방지를 위한 유틸리티 클래스
 *
 * <p>
 * 게시물 입력 값에서 스크립트, 오브젝트, 애플릿, 임베드, 폼 태그 등을
 * HTML 이스케이프 처리하여 보안 취약점을 예방한다.
 * </p>
 *
 * @author 김재섭(nirsa)
 * @since 2025.09.03
 * @version 1.0
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일        수정자             수정내용
 *  ----------    ----------        ---------------------------
 *  2025.09.03    김재섭(nirsa)      구조 개선하며 unscript 메서드를 해당 클래스로 이동
 * </pre>
 */
@Component
public class XssUtil {
    /**
     * XSS 방지 처리.
     *
     * @param data
     * @return
     */
    public String unscript(String data) {
        if (data == null || data.trim().equals("")) {
            return "";
        }

        String ret = data;

        ret = ret.replaceAll("<(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;script");
        ret = ret.replaceAll("</(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;/script");

        ret = ret.replaceAll("<(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;object");
        ret = ret.replaceAll("</(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;/object");

        ret = ret.replaceAll("<(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;applet");
        ret = ret.replaceAll("</(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;/applet");

        ret = ret.replaceAll("<(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");
        ret = ret.replaceAll("</(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");

        ret = ret.replaceAll("<(F|f)(O|o)(R|r)(M|m)", "&lt;form");
        ret = ret.replaceAll("</(F|f)(O|o)(R|r)(M|m)", "&lt;form");

        return ret;
    }
}
