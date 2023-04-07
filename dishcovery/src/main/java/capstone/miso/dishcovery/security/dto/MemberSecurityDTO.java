package capstone.miso.dishcovery.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * author        : duckbill413
 * date          : 2023-03-08
 * description   :
 **/
@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User implements OAuth2User {
    private Long mid;
    private String email;
    private String password;
    private String nickname;
    private Map<String, Object> props;
    public MemberSecurityDTO(Long mid, String email, String password,
                             String nickname,
                             Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities);

        this.mid = mid;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.getProps();
    }

    @Override
    public String getName() {
        return this.email;
    }
}
