package capstonedishcovery.data.domain.member;

/**
 * author        : duckbill413
 * date          : 2023-03-29
 * description   :
 **/
import capstonedishcovery.data.domain.BaseEntity;
import capstonedishcovery.data.domain.preference.Preference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mid;
    private String email;
    private String password;
    private String nickname;
    private boolean alarm;
    @OneToMany(mappedBy = "member")
    private List<Preference> preferences = new ArrayList<>();
}
