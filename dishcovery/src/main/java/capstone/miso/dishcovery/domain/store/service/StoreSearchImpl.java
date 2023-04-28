package capstone.miso.dishcovery.domain.store.service;

import capstone.miso.dishcovery.domain.keyword.QKeyword;
import capstone.miso.dishcovery.domain.store.QStore;
import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.dto.StoreSearchCondition;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.storeimg.QStoreImg;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;


/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/
public class StoreSearchImpl extends QuerydslRepositorySupport implements StoreSearch {
    public StoreSearchImpl() {
        super(Store.class);
    }

    @Override
    public Page<StoreShortDTO> searchAllStoreShort(StoreSearchCondition condition, Pageable pageable) {
        QStore store = QStore.store;
        QKeyword keyword = QKeyword.keyword;
        QStoreImg storeImg = QStoreImg.storeImg;

        BooleanExpression categoryCondition = null;
        BooleanExpression keywordCondition = null;
        BooleanExpression sectorCondition = null;
        if (condition != null) {
            categoryCondition = StringUtils.isNotBlank(condition.category()) ? store.category.contains(condition.category()) : null;
            keywordCondition = StringUtils.isNotBlank(condition.keyword()) ? keyword.keywordKeys.contains(condition.keyword()) : null;
            sectorCondition = StringUtils.isNotBlank(condition.sector()) ? store.sector.containsIgnoreCase(condition.sector()) : null;
        }
        Expression<String> subQuery1 = Expressions.stringTemplate("(SELECT k.keywordKeys FROM Keyword k WHERE k.store.sid = {0} ORDER BY k.kid ASC LIMIT 1)", store.sid);
        Expression<String> subQuery2 = Expressions.stringTemplate("(SELECT si.imageUrl FROM StoreImg si WHERE si.store.sid = {0} ORDER BY si.sid ASC LIMIT 1)", store.sid);
        /* JPA-JPQL의 GROUP_CONCAT 적용 시도 => 실패
        Expression<String> subQuery1 = Expressions.stringTemplate("(SELECT GROUP_CONCAT(DISTINCT k.keywordKeys SEPARATOR ', ') FROM Keyword k WHERE k.store.sid = {0})", store.sid);
        JPAExpressions.select(Expressions.stringTemplate("GROUP_CONCAT(DISTINCT {0} SEPARATOR ', ')", keyword.keywordKeys))
     .from(keyword)
     .where(keyword.store.sid.eq(store.sid))
         */
        JPQLQuery<StoreShortDTO> dtoQuery = from(store)
                .innerJoin(keyword).on(store.sid.eq(keyword.store.sid).and(keywordCondition))
                .leftJoin(storeImg).on(store.sid.eq(storeImg.store.sid))
                .where(categoryCondition.and(sectorCondition))
                .groupBy(store.sid, store.name, store.lat, store.lon, store.category, store.sector)
                .select(Projections.fields(StoreShortDTO.class,
                        store.sid,
                        store.name.as("storeName"),
                        store.lat,
                        store.lon,
                        store.category,
                        store.sector,
                        ExpressionUtils.as(
                                subQuery1,
                                "keyword"
                        ),
                        ExpressionUtils.as(
                                subQuery2,
                                "imageUrl"
                        )
                ));

        List<StoreShortDTO> dtoList = dtoQuery.fetch();
        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }
}
