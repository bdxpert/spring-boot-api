package info.doula.logic.impl;

import com.google.common.collect.Maps;
import info.doula.exception.ParameterResolveException;
import info.doula.logic.ApiParameterResolver;
import info.doula.util.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static info.doula.entity.JsonAttributes.*;
import static info.doula.util.NumberUtils.toInt;

/**
 * Created by tasnim on 6/11/2017.
 */
@Component("apiParameterResolver")
public class ApiParameterResolverImpl implements ApiParameterResolver {


    /**
     * Resolve request map
     *
     * If requestTemplateMap has fastforward is true then, it will return actual httpRequest
     * It checks actual httpRequest with requestTemplateMap as mentioned json file and prepare the request accordingly
     *
     * @see JsonAtrributes class for json attributes
     *
     * @param dataMap
     * @param requestTemplateMap
     * @return
     */
    @Override
    public LinkedHashMap<String, ?> resolveRequestParameter(Map<String, Object> dataMap,  Map<String, Object> jsonTemplateMap)
        throws ParameterResolveException {

        LinkedHashMap<String, ?> actualRequest = dataMap.get("request") != null?
                (LinkedHashMap<String, ?>) dataMap.get("request") : Maps.newLinkedHashMap();
        Map requestTemplateMap = dataMap.get(REQUEST) != null? (Map)dataMap.get(REQUEST) : Collections.emptyMap();

        if(requestTemplateMap.get(FAST_FORWARD).toString().equals("true")) {
            return actualRequest;
        }

        Map generatedMap = new HashMap();
        List templateParameterMap = requestTemplateMap.get(PARAMETERS) != null ?
                (ArrayList)requestTemplateMap.get(PARAMETERS) : Collections.emptyList();

        if(!(templateParameterMap instanceof List)) {
            throw new ParameterResolveException("invalid request parameters in json configuration file. request parameters should be list");
        }

        for(Object templateParameter : templateParameterMap){
            resolveRequestRecursively(actualRequest, it, generatedMap, dataMap);
        }

        return generatedMap;

    }


