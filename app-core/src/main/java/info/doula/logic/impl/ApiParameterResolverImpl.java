package info.doula.logic.impl;

import com.google.common.collect.Maps;
import info.doula.exception.ParameterResolveException;
import info.doula.logic.ApiParameterResolver;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

import static info.doula.entity.JsonAttributes.*;
import static info.doula.util.AppConstants.*;
import static info.doula.util.NumberUtils.*;
import static info.doula.util.ObjectUtils.isNullObject;

/**
 * Created by hossaindoula<hossaindoula@gmail.com> on 6/11/2017.
 */
@Component("apiParameterResolver")
public class ApiParameterResolverImpl implements ApiParameterResolver {

    private Pattern pattern;

    /**
     * Resolve request map
     *
     * If requestTemplateMap has fastforward is true then, it will return actual httpRequest
     * It checks actual httpRequest with requestTemplateMap as mentioned json file and prepare the request accordingly
     *
     * @see info.doula.entity.JsonAttributes class for json attributes
     *
     * @param dataMap
     * @param jsonTemplateMap
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public LinkedHashMap<String, Object> resolveRequestParameter(Map<String, Object> dataMap,  Map<String, Object> jsonTemplateMap)
        throws ParameterResolveException {

        LinkedHashMap<String, Object> actualRequest = dataMap.get("request") != null?
                (LinkedHashMap<String, Object>) dataMap.get("request") : Maps.newLinkedHashMap();
        Map requestTemplateMap = dataMap.get(REQUEST) != null? (Map)dataMap.get(REQUEST) : Collections.emptyMap();

        if(requestTemplateMap.get(FAST_FORWARD).toString().equals("true")) return actualRequest;

        LinkedHashMap generatedMap = new LinkedHashMap();
        List<?> templateParameterMap = requestTemplateMap.get(PARAMETERS) != null ?
                (ArrayList<?>)requestTemplateMap.get(PARAMETERS) : Collections.emptyList();

        if(isNull(templateParameterMap))
            throw new ParameterResolveException("invalid request parameters in json configuration file. " +
                    "request parameters should be list");

        for(Object templateParameter : templateParameterMap)
            resolveRequestRecursively(actualRequest, (Map)templateParameter, generatedMap, dataMap);

        return generatedMap;

    }

    /**
     * Reslove request checks actual httpRequest with requestTemplateMap as mentioned json file and prepare the request accordingly
     * @param requestMap
     * @param templateData
     * @param generatedRequestMap
     * @param dataMap
     * @throws ParameterResolveException
     */
    @SuppressWarnings("unchecked")
    private void resolveRequestRecursively(Map<String, Object> requestMap, Map<String, Object> templateData,
                                           Map<String, Object> generatedRequestMap, Map<String, Object> dataMap)
                                            throws ParameterResolveException {
        String key = templateData.get(NAME).toString();
        String type = templateData.get(TYPE).toString();
        String source = templateData.get(SOURCE) != null ? templateData.get(SOURCE).toString() : key;

        switch(type) {
            case TYPE_BOOLEAN:
                Object booleanValue = getParameterValue(templateData, source, requestMap);
                if(booleanValue != null) {
                    boolean value = false;
                    if(booleanValue.toString().toLowerCase().equals("true"))
                        value = true;
                    else if (isInteger(booleanValue))
                        value = toInt(booleanValue.toString()) > 0;
                    generatedRequestMap.put(key, value);
                }
                break;

            case TYPE_INT:
            case TYPE_INTEGER:
                Object integerValue = getParameterValue(templateData, source, requestMap);
                if(isNull(integerValue)) {
                    if(!isInteger(integerValue))
                        throw new ParameterResolveException(source + " " + PR_MST_INT);
                    int givenValue = toInt(integerValue.toString());
                    Integer maxValue = !isNullObject(templateData.get(MAX_VALUE)) ?
                            toInt(templateData.get(MAX_VALUE).toString())  : null;
                    Integer minValue = !isNullObject(templateData.get(MIN_VALUE)) ?
                            toInt(templateData.get(MIN_VALUE).toString()) : null;
                    checkMaxMinValue(maxValue, minValue, source, givenValue);
                    generatedRequestMap.put(key, givenValue);
                }
                break;

            case TYPE_LONG:
                Object longValue = getParameterValue(templateData, source, requestMap);
                if(isNull(longValue)) {
                    if(!isLong(longValue))
                        throw new ParameterResolveException(source + " " + PR_MST_LNG);
                    long givenValue = toLong(longValue.toString());
                    Long maxValue = !isNullObject(templateData.get(MAX_VALUE))? toLong(templateData.get(MAX_VALUE).toString()) : null;
                    Long minValue = !isNullObject(templateData.get(MIN_VALUE))? toLong(templateData.get(MIN_VALUE).toString()) : null;
                    checkMaxMinValue(maxValue, minValue, source, givenValue);
                    generatedRequestMap.put(key, givenValue);
                }
                break;

            case TYPE_DECIMAL:
                Object decimalValue = getParameterValue(templateData, source, requestMap);
                if(isNull(decimalValue)) {
                    if(!isBigDecimal(decimalValue.toString()))
                        throw new ParameterResolveException(source + " " + PR_MST_DEC);
                    doValidatePattern(decimalValue, templateData);
                    BigDecimal givenValue = new BigDecimal(decimalValue.toString());
                    BigDecimal maxValue = !isNullObject(templateData.get(MAX_VALUE)) ? new BigDecimal(templateData.get(MAX_VALUE).toString()) : null;
                    BigDecimal minValue = !isNullObject(templateData.get(MIN_VALUE)) ? new BigDecimal(templateData.get(MIN_VALUE).toString()) : null;
                    checkMaxMinValue(maxValue, minValue, source, givenValue);
                    generatedRequestMap.put(key, givenValue);
                }
                break;

            case TYPE_OPTION:
                Object optionValue = getParameterValue(templateData, source, requestMap);
                if(isNullObject(optionValue)) {
                    List values = (List)templateData.get(OPTION);
                    if(isNullObject(values))
                        throw new ParameterResolveException(source + " " + OPT_MST);

                    if(values.stream().anyMatch(ti -> ti != optionValue))
                        throw new ParameterResolveException("set " + source + " from " + values.add(','));

                    generatedRequestMap.put(key, optionValue);
                }
                break;

            case TYPE_INT_OPTION:
                Object intOptionValue = getParameterValue(templateData, source, requestMap);
                if(isNullObject(intOptionValue)) {
                    if(!isInteger(intOptionValue)) throw new ParameterResolveException(source + " " + PR_MST_INT);

                    int givenValue = toInt(intOptionValue.toString());
                    List<Integer> values = (List<Integer>)templateData.get(OPTION);

                    if(isNullObject(values))
                        throw new ParameterResolveException(source + " " + OPT_MST);
                    if(values.stream().anyMatch(ti -> ti != givenValue))
                        throw new ParameterResolveException("set " + source + " from " + values);
                    generatedRequestMap.put(key, givenValue);
                }
                break;

            case TYPE_STRING:
                String stringValue = getParameterValue(templateData, source, requestMap).toString();
                if(!isNullObject(stringValue)) {
                    doValidatePattern(stringValue, templateData);
                    checkMaxMinStringLength(templateData, source, stringValue);
                    generatedRequestMap.put(key, stringValue);
                }
                break;

            case TYPE_FIXED:
                generatedRequestMap.put(key, templateData.get(VALUE));
                break;

            case TYPE_INT_ARRAY:
            case TYPE_INTEGER_ARRAY:
                Object parameterValues = getParameterValue(templateData, source, requestMap);

                if(parameterValues != null) {
                    List<Integer> array = new ArrayList<>();
                    List<Integer> valuesArray = new ArrayList<>();

                    if(parameterValues instanceof List)
                        array = (List)parameterValues;
                    else
                        array.add(toInt(parameterValues));

                    if(isNull(templateData.get(MAX_SIZE))) {
                        int maxSize = toInt(templateData.get(MAX_SIZE).toString());
                        if(array.size() > maxSize)
                            throw new ParameterResolveException(source + " " + ARR_MST_UND + " " + maxSize);
                    }

                    for(Integer it : array){
                        if(!isInteger(it)) throw new ParameterResolveException(source + " " + PR_MST_NMBR);
                        int parameterValue = toInt(it);
                        Object maxValue = templateData.get(MAX_VALUE);
                        Object minValue = templateData.get(MIN_VALUE);

                        if(!isNullObject(maxValue) && isNotUnderMaxValue(parameterValue, maxValue))
                            throw new ParameterResolveException("all " + source + "'s must be under " + maxValue);
                        if(!isNullObject(minValue) && isNotOverMinValue(parameterValue, minValue))
                            throw new ParameterResolveException("all " + source + "'s must be over " + minValue);

                        valuesArray.add(parameterValue);
                    }
                    generatedRequestMap.put(key, valuesArray);
                }
                break;

            case TYPE_LONG_ARRAY:
                Object parameterLongValues = getParameterValue(templateData, source, requestMap);

                if(parameterLongValues != null) {
                    List<Long> longArray = new ArrayList<>();
                    List<Long> longValuesArray = new ArrayList<>();

                    if(parameterLongValues instanceof List)
                        longArray = (List)parameterLongValues;
                    else
                        longArray.add(toLong(parameterLongValues));

                    if(!isNullObject(templateData.get(MAX_SIZE))) {
                        int maxSize = toInt(templateData.get(MAX_SIZE).toString());
                        if(longArray.size() > maxSize)
                            throw new ParameterResolveException(source + " " + ARR_MST_UND + " " + maxSize);
                    }

                    for(Long it : longArray){
                        if(!isLong(it)) throw new ParameterResolveException(source + " " + PR_MST_NMBR);
                        Long parameterLong = toLong(it);
                        Object maxValue = templateData.get(MAX_VALUE);
                        Object minValue = templateData.get(MIN_VALUE);

                        if(!isNullObject(maxValue) && isNotUnderMaxValue(parameterLongValues, maxValue))
                            throw new ParameterResolveException("all " + source + "'s must be under " + maxValue);
                        if(!isNullObject(minValue) && isNotOverMinValue(parameterLongValues, minValue))
                            throw new ParameterResolveException("all " + source + "'s must be over " + minValue);

                        longValuesArray.add(parameterLong);
                    }

                    generatedRequestMap.put(key, longValuesArray);
                }
                break;

            case TYPE_STRING_ARRAY:
                List<String> stringValues = (List<String>)getParameterValue(templateData, source, requestMap);

                if(stringValues != null) {
                    List<String> stringArray = new ArrayList<>();
                    stringArray.addAll(stringValues);

                    if(templateData.get(MAX_SIZE) != null) {
                        int maxSize = toInt(templateData.get(MAX_SIZE).toString());
                        if(stringArray.size() > maxSize)
                            throw new ParameterResolveException(source + " " + ARR_MST_UND + " " + maxSize);
                    }

                    List<String> stringValuesArray = new ArrayList<>();
                    for(String st : stringArray){
                        pattern = Pattern.compile(templateData.get(PATTERN).toString());
                        if (!isNullObject(pattern) && !isNullObject(st) && !st.isEmpty() && !pattern.matcher(st).matches())
                            throw new ParameterResolveException("all " + source + "'s must be follow " + pattern);

                        Object maxLength = templateData.get(MAX_LENGTH);
                        Object minLength = templateData.get(MIN_LENGTH);

                        if(!isNullObject(maxLength) && st.length() > (toInt(maxLength.toString())))
                            throw new ParameterResolveException("all "+ source +"'s length must be under " + maxLength);
                        if(!isNullObject(minLength) && st.length() < (toInt(minLength.toString())))
                            throw new ParameterResolveException("all " + source + "'s length must be over " + minLength);

                        stringValuesArray.add(st);
                        generatedRequestMap.put(key, stringValuesArray);
                    }
                }
                break;

            case TYPE_OBJECT:
                Object objectValue = getParameterValue(templateData, source, requestMap);
                if(!isNullObject(objectValue)) {
                    LinkedHashMap generatedObjectMap = new LinkedHashMap();
                    if(!(templateData.get(PARAMETERS) instanceof List))
                        throw new ParameterResolveException(source + " " + PR_MST_LST);

                    List parameters = (List)templateData.get(PARAMETERS);
                    for(Object it : parameters)
                        resolveRequestRecursively((Map)objectValue, (Map)it, generatedObjectMap, null);

                    generatedRequestMap.put(key, generatedObjectMap);
                }
                break;

            case TYPE_OBJECT_ARRAY:
                Map objectArrayValue = (Map)getParameterValue(templateData, source, requestMap);

                if(!isNullObject(objectArrayValue)) {
                    List objArr = new ArrayList();
                    String childName = templateData.get(TYPE_OBJECT_CHILDNAME).toString();
                    if(!(templateData.get(PARAMETERS) instanceof List))
                        throw new ParameterResolveException(source + " " + PR_MST_LST);
                    if(childName != null) {
                        for(Object objectDataMap : (List)objectArrayValue.get(childName)){
                            Map generatedObjectResponse = new HashMap();
                            for(Object objectTemplateElement : (List) templateData.get(PARAMETERS)){
                                resolveRequestRecursively((Map)objectDataMap, (Map)objectTemplateElement, generatedObjectResponse, null);
                            }

                            Map childResponse = new HashMap();
                            objArr.add(childResponse);
                        }
                    } else {
                        for(Object objectDataMap : (List)objectArrayValue){
                            Map generatedObjectResponse = new HashMap();
                            for(Object objectTemplateElement : (List)templateData.get(PARAMETERS))
                                resolveRequestRecursively((Map)objectDataMap, (Map)objectTemplateElement, generatedObjectResponse, null);

                            objArr.add(generatedObjectResponse);
                        }
                    }
                    generatedRequestMap.put(key, objArr);
                }

                // Source for the value of the parameter with type=clientid is X-ClientId header
            case TYPE_CLIENTID:
                boolean isRequired = templateData.get(REQUIRED).toString().toLowerCase().equals("true");
                if(isRequired && (isNull(dataMap.get("clientId")) || StringUtils.isBlank(dataMap.get("clientId").toString())))
                    throw new ParameterResolveException("X-ClientId header is required");

                generatedRequestMap.put(key, dataMap.get("clientId"));
                break;

            case TYPE_SERVICE:
                generatedRequestMap.put(key, dataMap.get("service"));
                break;

            case TYPE_OPERATION:
                generatedRequestMap.put(key, dataMap.get("operation"));
                break;

            case TYPE_VERSION:
                generatedRequestMap.put(key, dataMap.get("version"));
                break;

            default:
                throw new ParameterResolveException(source + "  " + UNKNOWN_TYPE + type);
        }
    }

