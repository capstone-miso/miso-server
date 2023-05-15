package capstone.miso.dishcovery.domain.preference.repository;

import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.store.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class PreferenceDAO {
    private Long pid;
    private Member member;
    private Store store;
    public PreferenceDAO(Long pid, Member member, Store store) {
        this.pid = pid;
        this.member = member;
        this.store = store;
    }
}
