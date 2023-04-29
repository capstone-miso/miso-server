package capstone.miso.dishcovery.domain.store.service;

import capstone.miso.dishcovery.domain.keyword.QKeyword;
import capstone.miso.dishcovery.domain.store.QStore;
import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.dto.StoreSearchCondition;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.storeimg.QStoreImg;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


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
        Expression<String> subQuery = Expressions.stringTemplate("(SELECT si.imageUrl FROM StoreImg si WHERE si.store.sid = {0} ORDER BY si.sid ASC LIMIT 1)", store.sid);

        JPQLQuery<StoreShortDTO> dtoQuery = from(store)
                .innerJoin(keyword).on(store.sid.eq(keyword.store.sid).and(keywordCondition))
                .leftJoin(storeImg).on(store.sid.eq(storeImg.store.sid))
                .where(categoryCondition)
                .where(keywordCondition)
                .where(sectorCondition)
                .groupBy(store.sid, store.name, store.lat, store.lon, store.category, store.sector)
                .select(Projections.fields(StoreShortDTO.class,
                        store.sid,
                        store.name.as("storeName"),
                        store.lat,
                        store.lon,
                        store.category,
                        store.sector,
                        ExpressionUtils.as(subQuery, "imageUrl")
                ));
        // MEMO: Order 조건 추가 가능
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable, store);
        dtoQuery.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));

        List<StoreShortDTO> dtoList = dtoQuery.fetch();
        dtoList.forEach(this::addKeywordList);
        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable, QStore store) {
        List<OrderSpecifier<?>> orderSpecifiers = pageable.getSort().stream()
                .map(order -> {
                    if (order.getProperty().equalsIgnoreCase("sid")) {
                        return order.isDescending() ? store.sid.desc() : store.sid.asc();
                    } else if (order.getProperty().equalsIgnoreCase("storeName")) {
                        return order.isDescending() ? store.name.desc() : store.name.asc();
                    } else if (order.getProperty().equalsIgnoreCase("lat")) {
                        return order.isDescending() ? store.lat.desc() : store.lat.asc();
                    } else if (order.getProperty().equalsIgnoreCase("lon")) {
                        return order.isDescending() ? store.lon.desc() : store.lon.asc();
                    } else if (order.getProperty().equalsIgnoreCase("category")) {
                        return order.isDescending() ? store.category.desc() : store.category.asc();
                    } else if (order.getProperty().equalsIgnoreCase("sector")) {
                        return order.isDescending() ? store.sector.desc() : store.sector.asc();
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return orderSpecifiers;
    }

    private void addKeywordList(StoreShortDTO storeShortDTO) {
        QKeyword keyword = QKeyword.keyword;
        JPQLQuery<String> dtoQuery = from(keyword)
                .select(keyword.keywordKeys)
                .where(keyword.store.sid.eq(storeShortDTO.getSid()))
                .groupBy(keyword.keywordKeys);

        storeShortDTO.setKeywords(dtoQuery.fetch());
    }
}