    /**
     * Fetch parameterValue from the actual request,
     * if the value is not exist in actual request then it checks whether that filed is required for not,
     * if field is required then throws exception, if filed is not required & default present in json file then it will return default value
     * @param templateData
     * @param datasource
     * @param requestMap
     * @return parameterValue
     */
    private Object getParameterValue(Map templateData, String datasource, Map requestMap)
            throws ParameterResolveException {
        Object parameterValue =  requestMap.get(datasource);

        boolean isRequired = templateData.get(REQUIRED).toString().toLowerCase().equals("true");
        if(isRequired) {
            if(!isNullObject(parameterValue) || StringUtils.isBlank(parameterValue.toString()))
                throw new ParameterResolveException(datasource + " " + PR_MST);
        } else {
            if(!isNullObject(parameterValue) || StringUtils.isBlank(parameterValue.toString()))
                parameterValue = templateData.get(DEFAULT);
        }
        return parameterValue;
    }

    /**
     * Check value is null
     *
     * @param object
     * @return true if object is null, else false
     */
    private boolean isNull(Object object) {
        return object == null || object instanceof JSONObject && isNullObject(object);
    }

    /**
     * Check minValue and maxValue only if minValue and maxValue attibutes exists in json file for corresponding field
     * @param maxValue
     * @param minValue
     * @param key
     * @param givenValue
     */
    private void checkMaxMinValue(Object maxValue, Object minValue, String key, Object givenValue)
            throws ParameterResolveException {
        if(isNull(maxValue) && isNotUnderMaxValue(givenValue, maxValue))
            throw new ParameterResolveException(key + " " + MST_UND + maxValue);

        if(isNull(minValue) && isNotOverMinValue(givenValue, minValue))
            throw new ParameterResolveException(key + " " + MST_OV + maxValue);

    }

