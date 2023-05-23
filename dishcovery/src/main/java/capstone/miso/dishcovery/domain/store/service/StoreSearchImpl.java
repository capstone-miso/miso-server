package capstone.miso.dishcovery.domain.store.service;

import capstone.miso.dishcovery.domain.image.QImage;
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

import java.util.*;
import java.util.stream.Collectors;


/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 */
@Log4j2
public class StoreSearchImpl extends QuerydslRepositorySupport implements StoreSearch {
    private final static double KM = 0.0045d;
    private final static String[] CAFETERIA = {"음식점 > 한식%", "음식점 > 일식%", "음식점 > 양식%", "음식점 > 샤브샤브%", "음식점 > 패스트푸드%",
            "음식점 > 중식%", "음식점 > 퓨전요리%", "음식점 > 아시아음식%", "음식점 > 분식%", "음식점 > 패밀리레스토랑%", "음식점 > 기사식당%", "음식점 > 치킨%",
            "음식점 > 도시락%", "음식점 > 뷔페%", "음식점 > 샐러드%"};
    private final static String[] DESSERT = {"음식점 > 간식%", "음식점 > 카페%"};

    public StoreSearchImpl() {
        super(Store.class);
    }

    private static BooleanExpression CAFETERIA_BOOLEAN_EXPRESSION() {
        QStore store = QStore.store;
        BooleanExpression expression = store.category.like(CAFETERIA[0]);
        for (int i = 1; i < CAFETERIA.length; i++) {
            expression.or(store.category.like(CAFETERIA[i]));
        }
        return expression;
    }

    private static BooleanExpression DESSERT_BOOLEAN_EXPRESSION() {
        QStore store = QStore.store;
        BooleanExpression expression = store.category.like(DESSERT[0]);
        for (int i = 1; i < DESSERT.length; i++) {
            expression.or(store.category.like(DESSERT[i]));
        }
        return expression;
    }

    /**
     * 간단한 매장 리스트 추출
     * 카테고리 및 키워드를 기반으로 매장 리스트 조회
     * 매장 리스트에는 해당 매장의 키워드 및 나의 관심매장 정보 추가
     *
     * @param condition (매장 ID, 매장명, 카테고리, 키워드, 구역, 위도, 경도, 지도배율)
     * @param pageable
     * @return
     */
    @Override
    public Page<StoreShortDTO> searchAllStoreShort(StoreSearchCondition condition, Pageable pageable) {
        QStore store = QStore.store;
        QImage image = QImage.image;
        QKeyword keyword = QKeyword.keyword1;
        Map<String, BooleanExpression> expression = booleanExpressionMap(condition, store, keyword);
        /*
          매장 이미지 조회 SQL
          photoId 가 'M'인 이미지를 최우선으로 하나 조회
          만일 'M'인 이미지가 없는 경우 가장 처음 이미지 하나만 로드해 온다.
         */
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
                        store.address,
                        store.phone,
                        store.lat,
                        store.lon,
                        store.category,
                        store.sector,
                        ExpressionUtils.as(subQuery, "mainImage")
                ));
        // INFO: 위치 정보로 조건 및 정렬
        if (condition.getLat() != null && condition.getLon() != null) {
            dtoQuery.orderBy(Expressions.numberTemplate(Double.class, "ABS({0} - {1}) + ABS({2} - {3})",
                            store.lat, condition.getLat(), store.lon, condition.getLon())
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
        if (condition.getKeyword() != null) {
            expressionMap.put("keyword", keyword.keyword.stringValue().containsIgnoreCase(condition.getKeyword()));
        }
        if (condition.getStoreId() != null && condition.getStoreId().size() > 0) {
            expressionMap.put("storeId", store.sid.in(condition.getStoreId()));
        }
        if (condition.getStoreName() != null) {
            expressionMap.put("storeName", store.name.contains(condition.getStoreName()));
        }
        if (condition.getCategory() != null) {
            String searchCategory = condition.getCategory();
            if (searchCategory.equals("식당")) {
                expressionMap.put("category", CAFETERIA_BOOLEAN_EXPRESSION());
            } else if (searchCategory.equals("디저트")) {
                expressionMap.put("category", DESSERT_BOOLEAN_EXPRESSION());
            } else {
                expressionMap.put("category", store.category.contains(searchCategory));
            }
        }
        if (condition.getSector() != null) {
            expressionMap.put("sector", store.sector.containsIgnoreCase(condition.getSector()));
        }
        if (condition.getLat() != null) {
            expressionMap.put("lat", store.lat.between(condition.getLat() - KM * condition.getMulti(), condition.getLat() + KM * condition.getMulti()));
        }
        if (condition.getLon() != null) {
            expressionMap.put("lon", store.lon.between(condition.getLon() - KM * condition.getMulti(), condition.getLon() + KM * condition.getMulti()));
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

    /**
     * 매장에 키워드 정보 추가
     */
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