    /**
     * Reslove request checks actual httpRequest with requestTemplateMap as mentioned json file and prepare the request accordingly
     * @param requestMap
     * @param jsonTemplateMap
     * @param serviceName
     * @param operationName
     * @param versionNumber
     * @return
     */
    private void resolveRequestRecursively(def requestMap, Map templateData, def generatedRequestMap, def dataMap) {

        String key = templateData.get(NAME).toString();
        String type = templateData.get(TYPE).toString();
        String source = templateData.get(SOURCE) != null ? "": key;

        switch(type) {

            case TYPE_BOOLEAN:
                def parameterValue = getParameterValue(templateData, source, requestMap);
                if(parameterValue != null) {
                    boolean value = false;
                    if(parameterValue?.toString().toLowerCase() == "true") {
                        value = true;
                    } else if (parameterValue?.toString().isNumber()) {
                        value = parameterValue.toString().toInteger() > 0;
                    }
                    generatedRequestMap.put(key, value);
                }
                break;

            case TYPE_INT:
            case TYPE_INTEGER:
                def parameterValue = getParameterValue(templateData, source, requestMap);
                if(parameterValue != null) {
                    if(!isInteger(parameterValue)) {
                        throw new ParameterResolveException("${source} parameter should be integer")
                    }
                    int givenValue = parameterValue as int
                    def maxValue = templateData.get(MAX_VALUE) != null ? templateData.get(MAX_VALUE) as int : null
                    def minValue = templateData.get(MIN_VALUE) != null ? templateData.get(MIN_VALUE) as int : null

                    checkMaxMinValue(maxValue, minValue, source, givenValue)
                    generatedRequestMap.put(key, givenValue)
                }
                break;

            case TYPE_LONG:
                def parameterValue = getParameterValue(templateData, source, requestMap)
                if(parameterValue != null) {
                    if(!isLong(parameterValue)) {
                        throw new ParameterResolveException("${source} parameter should be long")
                    }
                    long givenValue = parameterValue as long
                    def maxValue = templateData.get(MAX_VALUE) != null ? templateData.get(MAX_VALUE) as long : null
                    def minValue = templateData.get(MIN_VALUE) != null ? templateData.get(MIN_VALUE) as long : null
                    checkMaxMinValue(maxValue, minValue, source, givenValue)
                    generatedRequestMap.put(key, givenValue)
                }
                break;

            case TYPE_DECIMAL:
                def parameterValue = getParameterValue(templateData, source, requestMap)
                if(parameterValue != null) {
                    if(!parameterValue.toString().isBigDecimal()) {
                        throw new ParameterResolveException("${source} parameter should be decimal")
                    }
                    doValidatePattern(parameterValue, templateData)
                    BigDecimal givenValue = parameterValue as BigDecimal
                    def maxValue = templateData.get(MAX_VALUE) != null ? templateData.get(MAX_VALUE) as BigDecimal : null
                    def minValue = templateData.get(MIN_VALUE) != null ? templateData.get(MIN_VALUE) as BigDecimal : null
                    checkMaxMinValue(maxValue, minValue, source, givenValue)
                    generatedRequestMap.put(key, givenValue)
                }
                break;

            case TYPE_OPTION:
                def parameterValue = getParameterValue(templateData, source, requestMap)
                if(parameterValue != null) {
                    def values = templateData.get(OPTION)
                    if(!values) {
                        throw new ParameterResolveException("${source} options should not be null/blank");
                    }
                    if(!values.any { it == parameterValue}) {
                        throw new ParameterResolveException("set ${source} from ${values.join(',')}");
                    }
                    generatedRequestMap.put(key, parameterValue);
                }
                break;

            case TYPE_INT_OPTION:
                def parameterValue = getParameterValue(templateData, source, requestMap);
                if(parameterValue != null) {
                    if(!isInteger(parameterValue)) {
                        throw new ParameterResolveException("${source} parameter should be integer");
                    }
                    int givenValue = parameterValue as int
                    def values = templateData.get(OPTION);
                    if(!values) {
                        throw new ParameterResolveException("${source} options should not be null/blank");
                    }
                    if(!values.any { it == givenValue}) {
                        throw new ParameterResolveException("set ${source} from ${values.join(',')}");
                    }
                    generatedRequestMap.put(key, givenValue);
                }
                break;

            case TYPE_STRING:
                String parameterValue = getParameterValue(templateData, source, requestMap);
                if(parameterValue) {
                    doValidatePattern(parameterValue, templateData);
                    checkMaxMinStringLength(templateData, source, parameterValue);
                    generatedRequestMap.put(key, parameterValue);
                }
                break;

            case TYPE_FIXED:
                generatedRequestMap.put(key, templateData.get(VALUE));
                break;

            case TYPE_INT_ARRAY:
            case TYPE_INTEGER_ARRAY:
                List parameterValues = getParameterValue(templateData, source, requestMap);

                if(parameterValues != null) {
                    List array = new ArrayList();
                    List valuesArray = new ArrayList();

                    if(parameterValues instanceof List) {
                        array = parameterValues;
                    } else {
                        array.addAll(parameterValues);
                    }

                    if(templateData.get(MAX_SIZE) != null) {
                        int maxSize = templateData.get(MAX_SIZE) as int
                        if(array.size() > maxSize) {
                            throw new ParameterResolveException("${source} array size must be under ${maxSize}")
                        }
                    }

                    array.each {
                        if(!isInteger(it)) {
                            throw new ParameterResolveException("${source} parameter should be numbers")
                        }
                        int parameterValue = it as int
                        def maxValue = templateData.get(MAX_VALUE)
                        def minValue = templateData.get(MIN_VALUE)

                        if(maxValue && isNotUnderMaxValue(parameterValue, maxValue as int)) {
                            throw new ParameterResolveException("all ${source}'s must be under ${maxValue}")
                        }
                        if(minValue && isNotOverMinValue(parameterValue, minValue as int)) {
                            throw new ParameterResolveException("all ${source}'s must be over ${minValue}")
                        }
                        valuesArray += parameterValue
                    }
                    generatedRequestMap.put(key, valuesArray);
                }
                break;

            case TYPE_LONG_ARRAY:
                parameterValues = getParameterValue(templateData, source, requestMap);
                if(parameterValues != null) {

                    List array = new ArrayList();
                    if(parameterValues instanceof List) {
                        array = parameterValues;
                    } else {
                        array.addAll(parameterValues);
                    }
                    if(templateData.get(MAX_SIZE) != null) {
                        int maxSize = templateData.get(MAX_SIZE) as int
                        if(array.size() > maxSize) {
                            throw new ParameterResolveException("${source} array size must be under ${maxSize}")
                        }
                    }

                    def valuesArray = []
                    array.each {
                        if(!isLong(it)) {
                            throw new ParameterResolveException("${source} parameter should be numbers")
                        }
                        long parameterValue = it as long
                        def maxValue = templateData.get(MAX_VALUE)
                        def minValue = templateData.get(MIN_VALUE)

                        if(maxValue && isNotUnderMaxValue(parameterValue, maxValue as long)) {
                            throw new ParameterResolveException("all ${source}'s must be under ${maxValue}")
                        }
                        if(minValue && isNotOverMinValue(parameterValue, minValue as long)) {
                            throw new ParameterResolveException("all ${source}'s must be over ${minValue}")
                        }
                        valuesArray += parameterValue
                    }
                    generatedRequestMap.put(key, valuesArray)
                }
                break

            case TYPE_STRING_ARRAY:
                def parameterValues = getParameterValue(templateData, source, requestMap)

                if(parameterValues != null) {

                    def array = []

                    if((parameterValues instanceof List) || (parameterValues instanceof String[])) {
                        for (String parameterValue : parameterValues) {
                            array+=parameterValue
                        }
                    } else {
                        array += parameterValues
                    }

                    if(templateData.get(MAX_SIZE) != null) {
                        int maxSize = templateData.get(MAX_SIZE) as int
                        if(array.size() > maxSize) {
                            throw new ParameterResolveException("${source} array size must be under ${maxSize}")
                        }
                    }

                    def valuesArray = []
                    array.each {
                        String value = it
                        String pattern = templateData.get(PATTERN)
                        if (pattern != null && value !=null && !value.isEmpty()) {
                            if (!(value ==~ pattern)) {
                                throw new ParameterResolveException("all ${source}'s must be follow \"${pattern}\"");
                            }
                        }

                        def maxLength = templateData.get(MAX_LENGTH)
                        def minLength = templateData.get(MIN_LENGTH)
                        if(maxLength && value?.length() > (maxLength as int)) {
                            throw new ParameterResolveException("all ${source}'s length must be under ${maxLength}")
                        }
                        if(minLength && value?.length() < (minLength as int)) {
                            throw new ParameterResolveException("all ${source}'s length must be over ${minLength}")
                        }
                        valuesArray += value
                    }
                    generatedRequestMap.put(key, valuesArray)
                }
                break

            case TYPE_OBJECT:
                def parameterValue = getParameterValue(templateData, source, requestMap)
                if(parameterValue != null) {
                    def generatedObjectMap = [:]
                    if(!(templateData.get(PARAMETERS) instanceof List)) {
                        throw new ParameterResolveException("${source} parameters must be list")
                    }
                    templateData.get(PARAMETERS)?.each {
                        resolveRequestRecursively(parameterValue, it, generatedObjectMap)
                    }
                    generatedRequestMap.put(key, generatedObjectMap)
                }
                break

            case TYPE_OBJECT_ARRAY:
                def parameterValue = getParameterValue(templateData, source, requestMap)

                if(parameterValue != null) {
                    def array = []
                    String childName = templateData.get(TYPE_OBJECT_CHILDNAME)
                    if(!(templateData.get(PARAMETERS) instanceof List)) {
                        throw new ParameterResolveException("${source} parameters must be list")
                    }
                    if(childName != null) {
                        parameterValue[childName]?.each { objectDataMap ->
                                def generatedObjectResponse = [:]
                            templateData.get(PARAMETERS)?.each { objectTemplateElement ->
                                    resolveRequestRecursively(objectDataMap, objectTemplateElement, generatedObjectResponse)
                            }
                            def childResponse = [:]
                            childResponse[childName] = generatedObjectResponse
                            array += childResponse
                        }
                    } else {
                        parameterValue?.each { objectDataMap ->
                                def generatedObjectResponse = [:]
                            templateData.get(PARAMETERS)?.each { objectTemplateElement ->
                                    resolveRequestRecursively(objectDataMap, objectTemplateElement, generatedObjectResponse)
                            }
                            array += generatedObjectResponse
                        }
                    }
                    generatedRequestMap.put(key, array)
                }

                // Source for the value of the parameter with type=clientid is X-ClientId header
            case TYPE_CLIENTID:
                boolean isRequired = templateData.get(REQUIRED)?.toString()?.toLowerCase() == "true";
                if(isRequired && (isNull(dataMap.clientId) || StringUtils.isBlank(dataMap.clientId as String))) {
                    throw new ParameterResolveException("X-ClientId header is required")
                }
                generatedRequestMap.put(key, dataMap?.clientId);
                break;

            case TYPE_SERVICE:
                generatedRequestMap.put(key, dataMap?.service);
                break;

            case TYPE_OPERATION:
                generatedRequestMap.put(key, dataMap?.operation);
                break;

            case TYPE_VERSION:
                generatedRequestMap.put(key, dataMap?.version);
                break;

            default:
                throw new ParameterResolveException("${source} unknown type ${type}");
        }

    }


