package egovframework.com.config;

import org.apache.commons.text.StringEscapeUtils;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;

public class HtmlCharacterEscapes extends CharacterEscapes {
	
	private static final long serialVersionUID = -6353236148390563705L;
	
	private final int[] asciiEscapes;

    public HtmlCharacterEscapes() {
        this.asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
        this.asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
        this.asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
        this.asciiEscapes['\"'] = CharacterEscapes.ESCAPE_CUSTOM;
        this.asciiEscapes['('] = CharacterEscapes.ESCAPE_CUSTOM;
        this.asciiEscapes[')'] = CharacterEscapes.ESCAPE_CUSTOM;
        this.asciiEscapes['#'] = CharacterEscapes.ESCAPE_CUSTOM;
        this.asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;
    }

    @Override
    public int[] getEscapeCodesForAscii() {
    	// 26.03.04 KISA 보안취약점 조치
    	// private 배열의 복제본 반환 처리
        return asciiEscapes.clone();
    }

    @Override
    public SerializableString getEscapeSequence(int ch) {
        return new SerializedString(StringEscapeUtils.escapeHtml4(Character.toString((char) ch)));
    }
}
