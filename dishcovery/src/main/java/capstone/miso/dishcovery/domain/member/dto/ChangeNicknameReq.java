package capstone.miso.dishcovery.domain.member.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * author        : duckbill413
 * date          : 2023-05-05
 * description   :
 **/

public record ChangeNicknameReq(@NotBlank(message = "Nickname이 Null 또는 공백이여서는 안됩니다.") String nickname) {
}
