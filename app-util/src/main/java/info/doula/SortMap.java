package info.doula;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by hossaindoula on 6/15/2017.
 */
@SuppressWarnings("unchecked")
public class SortMap {
    @SuppressWarnings("rawtypes")
    public static Map sortMap = new LinkedHashMap();

    static{
        sortMap.put("standard", "relevancy");
        sortMap.put("-reviewCount", "-review_num");
        sortMap.put("+reviewCount", "+review_num");
        sortMap.put("-affiliateRate", "-aflrate");
        sortMap.put("+affiliateRate", "+aflrate");
        sortMap.put("-itemPrice", "-item_price");
        sortMap.put("+itemPrice", "+item_price");
        sortMap.put("-updateTimestamp", "-update_timestamp");
        sortMap.put("+updateTimestamp", "+update_timestamp");
        sortMap.put("-reviewAverage", "-review_ave");
        sortMap.put("+reviewAverage", "+review_ave");
        sortMap.put("relevancyLowprice", "relevancy_lowprice");
        sortMap.put("relevancyHighprice", "relevancy_highprice");
        sortMap.put("relevancyReviewNum", "relevancy_review_num");
        sortMap.put("relevancyReviewAve", "relevancy_review_ave");
    }
}