    /**
     * Check givenValue is over the maxValue or not
     * @param givenValue
     * @param maxValue
     * @return true if the givenValue is over the maxValue
     */
    private boolean isNotUnderMaxValue(Object givenValue, Object maxValue) {
        return compareGreaterThan(givenValue, maxValue);
    }

    /**
     * Check givenValue is under the minValue or not
     * @param givenValue
     * @param minValue
     * @return true if the givenValue is under minValue
     */
    private boolean isNotOverMinValue(Object givenValue, Object minValue) {
        return compareLessThan(givenValue, minValue);
    }

    /**
     * Check givenValue is integer or not
     * @param givenValue
     * @return true if the givenValue is integer
     */
    private boolean isInteger(Object givenValue) {
        return isInteger(givenValue.toString());
    }

    /**
     * Check givenValue is long or not
     * @param givenValue
     * @return true if the givenValue is long
     */
    private boolean isLong(Object givenValue) {
        return isLong(givenValue.toString());
    }

    /**
     * This will check minLength and maxLength of string only minLenth or maxLength attibutes exists in json file for corresponding field
     * @param templateData
     * @param key
     * @param givenValue
     */
    private void checkMaxMinStringLength(Map templateData, String key, String givenValue)
                                            throws ParameterResolveException {

        Object maxLength = templateData.get(MAX_LENGTH);
        Object minLength = templateData.get(MIN_LENGTH);
        if(!isNullObject(maxLength) && givenValue.length() > (toInt(maxLength)))
            throw new ParameterResolveException(key + " " + LEN_MST_UND + " " + maxLength);

        if(!isNullObject(minLength) && givenValue.length() < (toInt(minLength) ))
            throw new ParameterResolveException(key + " " + LEN_MST_OV + " " + minLength);
    }

