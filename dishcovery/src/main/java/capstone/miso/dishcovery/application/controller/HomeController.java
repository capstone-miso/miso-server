package capstone.miso.dishcovery.application.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * author        : duckbill413
 * date          : 2023-05-04
 * description   :
 **/
@Log4j2
@Controller
@RequiredArgsConstructor
public class HomeController {
    @RequestMapping(value = "/", method = {RequestMethod.POST, RequestMethod.GET})
    public String redirectToLogin(){
        return "redirect:/login";
    }

    /**
     * author        : duckbill413
     * date          : 2023-04-13
     * description   :
     **/
    @RestController
    @RequestMapping("/keyword")
    @Tag(name = "키워드")
    public static class KeywordController {
    }
}
