package capstone.miso.dishcovery.domain.parkinglot.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Row {

    @SerializedName("PARKING_NAME")
    @Expose
    private String parkingName;
    @SerializedName("ADDR")
    @Expose
    private String addr;
    @SerializedName("PARKING_CODE")
    @Expose
    private String parkingCode;
    @SerializedName("PARKING_TYPE")
    @Expose
    private String parkingType;
    @SerializedName("PARKING_TYPE_NM")
    @Expose
    private String parkingTypeNm;
    @SerializedName("OPERATION_RULE")
    @Expose
    private String operationRule;
    @SerializedName("OPERATION_RULE_NM")
    @Expose
    private String operationRuleNm;
    @SerializedName("TEL")
    @Expose
    private String tel;
    @SerializedName("QUE_STATUS")
    @Expose
    private String queStatus;
    @SerializedName("QUE_STATUS_NM")
    @Expose
    private String queStatusNm;
    @SerializedName("CAPACITY")
    @Expose
    private Double capacity;
    @SerializedName("PAY_YN")
    @Expose
    private String payYn;
    @SerializedName("PAY_NM")
    @Expose
    private String payNm;
    @SerializedName("NIGHT_FREE_OPEN")
    @Expose
    private String nightFreeOpen;
    @SerializedName("NIGHT_FREE_OPEN_NM")
    @Expose
    private String nightFreeOpenNm;
    @SerializedName("WEEKDAY_BEGIN_TIME")
    @Expose
    private String weekdayBeginTime;
    @SerializedName("WEEKDAY_END_TIME")
    @Expose
    private String weekdayEndTime;
    @SerializedName("WEEKEND_BEGIN_TIME")
    @Expose
    private String weekendBeginTime;
    @SerializedName("WEEKEND_END_TIME")
    @Expose
    private String weekendEndTime;
    @SerializedName("HOLIDAY_BEGIN_TIME")
    @Expose
    private String holidayBeginTime;
    @SerializedName("HOLIDAY_END_TIME")
    @Expose
    private String holidayEndTime;
    @SerializedName("SYNC_TIME")
    @Expose
    private String syncTime;
    @SerializedName("SATURDAY_PAY_YN")
    @Expose
    private String saturdayPayYn;
    @SerializedName("SATURDAY_PAY_NM")
    @Expose
    private String saturdayPayNm;
    @SerializedName("HOLIDAY_PAY_YN")
    @Expose
    private String holidayPayYn;
    @SerializedName("HOLIDAY_PAY_NM")
    @Expose
    private String holidayPayNm;
    @SerializedName("FULLTIME_MONTHLY")
    @Expose
    private String fulltimeMonthly;
    @SerializedName("GRP_PARKNM")
    @Expose
    private String grpParknm;
    @SerializedName("RATES")
    @Expose
    private Double rates;
    @SerializedName("TIME_RATE")
    @Expose
    private Double timeRate;
    @SerializedName("ADD_RATES")
    @Expose
    private Double addRates;
    @SerializedName("ADD_TIME_RATE")
    @Expose
    private Double addTimeRate;
    @SerializedName("BUS_RATES")
    @Expose
    private Double busRates;
    @SerializedName("BUS_TIME_RATE")
    @Expose
    private Double busTimeRate;
    @SerializedName("BUS_ADD_TIME_RATE")
    @Expose
    private Double busAddTimeRate;
    @SerializedName("BUS_ADD_RATES")
    @Expose
    private Double busAddRates;
    @SerializedName("DAY_MAXIMUM")
    @Expose
    private Double dayMaximum;
    @SerializedName("LAT")
    @Expose
    private Double lat;
    @SerializedName("LNG")
    @Expose
    private Double lng;

}