    /**
     * This will check pattern only pattern attibute exists in json file for corresponding field
     * @param origin
     * @param templateData
     */
    private void doValidatePattern(Object origin, Map templateData) throws ParameterResolveException {
        pattern = Pattern.compile(templateData.get(PATTERN).toString());
        if (!isNullObject(pattern) && !isNullObject(origin) && !origin.toString().isEmpty()) {
            if (!pattern.matcher(origin.toString()).matches())
                throw new ParameterResolveException(templateData.get(SOURCE) != null? "" :
                        templateData.get(NAME) + " " + MST_FLLW + " " + pattern);

        }
    }


    /**
     * Resolve response parameters recursively
     * checks actual response with templateParameterMap as mentioned json file and prepare the response accordingly
     * @param response
     * @param jsonTemplateMap
     * @return generatedMap
     */
    public LinkedHashMap<String, ?> resolveResponseParameter(Map<String, Object> response,
                                                             Map<String, Object> jsonTemplateMap)
                                                            throws ParameterResolveException {
        LinkedHashMap<String, Object> actualResponse = !isNullObject(response) ?
                (LinkedHashMap<String, Object>)response: Maps.newLinkedHashMap();
        Map<String, Object> responseMap = !isNullObject(jsonTemplateMap.get(RESPONSE)) ?
                (Map<String, Object>)jsonTemplateMap.get(RESPONSE) : Collections.emptyMap();

        if(responseMap.get(FAST_FORWARD).toString().equals("true"))
            return actualResponse;

        LinkedHashMap<String, Object> generatedMap = new LinkedHashMap<>();

        Map<String, Object> templateParameterMap = !isNullObject(responseMap.get(PARAMETERS)) ?
                (Map<String, Object>)responseMap.get(PARAMETERS): Collections.emptyMap();
        if(!(templateParameterMap instanceof List)) {
            throw new ParameterResolveException("invalid response parameters configuration in json. " +
                    "response parameters should be list");
        }

        for(Object templateParameter : (List)templateParameterMap)
            resolveResponseRecursively(actualResponse, (Map)templateParameter, generatedMap);

        return generatedMap;
    }

