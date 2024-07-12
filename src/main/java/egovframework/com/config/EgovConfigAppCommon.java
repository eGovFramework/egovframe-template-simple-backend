package egovframework.com.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import egovframework.com.cmm.EgovComTraceHandler;
import egovframework.com.cmm.ImagePaginationRenderer;
import egovframework.com.cmm.web.EgovMultipartResolver;

import org.egovframe.rte.fdl.cmmn.trace.LeaveaTrace;
import org.egovframe.rte.fdl.cmmn.trace.handler.TraceHandler;
import org.egovframe.rte.fdl.cmmn.trace.manager.DefaultTraceHandleManager;
import org.egovframe.rte.fdl.cmmn.trace.manager.TraceHandlerService;
import org.egovframe.rte.fdl.cryptography.EgovPasswordEncoder;
import org.egovframe.rte.fdl.cryptography.impl.EgovARIACryptoServiceImpl;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.DefaultPaginationManager;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationRenderer;

/**
 * @ClassName : EgovConfigAppCommon.java
 * @Description : 공통 Bean 설정
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
 *   2023. 5. 05    crlee              remove EgovMessageSource config
 * </pre>
 *
 */
@Configuration
@ComponentScan(basePackages = "egovframework", includeFilters = {
	@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Service.class),
	@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Repository.class)
}, excludeFilters = {
	@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
	@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class)
})
public class EgovConfigAppCommon {

	/**
	 * @return AntPathMatcher 등록.  Ant 경로 패턴 경로와 일치하는지 여부를 확인
	 */
	@Bean
	public AntPathMatcher antPathMatcher() {
		return new AntPathMatcher();
	}


	/**
	 * @return [LeaveaTrace 설정] defaultTraceHandler 등록
	 */
	@Bean
	public EgovComTraceHandler defaultTraceHandler() {
		return new EgovComTraceHandler();
	}

	/**
	 * @return [LeaveaTrace 설정] traceHandlerService 등록. TraceHandler 설정
	 */
	@Bean
	public DefaultTraceHandleManager traceHandlerService() {
		DefaultTraceHandleManager defaultTraceHandleManager = new DefaultTraceHandleManager();
		defaultTraceHandleManager.setReqExpMatcher(antPathMatcher());
		defaultTraceHandleManager.setPatterns(new String[] {"*"});
		defaultTraceHandleManager.setHandlers(new TraceHandler[] {defaultTraceHandler()});
		return defaultTraceHandleManager;
	}

	/**
	 * @return [LeaveaTrace 설정] LeaveaTrace 등록
	 */
	@Bean
	public LeaveaTrace leaveaTrace() {
		LeaveaTrace leaveaTrace = new LeaveaTrace();
		leaveaTrace.setTraceHandlerServices(new TraceHandlerService[] {traceHandlerService()});
		return leaveaTrace;
	}

	/**
	 * @return [ImagePaginationRenderer 설정] ImagePaginationRenderer 등록
	 */
	@Bean
	public ImagePaginationRenderer imageRenderer() {
		return new ImagePaginationRenderer();
	}

	/**
	 * @return [ImagePaginationRenderer 설정] defaultPaginationManager 설정.
	 */
	@Bean
	public DefaultPaginationManager paginationManager() {
		DefaultPaginationManager defaultPaginationManager = new DefaultPaginationManager();

		Map<String, PaginationRenderer> rendererType = new HashMap<>();
		rendererType.put("image", imageRenderer());
		defaultPaginationManager.setRendererType(rendererType);

		return defaultPaginationManager;
	}

	/**
	 * @return [MultipartResolver 설정] CommonsMultipartResolver 등록
	 */
	@Bean
	public CommonsMultipartResolver springRegularCommonsMultipartResolver() {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setMaxUploadSize(100000000);
		commonsMultipartResolver.setMaxInMemorySize(100000000);
		commonsMultipartResolver.setSupportedMethods("POST","PUT");
		return commonsMultipartResolver;
	}

	/**
	 * 확장자 제한 : globals.properties > Globals.fileUpload.Extensions로 설정
	 * @return [MultipartResolver 설정] EgovMultipartResolver 등록
	 */
	@Bean
	public EgovMultipartResolver localMultiCommonsMultipartResolver() {
		EgovMultipartResolver egovMultipartResolver = new EgovMultipartResolver();
		egovMultipartResolver.setMaxUploadSize(100000000);
		egovMultipartResolver.setMaxInMemorySize(100000000);
		egovMultipartResolver.setSupportedMethods("POST","PUT");
		return egovMultipartResolver;
	}
	
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		return localMultiCommonsMultipartResolver();
	}
	
	/**
	 * 암복호화
	 * @return [EgovPasswordEncoder 설정] EgovPasswordEncoder 등록
	 */
	@Bean
	public EgovPasswordEncoder egovPasswordEncoder() {
		EgovPasswordEncoder egovPasswordEncoder = new EgovPasswordEncoder();
		egovPasswordEncoder.setAlgorithm("SHA-256");
		egovPasswordEncoder.setHashedPassword("gdyYs/IZqY86VcWhT8emCYfqY1ahw2vtLG+/FzNqtrQ=");
		return egovPasswordEncoder;
	}
	
	/**
	 * 암복호화
	 * @return [EgovARIACryptoServiceImpl 설정] EgovARIACryptoServiceImpl 등록
	 */
	@Bean
	public EgovARIACryptoServiceImpl egovARIACryptoService() {
		EgovARIACryptoServiceImpl egovARIACryptoServiceImpl = new EgovARIACryptoServiceImpl();
		egovARIACryptoServiceImpl.setPasswordEncoder(egovPasswordEncoder());
		egovARIACryptoServiceImpl.setBlockSize(1024);
		return egovARIACryptoServiceImpl;
	}
}
