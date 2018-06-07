package com.soubu.search.score;

public final class ScoreConst {
    public static final String BASE_KEY = "base";
    public static final String MAGIC_KEY = "magic";
    
    // shop deposit start
    public static final String SHOP_THRESHOLD = "threshold";
    public static final String SHOP_DEPOSIT_TIME_FIELD = "deposit_time";
    public static final String SHOP_ENTRIES = "entries";
    public static final String DEPOSIT_HOT_THRESHOLD_SHOP = "deposit_hot_threshold_shop";
    public static final String DEPOSIT_HOT_THRESHOLD_PRODUCT = "deposit_hot_threshold_product";
    // shop deposit end

    // product deposit start
    public static final String DEPOSIT_PRODUCT_DISCOUNT = "deposit_product_discount";
    // product deposit end

    public static final String BUY_CLICKS_SCORE_KEY = "buy_clicks";
    public static final String PRODUCT_CLICKS_SCORE_KEY = "product_clicks";
    public static final String PRODUCT_CLICKS_1_SCORE_KEY = "product_clicks_1";
    public static final String PRODUCT_COLLECT_SCORE_KEY = "product_collect";
    public static final String PRODUCT_COLLECT_3_SCORE_KEY = "product_collect_3";
    public static final String PRODUCT_ACTIVITY_SCORE_KEY = "product_activity";
    public static final String PRODUCT_ORDERS_AMOUNT_SCORE_KEY = "product_orders_amount";

    public static final String PRODUCT_ORDERS_TOTAL_SCORE_KEY = "product_orders_total";

    public static final String SHOP_CLICKS_SCORE_KEY = "shop_clicks";
    public static final String SHOP_PRODUCT_NUMBER_SCORE_KEY = "shop_product_number";
    public static final String SHOP_ACTIVITY_SCORE_KEY = "shop_activity";
    public static final String SHOP_ORDERS_AMOUNT_SCORE_KEY = "shop_orders_amount";
    public static final String SHOP_ORDERS_TOTAL_SCORE_KEY = "shop_orders_total";

    public static final String PRODUCT_ORDERS_7_SCORE_KEY = "product_orders_7";
    public static final String PRODUCT_ORDERS_15_SCORE_KEY = "product_orders_15";
    public static final String PRODUCT_ORDERS_30_SCORE_KEY = "product_orders_30";

    // score normalize filter analyzer
    public static final String NORMALIZE_FILTER_SCORE_MIN = "normalize_filter_score_min";
    public static final String NORMALIZE_FILTER_SCORE_MAX = "normalize_filter_score_max";
    public static final String NORMALIZE_FILTER_NORMAL_MAX = "normalize_filter_normal_max";
    public static final String NORMALIZE_FILTER_NORMAL_MIN = "normalize_filter_normal_min";

    public static final String RANGE_FILTER_SCORE_MIN = "score_range_filter_score_min";
    public static final String RANGE_FILTER_SCORE_MAX = "score_range_filter_score_max";

    // supplier
//    public static final String SUPPLIER_TIMES_CONVERSION_RATE_7 = "Supplier_times_conversion_rate_7";
    public static final String SUPPLIER_TIMES_CONVERSION_RATE_15 = "Supplier_times_conversion_rate_15";
    public static final String SUPPLIER_TIMES_CONVERSION_RATE_30 = "Supplier_times_conversion_rate_30";
//    public static final String SUPPLIER_MONEY_CONVERSION_RATE_7 = "Supplier_money_conversion_rate_7";
    public static final String SUPPLIER_MONEY_CONVERSION_RATE_15 = "Supplier_money_conversion_rate_15";
    public static final String SUPPLIER_MONEY_CONVERSION_RATE_30 = "Supplier_money_conversion_rate_30";

}