    /**
     * Resolve response parameters recursively
     * checks actual response with templateParameterMap as mentioned json file and prepare the response accordingly
     * @param actualDataMap
     * @param templateData
     * @param generatedMap
     * @throws ParameterResolveException
     */
    private void resolveResponseRecursively(Map actualDataMap, Map templateData, Map generatedMap)
                                            throws ParameterResolveException {

        Map reslovedRequestMap = new HashMap();
        String key = templateData.get(NAME).toString();
        String type = templateData.get(TYPE).toString();
        String source = !isNullObject(templateData.get(SOURCE)) ?
                templateData.get(SOURCE).toString() : key;
        Object parameterValue =  actualDataMap.get(source);

        switch(type) {
            case TYPE_BOOLEAN:
                boolean convertedValue =  false;
                if(parameterValue.toString().toLowerCase().equals("true"))
                    convertedValue = true;
                else if (isNumber(parameterValue.toString()) && toInt(parameterValue.toString()) > 0)
                    convertedValue = true;

                generatedMap.put(key, convertedValue);
                break;

            case TYPE_INT:
            case TYPE_INTEGER:
                Object value = 0;
                if(parameterValue != null) {
                    if(!isInteger(parameterValue)) {
                        throw new ParameterResolveException(source + " " + PR_MST_INT);
                    }
                    value = toInt(parameterValue);
                }
                generatedMap.put(key, value);
                break;

            case TYPE_LONG:
                long longValue = 0;
                if(parameterValue != null) {
                    if(!isLong(parameterValue)) {
                        throw new ParameterResolveException(source + " " + PR_MST_LNG);
                    }
                    longValue = toLong(parameterValue);
                }

                generatedMap.put(key, longValue);
                break;

            case TYPE_DECIMAL:
                BigDecimal bigDecimalValue = new BigDecimal(0.0);
                if(parameterValue != null) {
                    if(!isBigDecimal(parameterValue.toString())) {
                        throw new ParameterResolveException(source + " " + PR_MST_DEC);
                    }
                    bigDecimalValue = new BigDecimal(parameterValue.toString());
                }

                generatedMap.put(key, bigDecimalValue);
                break;

            case TYPE_OPTION:
                if(parameterValue != null) {
                    List values = (List)templateData.get(OPTION);
                    if(values == null) {
                        throw new ParameterResolveException(source + " " + OPT_MST);
                    }
                    if(values.stream().anyMatch(ti -> ti != parameterValue)){
                        throw new ParameterResolveException("set " + source + " from " + values.add(','));
                    }
                }
                generatedMap.put(key, parameterValue);
                break;

            case TYPE_STRING:
                generatedMap.put(key, parameterValue.toString());
                break;

            case TYPE_FIXED:
                generatedMap.put(key, templateData.get(VALUE));
                break;

            case TYPE_INT_ARRAY:
            case TYPE_INTEGER_ARRAY:
                List<Integer> array = new ArrayList<>();
                if(parameterValue != null) {
                    if(parameterValue instanceof List) {
                        for(Object it : (List)parameterValue){
                            if(!isInteger(it)) {
                                throw new ParameterResolveException(source + " " + PR_MST_NMBR);
                            }
                            array.add(toInt(it.toString()));
                        }
                    } else {
                        if(!isInteger(parameterValue)) {
                            throw new ParameterResolveException(source + " " + PR_MST_NMBR);
                        }
                        array.add(toInt(parameterValue.toString()));
                    }
                }
                generatedMap.put(key, array);
                break;

            case TYPE_LONG_ARRAY:
                List<Long> longArray = new ArrayList<>();
                if(parameterValue != null) {
                    if(parameterValue instanceof List) {
                        for(Object it : (List)parameterValue){
                            if(!isLong(it)) {
                                throw new ParameterResolveException(source + " " + PR_MST_NMBR);
                            }
                            longArray.add(toLong(it.toString()));
                        }
                    } else {
                        if(!isLong(parameterValue)) {
                            throw new ParameterResolveException(source + " " + PR_MST_NMBR);
                        }
                        longArray.add(toLong(parameterValue.toString()));
                    }
                }
                generatedMap.put(key, longArray);
                break;

            case TYPE_STRING_ARRAY:
                List<String> strArray = new ArrayList<>();
                if(parameterValue != null) {
                    if(parameterValue instanceof List) {
                        for(Object it : (List)parameterValue){
                            strArray.add(it.toString());
                        }
                    } else {
                        strArray.add(parameterValue.toString());
                    }
                }
                generatedMap.put(key, strArray);
                break;

            case TYPE_OBJECT:
                Map generatedObjectResponse = new HashMap();
                for(Object it : (List)templateData.get(PARAMETERS))
                    resolveResponseRecursively((Map)parameterValue, (Map)it, generatedObjectResponse);

                generatedMap.put(key, generatedObjectResponse);
                break;

            case TYPE_OBJECT_ARRAY:
                List<Object> objects = new ArrayList<>();
                Map parameter = (Map) parameterValue;
                if(!isNullObject(parameter)) {
                    String childName = templateData.get(TYPE_OBJECT_CHILDNAME).toString();
                    if(!(templateData.get(PARAMETERS) instanceof List)) {
                        throw new ParameterResolveException(source + " " + PR_MST_LST);
                    }
                    if(!isNullObject(childName)) {
                        for(Object objectDataMap : (List)parameter.get(childName)){
                            Map generatedObjectesp = new HashMap();

                            for(Object objectTemplateElement : (List)templateData.get(PARAMETERS))
                                resolveResponseRecursively((Map)objectDataMap, (Map)objectTemplateElement, generatedObjectesp);

                            Map childResponse = new HashMap();
                            childResponse.put(childName, generatedObjectesp) ;
                            objects.add(childResponse);
                        }
                    } else {
                        for(Object objectDataMap : (List)parameterValue){
                            generatedObjectResponse = new HashMap();
                            for(Object objectTemplateElement : (List)templateData.get(PARAMETERS))
                                resolveResponseRecursively((Map)objectDataMap, (Map)objectTemplateElement, generatedObjectResponse);

                            objects.add(generatedObjectResponse);
                        }
                    }
                }

                generatedMap.put(key, objects);
                break;
            default:
                throw new ParameterResolveException(source + " " + UNKNOWN_TYPE + " " + type);
        }

    }

}
