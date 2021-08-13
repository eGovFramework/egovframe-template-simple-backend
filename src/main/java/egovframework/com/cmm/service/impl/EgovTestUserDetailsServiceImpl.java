package egovframework.com.cmm.service.impl;

import java.util.ArrayList;
import java.util.List;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.EgovUserDetailsService;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 *
 * @author 공통서비스 개발팀 서준식
 * @since 2011. 8. 12.
 * @version 1.0
 * @see
 *
 * <pre>
 * 개정이력(Modification Information)
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2011. 8. 12.    서준식        최초생성
 *
 *  </pre>
 */

public class EgovTestUserDetailsServiceImpl extends EgovAbstractServiceImpl implements
		EgovUserDetailsService {

	@Override
	public Object getAuthenticatedUser() {

		LoginVO loginVO = new LoginVO();
		loginVO.setId("TEST1");
		loginVO.setPassword("raHLBnHFcunwNzcDcfad4PhD11hHgXSUr7fc1Jk9uoQ=");
		loginVO.setUserSe("USR");
		loginVO.setEmail("egovframe@nia.or.kr");
		loginVO.setIhidNum("");
		loginVO.setName("더미사용자");
		loginVO.setOrgnztId("ORGNZT_0000000000000");
		loginVO.setUniqId("USRCNFRM_00000000000");
		return loginVO;

		// return
		// RequestContextHolder.getRequestAttributes().getAttribute("loginVO",
		// RequestAttributes.SCOPE_SESSION);

	}

	@Override
	public List<String> getAuthorities() {

		// 권한 설정을 리턴한다.

		List<String> listAuth = new ArrayList<String>();
		listAuth.add("IS_AUTHENTICATED_ANONYMOUSLY");
		listAuth.add("IS_AUTHENTICATED_FULLY");
		listAuth.add("IS_AUTHENTICATED_REMEMBERED");
		listAuth.add("ROLE_ADMIN");
		listAuth.add("ROLE_ANONYMOUS");
		listAuth.add("ROLE_RESTRICTED");
		listAuth.add("ROLE_USER");

		return listAuth;
	}

	@Override
	public Boolean isAuthenticated() {
		// 인증된 유저인지 확인한다.

		/*if (RequestContextHolder.getRequestAttributes() == null) {
			return false;
		} else {

			if (RequestContextHolder.getRequestAttributes().getAttribute(
					"loginVO", RequestAttributes.SCOPE_SESSION) == null) {
				return false;
			} else {
				return true;
			}
		}*/

		return true;
	}

}
