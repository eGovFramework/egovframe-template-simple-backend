package egovframework.com.cmm;

import java.io.IOException;
import java.io.Reader;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Cross-Site Scripting 체크하여 값을 되돌려 받는 핸들러 JSP TLD, 자바에서 사용가능
 *
 * @author 공통서비스 장동한
 * @since 2010.11.09
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2010.11.09  장동한          최초 생성
 *
 * </pre>
 */
public class EgovComCrossSiteHndlr extends BodyTagSupport {
	
	public static final int HIGHEST_SPECIAL = '>';
	public static char[][] specialCharactersRepresentation = new char[HIGHEST_SPECIAL + 1][];
	static {
	    specialCharactersRepresentation['&'] = "&amp;".toCharArray();
	    specialCharactersRepresentation['<'] = "&lt;".toCharArray();
	    specialCharactersRepresentation['>'] = "&gt;".toCharArray();
	    specialCharactersRepresentation['"'] = "&#034;".toCharArray();
	    specialCharactersRepresentation['\''] = "&#039;".toCharArray();
	}

	/*
	 * (One almost wishes XML and JSP could support "anonymous tags," given the
	 * amount of trouble we had naming this one!) :-) - sb
	 */

	// *********************************************************************
	// Internal state

	private static final long serialVersionUID = -6750233818675360686L;
	protected Object value; // tag attribute
	protected String def; // tag attribute
	protected boolean escapeXml; // tag attribute
	private boolean needBody; // non-space body needed?

	// *********************************************************************
	// Construction and initialization

	private String m_sDiffChar ="()[]{}\"',:;= \t\r\n%!+-";
	//private String m_sDiffChar ="()[]{}\"',:;=%!+-";
	private String m_sArrDiffChar [] = {
						"&#40;","&#41;",
						"&#91;","&#93;",
						"&#123;","&#125;",
						"&#34;","&#39;",
						"&#44;","&#58;",
						"&#59;","&#61;",
						" ","\t", //" ","\t",
						"\r","\n", //"\r","\n",
						"&#37;","&#33;",
						"&#43;","&#45;"
						};

	/**
	 * Constructs a new handler. As with TagSupport, subclasses should not
	 * provide other constructors and are expected to call the superclass
	 * constructor.
	 */
	public EgovComCrossSiteHndlr() {
		super();
		init();
	}

	// resets local state
	private void init() {
		value = def = null;
		escapeXml = true;
		needBody = false;
	}

	// Releases any resources we may have (or inherit)
	@Override
	public void release() {
		super.release();
		init();
	}

	// *********************************************************************
	// Tag logic

	// evaluates 'value' and determines if the body should be evaluted
	@Override
	public int doStartTag() throws JspException {

		needBody = false; // reset state related to 'default'
		this.bodyContent = null; // clean-up body (just in case container is
									// pooling tag handlers)

		JspWriter out = pageContext.getOut();
		//log.debug("EgovComCrossSiteFilter> ============================");
		try {
			// print value if available; otherwise, try 'default'
			if (value != null) {
				//log.debug("EgovComCrossSiteFilter> =value");
				String sWriteEscapedXml = getWriteEscapedXml();
				//log.debug("EgovComCrossSiteFilter sWriteEscapedXml>" + sWriteEscapedXml);
				out.print(sWriteEscapedXml);
				return SKIP_BODY;
			} else {
				// if we don't have a 'default' attribute, just go to the body
				if (def == null) {
					needBody = true;
					return EVAL_BODY_BUFFERED;
				}

				//log.debug("EgovComCrossSiteFilter def> ="+def);

				// if we do have 'default', print it
				else {
					// good 'default'
					out(pageContext, escapeXml, def);
				//log.debug("EgovComCrossSiteFilter> ="+def);
				}
				return SKIP_BODY;
			}
		} catch (IOException ex) {
			throw new JspException(ex.toString(), ex);
		}
	}

