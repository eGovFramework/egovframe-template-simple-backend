package egovframework.com.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import egovframework.com.cmm.service.EgovProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author : 정완배
 * @since : 2023. 8. 9.
 * @version : 1.0
 *
 * @package : egovframework.com.config
 * @filename : EgovWebServletContextListener.java
 * @modificationInformation
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일            수정자             수정내용
 *  ----------   ----------   ----------------------
 *  2023. 8. 9.    정완배              주석추가
 * </pre>
 *
 *
 */
@Slf4j
public class EgovWebServletContextListener implements ServletContextListener {

	public EgovWebServletContextListener() {
		setEgovProfileSetting();
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		if (System.getProperty("spring.profiles.active") == null) {
			setEgovProfileSetting();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		if (System.getProperty("spring.profiles.active") != null) {
			System.clearProperty("spring.profiles.active");
		}
	}

	public void setEgovProfileSetting() {
		try {
			log.debug("===========================Start EgovServletContextLoad START ===========");
			System.setProperty("spring.profiles.active",
					EgovProperties.getProperty("Globals.DbType") + "," + EgovProperties.getProperty("Globals.Auth"));
			log.debug("Setting spring.profiles.active>" + System.getProperty("spring.profiles.active"));
			log.debug("===========================END   EgovServletContextLoad END ===========");
		} catch (IllegalArgumentException e) {
			log.error("[IllegalArgumentException] Try/Catch...usingParameters Runing : " + e.getMessage());
		}
	}
}
