package capstone.miso.dishcovery.domain.store.service;

import capstone.miso.dishcovery.domain.keyword.Keyword;
import capstone.miso.dishcovery.domain.keyword.KeywordSet;
import capstone.miso.dishcovery.domain.keyword.QKeyword;
import capstone.miso.dishcovery.domain.keyword.QKeywordData;
import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.parkinglot.Parkinglot;
import capstone.miso.dishcovery.domain.parkinglot.QParkinglot;
import capstone.miso.dishcovery.domain.preference.QPreference;
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
    public StoreSearchImpl() {
        super(Store.class);
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
        QPreference preference = QPreference.preference;
        QKeywordData keywordData = QKeywordData.keywordData;
        Map<String, BooleanExpression> expression = booleanExpressionMap(condition, store);

        JPQLQuery<StoreShortDTO> dtoQuery = from(store)
                .leftJoin(preference).select(preference.store.sid).on(store.sid.eq(preference.store.sid))
                .leftJoin(keywordData).select(keywordData.totalCost, keywordData.totalVisited).on(store.eq(keywordData.store))
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
                        store.mainImageUrl.as("mainImage"),
                        store.category,
                        store.categoryKey.as("categoryGroup"),
                        store.sector,
                        preference.count().as("preferenceCount"), // preference 정렬 조건
                        keywordData.totalVisited.as("totalVisit"), // visit 정렬 조건
                        keywordData.totalCost.as("totalCost") // cost 정렬 조건
                ))
                .distinct();
        // keyword 에 대하여 검색할 시 사용됨
        if (condition.getKeyword() != null) {
            QKeyword keyword = QKeyword.keyword1;
            List<Store> keywordFind = from(keyword)
                    .select(keyword.store)
                    .where(keyword.keyword.stringValue().containsIgnoreCase(condition.getKeyword()))
                    .fetch();
            dtoQuery.where(store.in(keywordFind));
        }
        // INFO: 위치 정보로 조건 및 정렬하여 선택
        OrderSpecifier<Double> orderByDistance = null;
        if (condition.getLat() != null && condition.getLon() != null) {
            orderByDistance = Expressions.numberTemplate(Double.class, "ABS({0} - {1}) + ABS({2} - {3})",
                            store.lat, condition.getLat(), store.lon, condition.getLon())
                    .asc();
        }
        // Member preference 조회 설정 1 : 나의 관심 매장, 2: 나의 관심 매장 X
        if (condition.getPreference() != 0L && condition.getMember() != null) {
            List<Long> myPreferenceStoreIds = from(preference)
                    .select(preference.store.sid)
                    .where(preference.member.eq(condition.getMember()))
                    .fetch();
            if (condition.getPreference() == 1L) {
                dtoQuery.where(store.sid.in(myPreferenceStoreIds));
            } else if (condition.getPreference() == 2L) {
                dtoQuery.where(store.sid.notIn(myPreferenceStoreIds));
            }
        }
        // MEMO: Order 조건 추가 가능
        List<OrderSpecifier<?>> orderSpecifiers = null;
        if (pageable != null) {
            orderSpecifiers = getOrderSpecifiers(pageable);
            dtoQuery.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));
            // Pagination 처리
            int page = pageable.getPageNumber();
            int size = pageable.getPageSize();
            Objects.requireNonNull(this.getQuerydsl()).applyPagination(PageRequest.of(page, size), dtoQuery);
        } else {
            pageable = PageRequest.of(0, 10);
        }
        // dtoQuery 에 정렬 기준 적용 (거리, 좋아요, 금액, 방문)
        if (orderByDistance != null && orderSpecifiers != null) {
            orderSpecifiers.add(0, orderByDistance);
            dtoQuery.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));
        } else if (orderByDistance != null) {
            dtoQuery.orderBy(orderByDistance);
        } else if (orderSpecifiers != null) {
            dtoQuery.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));
        }

        List<StoreShortDTO> dtoList = dtoQuery.fetch();
        long count = dtoQuery.fetchCount();
        dtoList.forEach(this::addKeywordList);
        if (condition.getPreference() == 0 && condition.getMember() != null) {
            checkMyPreferences(dtoList, condition.getMember());
        }
        return new PageImpl<>(dtoList, pageable, count);
    }

    private Map<String, BooleanExpression> booleanExpressionMap(StoreSearchCondition condition, QStore store) {
        Map<String, BooleanExpression> expressionMap = new HashMap<>();
        if (condition.getStoreId() != null && condition.getStoreId().size() > 0) {
            expressionMap.put("storeId", store.sid.in(condition.getStoreId()));
        }
        if (condition.getStoreName() != null) {
            expressionMap.put("storeName", store.name.contains(condition.getStoreName()));
        }
        if (condition.getCategory() != null) {
            String searchCategory = condition.getCategory();
            if (searchCategory.equals("식당")) {
                expressionMap.put("category", store.categoryKey.eq("음식점"));
            } else if (searchCategory.equals("디저트")) {
                expressionMap.put("category", store.categoryKey.contains("카페"));
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

    private List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable) {
        QStore store = QStore.store;
        QPreference preference = QPreference.preference;
        QKeywordData keywordData = QKeywordData.keywordData;
        return pageable.getSort().stream()
                .map(order -> {
                    if (order.getProperty().equalsIgnoreCase("preference")) {
                        return order.isDescending() ? preference.count().desc() : preference.count().asc();
                    } else if (order.getProperty().equalsIgnoreCase("visit")) {
                        return order.isDescending() ? keywordData.totalVisited.desc() : keywordData.totalVisited.asc();
                    } else if (order.getProperty().equalsIgnoreCase("cost")) {
                        return order.isDescending() ? keywordData.totalCost.desc() : keywordData.totalVisited.asc();
                    } else if (order.getProperty().equalsIgnoreCase("name")) {
                        return order.isDescending() ? store.name.asc() : store.name.desc();
                    } else if (order.getProperty().equalsIgnoreCase("update")) {
                        return order.isDescending() ? store.updatedAt.desc() : store.updatedAt.asc();
                    } else {
                        return null;
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
        List<KeywordSet> keywords = from(keyword)
                .select(keyword.keyword)
                .where(keyword.store.sid.eq(storeShortDTO.getId()))
                .fetch();

        storeShortDTO.setKeywords(keywords.stream().map(KeywordSet::getKorean).toList());
    }

    private void checkMyPreferences(List<StoreShortDTO> storeShortDTOS, Member member) {
        QPreference preference = QPreference.preference;

        for (StoreShortDTO storeShortDTO : storeShortDTOS) {
            boolean check = from(preference)
                    .where(preference.member.eq(member))
                    .where(preference.store.sid.eq(storeShortDTO.getId()))
                    .fetchFirst() != null;
            storeShortDTO.setPreference(check);
        }
    }
}
