package capstone.miso.dishcovery.domain.preference.repository;


import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.preference.Preference;
import capstone.miso.dishcovery.domain.store.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * author        : duckbill413
 * date          : 2023-04-03
 * description   :
 **/

public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    List<Preference> findByMember(Member member);

    @Query(value = "SELECT new capstone.miso.dishcovery.domain.preference.repository.PreferenceDAO(p.pid, p.member, p.store) " +
            "FROM Preference p " +
            "WHERE p.member = :member " +
            "GROUP BY p.store")
    Page<PreferenceDAO> findMyPreferenceStores(@Param("member") Member member, Pageable pageable);

    Optional<Preference> findByMemberAndStore(Member member, Store store);

    @Query("SELECT p.store.sid FROM Preference p WHERE p.pid = :pid")
    Long findStoreIdByPreferenceKey(@Param("pid") Long pid);

    @Query("SELECT p.store.sid " +
            "FROM Preference p " +
            "WHERE p.member NOT LIKE :member " +
            "GROUP BY p.store " +
            "order by count(p.store.sid) DESC," +
            "p.updatedAt DESC ")
    Page<Long> findFamousStores(@Param("member") Member member, Pageable pageable);

    @Query("SELECT EXISTS " +
            "(SELECT p.store.sid " +
            "FROM Preference p " +
            "WHERE p.member = :member and p.store.sid = :sid " +
            "GROUP BY p.store)")
    boolean checkMyStorePreference(@Param("member") Member member, @Param("sid") Long sid);

    @Query("select count(p.store.sid) from Preference p where p.store.sid = :storeId")
    long countByStoreId(@Param("storeId") Long storeId);
    @Query(value = """
            select s.store_id
            from (select sk.store_id, p2.pid
                  from store_keyword sk
                           left join preference p2 on sk.store_id = p2.store_id
                           left join store s2 on s2.sid = sk.store_id
                  where sk.keyword in (select tmp.keyword
                                       from (select ssk.keyword
                                             from store_keyword ssk
                                             where ssk.store_id in (select p.store_id
                                                                    from preference p
                                                                    where p.member_id = :member
                                                                    group by p.store_id)
                                             group by ssk.keyword
                                             order by ssk.updated_at desc
                                             limit 3) as tmp)
                    and s2.category IN (select sc.category
                                        from (select s.category
                                              from preference p
                                                       left join store s on s.sid = p.store_id
                                              where p.member_id = :member
                                              order by p.updated_at desc
                                              limit 10) as sc)
                  group by sk.store_id, p2.pid) as s
            group by s.store_id
            order by count(s.pid) desc
            """, countQuery = """
            select count(r.store_id)
            from (select s.store_id
                  from (select sk.store_id, p2.pid
                        from store_keyword sk
                                 left join preference p2 on sk.store_id = p2.store_id
                                 left join store s2 on s2.sid = sk.store_id
                        where sk.keyword in (select tmp.keyword
                                             from (select ssk.keyword
                                                   from store_keyword ssk
                                                   where ssk.store_id in (select p.store_id
                                                                          from preference p
                                                                          where p.member_id = :member
                                                                          group by p.store_id)
                                                   group by ssk.keyword
                                                   order by ssk.updated_at desc
                                                   limit 3) as tmp)
                          and s2.category IN (select sc.category
                                              from (select s.category
                                                    from preference p
                                                             left join store s on s.sid = p.store_id
                                                    where p.member_id = :member
                                                    order by p.updated_at desc
                                                    limit 10) as sc)
                        group by sk.store_id, p2.pid) as s
                  group by s.store_id) r
            """,
            nativeQuery = true)
    Page<Long> findStoreInMyInterest(@Param("member") String member, Pageable pageable);
}
