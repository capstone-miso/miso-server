package capstone.miso.dishcovery.application.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author        : duckbill413
 * date          : 2023-05-05
 * description   :
 **/
@Log4j2
@RestController
@RequestMapping("/keyword")
@RequiredArgsConstructor
@Tag(name = "키워드", description = "키워드 관련 Controller")
public class KeywordController {
}
