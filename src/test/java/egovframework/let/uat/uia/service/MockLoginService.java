package egovframework.let.uat.uia.service;

import egovframework.com.cmm.LoginVO;
import egovframework.let.uat.uia.service.EgovLoginService;
import egovframework.let.utl.sim.service.EgovFileScrty;

/**
 * fileName       : MockLoginService
 * author         : crlee
 * date           : 2023/05/06
 * description    : Login Service 목
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/05/06        crlee       최초 생성
 */
public class MockLoginService implements EgovLoginService {

    @Override
    public LoginVO actionLogin(LoginVO vo) throws Exception {
        if( vo.getPassword().equals("badPwd")){
            return null;
        }
        String enpassword = EgovFileScrty.encryptPassword(vo.getPassword(), vo.getId());
        vo.setPassword(enpassword);
        return vo;
    }

    @Override
    public LoginVO searchId(LoginVO vo) throws Exception {
        return vo;
    }

    @Override
    public boolean searchPassword(LoginVO vo) throws Exception {
        return true;
    }
}