package capstone.miso.dishcovery.domain.store.service;

import capstone.miso.dishcovery.domain.image.QImage;
import capstone.miso.dishcovery.domain.keyword.KeywordSet;
import capstone.miso.dishcovery.domain.keyword.QKeyword;
import capstone.miso.dishcovery.domain.store.QStore;
import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.dto.StoreSearchCondition;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/
@Log4j2
public class StoreSearchImpl extends QuerydslRepositorySupport implements StoreSearch {
    private final double KM = 0.0045d;

    public StoreSearchImpl() {
        super(Store.class);
    }

    @Override
    public Page<StoreShortDTO> searchAllStoreShort(StoreSearchCondition condition, Pageable pageable) {
        QStore store = QStore.store;
        QImage image = QImage.image;
        QKeyword keyword = QKeyword.keyword1;

        Map<String, BooleanExpression> expression = booleanExpressionMap(condition, store, keyword);
        String imageSql = """
                (SELECT i.imageUrl
                FROM Image i
                WHERE i.store.sid = {0}
                    AND (i.photoId = 'M' OR NOT EXISTS (SELECT ei.imageUrl FROM Image ei WHERE ei.store.sid = {0} AND ei.photoId = 'M'))
                ORDER BY i.sid ASC
                LIMIT 1)
                """;
        Expression<String> subQuery = Expressions.stringTemplate(imageSql, store.sid);

        JPQLQuery<StoreShortDTO> dtoQuery = from(store)
                .leftJoin(keyword).on(store.sid.eq(keyword.store.sid).and(expression.get("keyword")))
                .leftJoin(image).on(store.sid.eq(image.store.sid))
                .where(expression.get("category"))
                .where(expression.get("keyword"))
                .where(expression.get("sector"))
                .where(expression.get("storeId"))
                .where(expression.get("storeName"))
                .where(expression.get("lat"))
                .where(expression.get("lon"))
                .groupBy(store.sid, store.name, store.lat, store.lon, store.category, store.sector)
                .select(Projections.fields(StoreShortDTO.class,
                        store.sid.as("id"),
                        store.name.as("storeName"),
                        store.lat,
                        store.lon,
                        store.category,
                        store.sector,
                        ExpressionUtils.as(subQuery, "mainImage")
                ));
        // INFO: 위치 정보로 조건 및 정렬
        if (condition.lat() != null && condition.lon() != null) {
            dtoQuery.orderBy(Expressions.numberTemplate(Double.class, "ABS({0} - {1}) + ABS({2} - {3})",
                            store.lat, condition.lat(), store.lon, condition.lon())
                    .asc());
        }
        // MEMO: Order 조건 추가 가능
        if (pageable != null) {
            List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable, store);
            dtoQuery.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));

            // Pagination 처리
            Objects.requireNonNull(this.getQuerydsl()).applyPagination(pageable, dtoQuery);
        } else {
            pageable = PageRequest.of(0, 10);
        }

        List<StoreShortDTO> dtoList = dtoQuery.fetch();
        long count = dtoQuery.fetchCount();
        dtoList.forEach(this::addKeywordList);
        return new PageImpl<>(dtoList, pageable, count);
    }

    private Map<String, BooleanExpression> booleanExpressionMap(StoreSearchCondition condition, QStore store, QKeyword keyword) {
        Map<String, BooleanExpression> expressionMap = new HashMap<>();
        if (condition.keyword() != null) {
            expressionMap.put("keyword", keyword.keyword.stringValue().eq(condition.keyword()));
        }
        if (condition.storeId() != null) {
            expressionMap.put("storeId", store.sid.eq(condition.storeId()));
        }
        if (condition.storeName() != null) {
            expressionMap.put("storeName", store.name.contains(condition.storeName()));
        }
        if (condition.category() != null) {
            expressionMap.put("category", store.category.contains(condition.category()));
        }
        if (condition.sector() != null) {
            expressionMap.put("sector", store.sector.containsIgnoreCase(condition.sector()));
        }
        if (condition.lat() != null) {
            expressionMap.put("lat", store.lat.between(condition.lat() - KM * condition.multi(), condition.lat() + KM * condition.multi()));
        }
        if (condition.lon() != null) {
            expressionMap.put("lon", store.lon.between(condition.lon() - KM * condition.multi(), condition.lon() + KM * condition.multi()));
        }
        return expressionMap;
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable, QStore store) {
        return pageable.getSort().stream()
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
                    } else if (order.getProperty().equalsIgnoreCase("updatedAt")) {
                        return order.isDescending() ? store.updatedAt.desc() : store.updatedAt.asc();
                    } else {
                        throw new IllegalArgumentException("Not Support Sort Method");
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private void addKeywordList(StoreShortDTO storeShortDTO) {
        QKeyword keyword = QKeyword.keyword1;
        List<String> keywords = from(keyword)
                .select(keyword.keyword)
                .where(keyword.store.sid.eq(storeShortDTO.getId()))
                .fetch()
                .stream()
                .map(Enum::name)
                .toList();
        storeShortDTO.setKeywords(keywords);
    }

}
