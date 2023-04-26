package capstone.miso.dishcovery.security.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * author        : duckbill413
 * date          : 2023-04-26
 * description   :
 **/
@Getter
public class MemberSecurityDTO extends User implements OAuth2User {
    private final String email;
    private final String password;
    private String nickname;
    private Map<String, Object> props;
    public MemberSecurityDTO(String username, String password,
                             String nickname,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email = username;
        this.password = password;
        this.nickname = nickname;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.getProps();
    }

    public void setProps(Map<String, Object> props){
        this.props = props;
    }

    @Override
    public String getName() {
        return this.email;
    }
}
