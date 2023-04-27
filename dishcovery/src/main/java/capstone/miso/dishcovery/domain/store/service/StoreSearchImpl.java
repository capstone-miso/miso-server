package capstone.miso.dishcovery.domain.store.service;

import capstone.miso.dishcovery.domain.keyword.Keyword;
import capstone.miso.dishcovery.domain.keyword.QKeyword;
import capstone.miso.dishcovery.domain.store.QStore;
import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.dto.StoreSearchCondition;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.storeimg.QStoreImg;
import capstone.miso.dishcovery.domain.storeimg.StoreImg;
import capstone.miso.dishcovery.domain.storeimg.dto.StoreImgDTO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.catalina.LifecycleState;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.security.Key;
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
        JPQLQuery<Store> query = from(store);
        query.leftJoin(keyword).on(keywordCondition);
        query.leftJoin(storeImg);
        query.where(categoryCondition);

        List<Keyword> kq = from(keyword).on(keywordCondition).fetch();
        List<StoreImg> sq = from(storeImg).groupBy(storeImg.store).fetch();

        JPQLQuery<StoreShortDTO> dtoQuery = query.select(Projections.bean(StoreShortDTO.class,
                store.sid,
                store.name,
                store.lat,
                store.lon,
                store.category,
                keyword.keywordKeys,
                store.sector,
                storeImg.imageUrl));

        this.getQuerydsl().applyPagination(pageable, dtoQuery);

        List<StoreShortDTO> dtoList = dtoQuery.fetch();
        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }
}