    /**
     * Fetch parameterValue from the actual request,
     * if the value is not exist in actual request then it checks whether that filed is required for not,
     * if field is required then throws exception, if filed is not required & default present in json file then it will return default value
     * @param parameterData
     * @param key
     * @param requestMap
     * @return parameterValue
     */
    private Object getParameterValue(Map templateData, String datasource, Map requestMap) throws ParameterResolveException {

        Object parameterValue =  requestMap.get(datasource);

        boolean isRequired = templateData.get(REQUIRED).toString().toLowerCase().equals("true");
        if(isRequired) {
            if(isNull(parameterValue) || StringUtils.isBlank(parameterValue.toString())) {
                throw new ParameterResolveException("${datasource} parameter should not be null/blank");
            }
        } else {
            if(isNull(parameterValue) || StringUtils.isBlank(parameterValue.toString())) {
                parameterValue = templateData.get(DEFAULT);
            }
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
        if (object == null) {
            return true;
        } else if (object instanceof JSONObject && ((JSONObject) object).isNullObject()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check minValue and maxValue only if minValue and maxValue attibutes exists in json file for corresponding field
     * @param maxValue
     * @param minValue
     * @param key
     * @param givenValue
     */
    private void checkMaxMinValue(def maxValue, def minValue, String key, def givenValue) throws ParameterResolveException {

        if(maxValue != null && isNotUnderMaxValue(givenValue, maxValue)) {
            throw new ParameterResolveException("${key} must be under ${maxValue}");
        }

        if(minValue != null && isNotOverMinValue(givenValue, minValue)) {
            throw new ParameterResolveException("${key} must be over ${minValue}");
        }

    }

    /**
     * Check givenValue is over the maxValue or not
     * @param givenValue
     * @param maxValue
     * @return true if the givenValue is over the maxValue
     */
    private boolean isNotUnderMaxValue(int givenValue, int maxValue) {
        return (givenValue > maxValue);
    }

    /**
     * Check givenValue is under the minValue or not
     * @param givenValue
     * @param minValue
     * @return true if the givenValue is under minValue
     */
    private boolean isNotOverMinValue(int givenValue, int minValue) {
        return (givenValue < minValue);
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
        throws ParameterResolveException{

        Object maxLength = templateData.get(MAX_LENGTH);
        Object minLength = templateData.get(MIN_LENGTH);
        if(maxLength != null && givenValue.length() > (toInt(maxLength))) {
            throw new ParameterResolveException("${key} length must be under ${maxLength}");
        }
        if(minLength != null && givenValue.length() < (toInt(minLength) )) {
            throw new ParameterResolveException("${key} length must be over ${minLength}");
        }
    }

    /**
     * This will check pattern only pattern attibute exists in json file for corresponding field
     * @param origin
     * @param targetNode
     */
    private void doValidatePattern(def origin, def templateData) {

        String pattern = templateData.get(PATTERN)
        if (pattern != null && origin !=null && !origin.toString().isEmpty()) {
            if (!((origin as String) ==~ pattern)) {
                throw new ParameterResolveException("${templateData.get(SOURCE) ?:templateData.get(NAME)} must be follow \"${pattern}\"");
            }
        }
    }


    /**
     * Resolve response parameters recursively
     * checks actual response with templateParameterMap as mentioned json file and prepare the response accordingly
     * @param actualDataMap
     * @param templateParameterMap
     * @return generatedMap
     */
    public LinkedHashMap<String, ?> resolveResponseParameter(Map<String, Object> response,  Map<String, Object> jsonTemplateMap) {

        def actualResponse = response ?: [:]
        def responseMap = jsonTemplateMap?.get(RESPONSE)?: [:]

        if(responseMap.get(FAST_FORWARD)?.toString() == "true") {
            return actualResponse
        }
        def generatedMap = [:]

        def templateParameterMap = responseMap.get(PARAMETERS) ?: []
        if(!(templateParameterMap instanceof List)) {
            throw new ParameterResolveException("invalid response parameters configuration in json. response parameters should be list");
        }
        templateParameterMap.each {
            resolveResponseRecursively(actualResponse, it, generatedMap)
        }

        return generatedMap
    }

    /**
     * Resolve response parameters recursively
     * checks actual response with templateParameterMap as mentioned json file and prepare the response accordingly
     * @param actualDataMap
     * @param templateParameterMap
     * @param generatedMap
     */
    private void resolveResponseRecursively(def actualDataMap, def templateData, def generatedMap) {

        def reslovedRequestMap = [:]
        String key = templateData.get(NAME)
        String type = templateData.get(TYPE)
        String source = templateData.get(SOURCE) ?: key
        def parameterValue =  actualDataMap?.get(source)

        switch(type) {

            case TYPE_BOOLEAN:
                boolean convertedValue =  false
                if(parameterValue?.toString().toLowerCase() == "true") {
                convertedValue = true
            } else if (parameterValue?.toString().isNumber() && parameterValue.toString().toInteger() > 0) {
                convertedValue = true
            }
            generatedMap.put(key, convertedValue)
            break;

            case TYPE_INT:
            case TYPE_INTEGER:
                int value = 0
                if(parameterValue) {
                    if(!isInteger(parameterValue)) {
                        throw new ParameterResolveException("${source} parameter should be int")
                    }
                    value = parameterValue as int
                }
                generatedMap.put(key, value)
                break;

            case TYPE_LONG:
                long value = 0
                if(parameterValue) {
                    if(!isLong(parameterValue)) {
                        throw new ParameterResolveException("${source} parameter should be long")
                    }
                    value = parameterValue as long
                }
                generatedMap.put(key, value)
                break;

            case TYPE_DECIMAL:
                BigDecimal value = new BigDecimal(0.0)
                if(parameterValue) {
                    if(!parameterValue.toString().isBigDecimal()) {
                        throw new ParameterResolveException("${source} parameter should be decimal")
                    }
                    value = parameterValue as BigDecimal
                }
                generatedMap.put(key, value)
                break;

            case TYPE_OPTION:
                if(parameterValue != null) {
                    def values = templateData.get(OPTION)
                    if(!values) {
                        throw new ParameterResolveException("${source} options should not be null/blank")
                    }
                    if(!values.any { it == parameterValue}) {
                        throw new ParameterResolveException("set ${source} from ${values.join(',')}")
                    }
                }
                generatedMap.put(key, parameterValue)
                break

            case TYPE_STRING:
                generatedMap.put(key, parameterValue as String)
                break

            case TYPE_FIXED:
                generatedMap.put(key, templateData.get(VALUE))
                break

            case TYPE_INT_ARRAY:
            case TYPE_INTEGER_ARRAY:
                def array = []
                if(parameterValue != null) {
                    if(parameterValue instanceof List) {
                        parameterValue.each {
                            if(!isInteger(it)) {
                                throw new ParameterResolveException("${source} parameter should be numbers")
                            }
                            array += it as int
                        }
                    } else {
                        if(!isInteger(parameterValue)) {
                            throw new ParameterResolveException("${source} parameter should be numbers")
                        }
                        array += parameterValue as int
                    }
                }
                generatedMap.put(key, array)
                break

            case TYPE_LONG_ARRAY:
                def array = []
                if(parameterValue != null) {
                    if(parameterValue instanceof List) {
                        parameterValue.each {
                            if(!isLong(it)) {
                                throw new ParameterResolveException("${source} parameter should be numbers")
                            }
                            array += it as long
                        }
                    } else {
                        if(!isLong(parameterValue)) {
                            throw new ParameterResolveException("${source} parameter should be numbers")
                        }
                        array += parameterValue as long
                    }
                }
                generatedMap.put(key, array)
                break

            case TYPE_STRING_ARRAY:
                def array = []
                if(parameterValue != null) {
                    if(parameterValue instanceof List) {
                        parameterValue.each {
                            array += it as String
                        }
                    } else {
                        array += parameterValue as String
                    }
                }
                generatedMap.put(key, array)
                break

            case TYPE_OBJECT:
                def generatedObjectResponse = [:]
                templateData.get(PARAMETERS)?.each {
                resolveResponseRecursively(parameterValue, it, generatedObjectResponse)
            }
            generatedMap.put(key, generatedObjectResponse)
            break

            case TYPE_OBJECT_ARRAY:
                def array = []
                if(parameterValue != null) {
                    String childName = templateData.get(TYPE_OBJECT_CHILDNAME)
                    if(!(templateData.get(PARAMETERS) instanceof List)) {
                        throw new ParameterResolveException("${source} parameters must be list")
                    }
                    if(childName != null) {
                        parameterValue[childName]?.each { objectDataMap ->
                                def generatedObjectResponse = [:]
                            templateData.get(PARAMETERS)?.each { objectTemplateElement ->
                                    resolveResponseRecursively(objectDataMap, objectTemplateElement, generatedObjectResponse)
                            }
                            def childResponse = [:]
                            childResponse[childName] = generatedObjectResponse
                            array += childResponse
                        }
                    } else {
                        parameterValue?.each { objectDataMap ->
                                def generatedObjectResponse = [:]
                            templateData.get(PARAMETERS)?.each { objectTemplateElement ->
                                    resolveResponseRecursively(objectDataMap, objectTemplateElement, generatedObjectResponse)
                            }
                            array += generatedObjectResponse
                        }
                    }
                }
                generatedMap.put(key, array);
                break;

            default:
                throw new ParameterResolveException("${source} unknown type ${type}");
        }

    }

}
