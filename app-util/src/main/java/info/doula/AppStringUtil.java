package info.doula;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by hossaindoula on 12/23/16.
 */
public final class AppStringUtil extends StringUtils {

    public static int convertToZeroOrRealNumber(String props){
        if(props == null)
            return 0;
        else
            return Integer.parseInt(props);
    }
}
