package egovframework.com.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName : EgovWebApplicationInitializer.java
 * @Description : 공통 컴포넌트 3.10 EgovWebApplicationInitializer 참조 작성
 *
 * @author : 윤주호
 * @since  : 2021. 7. 20
 * @version : 1.0
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일              수정자               수정내용
 *  -------------  ------------   ---------------------
 *   2021. 7. 20    윤주호               최초 생성
 * </pre>
 *
 */
@Slf4j
public class EgovWebApplicationInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		log.debug("EgovWebApplicationInitializer START-============================================");

		// -------------------------------------------------------------
		// Spring Root Context 설정
		// -------------------------------------------------------------
		addRootContext(servletContext);

		// -------------------------------------------------------------
		// Spring Servlet Context 설정
		// -------------------------------------------------------------
		addWebServletContext(servletContext);

		// -------------------------------------------------------------
		// Egov Web ServletContextListener 설정 - System property setting
		// -------------------------------------------------------------
		servletContext.addListener(new egovframework.com.config.EgovWebServletContextListener());

		// -------------------------------------------------------------
		// 필터설정
		// -------------------------------------------------------------
		addFilters(servletContext);

		log.debug("EgovWebApplicationInitializer END-============================================");
	}

	/**
	 * @param servletContext
	 * Root Context를 등록한다.
	 */
	private void addRootContext(ServletContext servletContext) {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(EgovConfigApp.class);

		servletContext.addListener(new ContextLoaderListener(rootContext));
	}

	/**
	 * @param servletContext
	 * Servlet Context를 등록한다.
	 */

	private void addWebServletContext(ServletContext servletContext) {
	AnnotationConfigWebApplicationContext webApplicationContext = new
	AnnotationConfigWebApplicationContext();
	webApplicationContext.register(EgovConfigWebDispatcherServlet.class);
	
	ServletRegistration.Dynamic dispatcher = servletContext.addServlet("action",
	new DispatcherServlet(webApplicationContext));
	dispatcher.setLoadOnStartup(1);
	
	dispatcher.addMapping("*.do"); }

	/**
	 * @param servletContext
	 * 필터들을 등록 한다.
	 */
	private void addFilters(ServletContext servletContext) {
		addEncodingFilter(servletContext);
//		addCORSFilter(servletContext);
	}

	/**
	 * @param servletContext
	 * Spring CharacterEncodingFilter 설정
	 */
	private void addEncodingFilter(ServletContext servletContext) {
		FilterRegistration.Dynamic characterEncoding = servletContext.addFilter("encodingFilter",
			new org.springframework.web.filter.CharacterEncodingFilter());
		characterEncoding.setInitParameter("encoding", "UTF-8");
		characterEncoding.setInitParameter("forceEncoding", "true");
		characterEncoding.addMappingForUrlPatterns(null, false, "*.do");
	}

	/**
	 * @param servletContext
	 * CORSFilter 설정
	 */
//	private void addCORSFilter(ServletContext servletContext) {
//		FilterRegistration.Dynamic corsFilter = servletContext.addFilter("SimpleCORSFilter",
//			new SimpleCORSFilter());
//		corsFilter.addMappingForUrlPatterns(null, false, "*.do");
//	}

}