	// prints the body if necessary; reports errors
	@Override
	public int doEndTag() throws JspException {
		try {
			//log.debug("EgovComCrossSiteFilter ==== doEndTag");
			if (!needBody){
				return EVAL_PAGE; // nothing more to do
			}

			// trim and print out the body
			if (bodyContent != null && bodyContent.getString() != null){
				//String sWriteEscapedXml = getWriteEscapedXml();
				//out2(pageContext, escapeXml, sWriteEscapedXml.toString());
				//log.debug("EgovComCrossSiteFilter> end");
				//log.debug("EgovComCrossSiteFilter sWriteEscapedXml > sWriteEscapedXml");
				out(pageContext, escapeXml, bodyContent.getString().trim());

			}
			return EVAL_PAGE;
		} catch (IOException ex) {
			throw new JspException(ex.toString(), ex);
		}
	}

	// *********************************************************************
	// Public utility methods

	/**
	 * Outputs <tt>text</tt> to <tt>pageContext</tt>'s current JspWriter. If
	 * <tt>escapeXml</tt> is true, performs the following substring replacements
	 * (to facilitate output to XML/HTML pages):
	 *
	 * & -> &amp; < -> &lt; > -> &gt; " -> &#034; ' -> &#039;
	 *
	 * See also Util.escapeXml().
	 */
	public static void out(PageContext pageContext, boolean escapeXml,
			Object obj) throws IOException {
		JspWriter w = pageContext.getOut();

		if (!escapeXml) {
			// write chars as is
			if (obj instanceof Reader) {
				Reader reader = (Reader) obj;
				char[] buf = new char[4096];
				int count;
				while ((count = reader.read(buf, 0, 4096)) != -1) {
					w.write(buf, 0, count);
				}
			} else {
				w.write(obj.toString());
			}
		} else {
			// escape XML chars
			if (obj instanceof Reader) {
				Reader reader = (Reader) obj;
				char[] buf = new char[4096];
				int count;
				while ((count = reader.read(buf, 0, 4096)) != -1) {
					writeEscapedXml(buf, count, w);
				}
			} else {
				String text = obj.toString();
				writeEscapedXml(text.toCharArray(), text.length(), w);
			}
		}

	}
	public static void out2(PageContext pageContext, boolean escapeXml,
			Object obj) throws IOException {
		JspWriter w = pageContext.getOut();

		w.write(obj.toString());

	}

	/**
	 *
	 * Optimized to create no extra objects and write directly to the JspWriter
	 * using blocks of escaped and unescaped characters
	 *
	 */
	private static void writeEscapedXml(char[] buffer, int length, JspWriter w)
			throws IOException {
		int start = 0;

		for (int i = 0; i < length; i++) {
			char c = buffer[i];
			if (c <= HIGHEST_SPECIAL) {
				char[] escaped = specialCharactersRepresentation[c];
				if (escaped != null) {
					// add unescaped portion
					if (start < i) {
						w.write(buffer, start, i - start);
					}
					// add escaped xml
					w.write(escaped);
					start = i + 1;
				}
			}
		}
		// add rest of unescaped portion
		if (start < length) {
			w.write(buffer, start, length - start);
		}
	}

