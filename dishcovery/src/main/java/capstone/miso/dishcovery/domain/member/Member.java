package capstone.miso.dishcovery.domain.member;

/**
 * author        : duckbill413
 * date          : 2023-03-29
 * description   :
 **/

import capstone.miso.dishcovery.domain.BaseEntity;
import capstone.miso.dishcovery.domain.preference.Preference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private String email;
    private String password;
    private String nickname;
    private boolean alarm;
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Set<MemberRole> roleSet = new HashSet<>();
    @OneToMany(mappedBy = "member")
    private List<Preference> preferences = new ArrayList<>();

    public void addRole(MemberRole memberRole) {
        this.roleSet.add(memberRole);
    }
}
