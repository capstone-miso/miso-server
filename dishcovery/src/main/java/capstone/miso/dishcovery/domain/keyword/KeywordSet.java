package capstone.miso.dishcovery.domain.keyword;

import lombok.Getter;

@Getter
public enum KeywordSet {
    BREAKFAST("아침에 가기 좋은 집"),
    LUNCH("점심에 가기 좋은 집"),
    DINNER("저녁에 가기 좋은 집"),
    SPRING("봄에 가기 좋은 집"),
    SUMMER("여름 맛집"),
    FALL("가을 맛집"),
    WINTER("겨울 맛집"),
    FOUR_SEASONS("사계절 맛집"),
    UNDER_COST_8000("8000원 이하 맛집"),
    UNDER_COST_15000("15000원 이하 맛집"),
    UNDER_COST_25000("25000원 이하 맛집"),
    OVER_COST_25000("25000원 이상 맛집"),
    UNDER_PARTICIPANTS_5("4인 이하 맛집"),
    UNDER_PARTICIPANTS_10("10인 이하 맛집"),
    UNDER_PARTICIPANTS_20("20인 이하 맛집"),
    OVER_PARTICIPANTS_20("20인 이상 맛집"),
    HIGH_TOTAL_COST("상위 10% 가성비 맛집"),
    HIGH_TOTAL_PARTICIPANTS("상위 10% 방문자 수 맛집"),
    HIGH_TOTAL_VISITED("상위 10% 방문 수 맛집");

    private final String korean;
    KeywordSet(String korean){
        this.korean = korean;
    }
}