	/**
	 *
	 * Optimized to create no extra objects and write directly to the JspWriter
	 * using blocks of escaped and unescaped characters
	 *
	 */
	@SuppressWarnings("unused")
	private String getWriteEscapedXml() throws IOException {
		String sRtn = "";

		Object obj = this.value;

		int start = 0;
		String text = obj.toString();

		int length = text.length();
		char[] buffer = text.toCharArray();
		boolean booleanDiff = false;
		//String sDiffChar
		//String sArrDiffChar
		char[] cDiffChar =  this.m_sDiffChar.toCharArray();

		for(int i = 0; i < length; i++) {
			char c = buffer[i];

			booleanDiff = false;

			for(int k = 0; k < cDiffChar.length; k++){
				if(c == cDiffChar[k]){
					sRtn = sRtn + m_sArrDiffChar[k];
					booleanDiff = true;
					continue;
				}
			}

			if(booleanDiff) continue;

			if (c <= HIGHEST_SPECIAL) {
				char[] escaped = specialCharactersRepresentation[c];
				if (escaped != null) {
					// add unescaped portion
					//if (start < i) {
					//	sRtn = sRtn + text.substring(start, i - start);
					//}
					// add escaped xml
					//sRtn = sRtn + escaped;
					//log.debug(buffer[i]+" :: " + escaped);
					for (int j = 0; j < escaped.length; j++) {
						//log.debug(buffer[i]+" :>: " + escaped[j]);
						sRtn = sRtn + escaped[j];
					}
					//sRtn = sRtn+ escaped.toString();
					//sRtn = sRtn + String.valueOf(buffer[i]);
					start = i + 1;
				}else{
					sRtn = sRtn + c;
				}
			}else{
				sRtn = sRtn + c;
			}
		}

		return sRtn;
	}

	/**
	 *
	 * Optimized to create no extra objects and write directly to the JspWriter
	 * using blocks of escaped and unescaped characters
	 *
	 */
	@SuppressWarnings("unused")
	private String getWriteEscapedXml(String sWriteString) throws IOException {

		String sRtn = "";

		Object obj = sWriteString;

		int start = 0;
		String text = obj.toString();

		int length = text.length();
		char[] buffer = text.toCharArray();
		boolean booleanDiff = false;
		//String sDiffChar
		//String sArrDiffChar
		char[] cDiffChar =  this.m_sDiffChar.toCharArray();

		for(int i = 0; i < length; i++) {
			char c = buffer[i];

			booleanDiff = false;

			for(int k = 0; k < cDiffChar.length; k++){
				if(c == cDiffChar[k]){
					sRtn = sRtn + m_sArrDiffChar[k];
					booleanDiff = true;
					continue;
				}
			}

			if(booleanDiff) continue;

			if (c <= HIGHEST_SPECIAL) {
				char[] escaped = specialCharactersRepresentation[c];
				if (escaped != null) {
					// add unescaped portion
					//if (start < i) {
					//	sRtn = sRtn + text.substring(start, i - start);
					//}
					// add escaped xml
					//sRtn = sRtn + escaped;
					//log.debug(buffer[i]+" :: " + escaped);
					for (int j = 0; j < escaped.length; j++) {
						//log.debug(buffer[i]+" :>: " + escaped[j]);
						sRtn = sRtn + escaped[j];
					}
					//sRtn = sRtn+ escaped.toString();
					//sRtn = sRtn + String.valueOf(buffer[i]);
					start = i + 1;
				}else{
					sRtn = sRtn + c;
				}
			}else{
				sRtn = sRtn + c;
			}
		}

		return sRtn;
	}

    // for tag attribute
    public void setValue(Object value) {
        this.value = value;
    }

    // for tag attribute
    public void setDefault(String def) {
        this.def = def;
    }

    // for tag attribute
    public void setEscapeXml(boolean escapeXml) {
        this.escapeXml = escapeXml;
    }
    /*
    public static void main(String[] args) throws IOException
    {

    	EgovComCrossSiteHndlr egovComCrossSiteHndlr = new EgovComCrossSiteHndlr();

    	egovComCrossSiteHndlr.value = "TRNSMIT";

    	String sCrossSiteHndlr = egovComCrossSiteHndlr.getWriteEscapedXml();
    	//log.debug("writeEscapedXml " + egovComCrossSiteHndlr.getWriteEscapedXml());

    	log.debug("sCrossSiteHndlr|"+ sCrossSiteHndlr + "|");

    	try{
    		log.debug("TRY TEST 1");
    		throw new Exception();
    	}catch(Exception e){
    		log.debug("TRY TEST 2");
    	}finally{
    		log.debug("TRY TEST 3");

    	}

    }
    */
 }


