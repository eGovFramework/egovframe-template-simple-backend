package egovframework.com.security.pass;

import egovframework.com.cmm.annotation.SecurityPass;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class Test2Controller {

    @GetMapping(value = "/test2/get")
    public String getTest(){
        return "Hellow Egov!";
    }


    @SecurityPass(role = "ROLE1")
    @PostMapping(value = "/test2/post")
    public String postTest(){
        return "Hellow Egov!";
    }
}
