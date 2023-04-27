package capstone.miso.dishcovery.domain.keyword.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author        : duckbill413
 * date          : 2023-04-13
 * description   :
 **/
@RestController
@RequestMapping("/keyword")
@Tag(name = "구수핟넹")
public class KeywordController {
    @GetMapping("")
    public String check(){
        return "작동 중이야~~~";
    }
}
