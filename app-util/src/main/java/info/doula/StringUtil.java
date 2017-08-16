package info.doula;

import java.util.ArrayList;
import java.util.List;

/**
 * StringUtil
 *
 * @author Sekhar Akkisetti <ts-sekhar.akkisetti@rakuten.com>
 *
 */
public class StringUtil {
	public static String nullToBlank(String s) {
		if (s == null) {
			return "";
		}
		return s;
	}

	public static String xmlEscape(String s) {
		if (s == null || s.equals("")) {
			return s;
		}
		StringBuffer sb = new StringBuffer();
		char[] array = s.toCharArray();
		for (int i = 0; i < array.length ; i++) {
			switch (array[i]) {
			case '&':
				sb.append("&amp;");
				break;
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '"':
				sb.append("&quot;");
				break;
			case '\'':
				sb.append("&#39;");
				break;
			default:
				sb.append(array[i]);
			}
		}

		return sb.toString();
	}

    /**
     * 文字列が空かどうか判断します。<br>
     * nullと長さ０の文字列のときにtrueを返すメソッドです。
     * 長さを測る前にtrim()を実行するのでスペースだけの場合もtrueになります。
     * @param s
     * @return
     */
    public static final boolean isEmpty(String s){
        return (s == null || s.trim().length() == 0);
    }

   /*
    * 文字列を特定文字列で分割し、リストを作成します。<br>
    * valueがdelimiterで区切れなかった場合には、valueだけが入ったListを作ります。
    * 区切り文字の指定が、nullもしくは長さ0の文字列の場合も分割できないのでvalueだけが入ったListを返します
    *
    * @param value 分割対象の文字列
    * @param delimiter 区切り文字
    * @param outputEmpty trueならば分割したものが""でも出力
    * @return 分割した文字列のリスト
    */
    public static List<String> split(String value, String delimiter, boolean outputEmpty){
    	List<String> l = new ArrayList<String>();

    	//valueとdelimiterがきちんと指定された場合だけやります。delimiter=""は終わらなくなるので禁止
    	if( value != null && delimiter != null && delimiter.length() != 0){
    		int delimLength = delimiter.length();
    		int fromIndex = 0;
    		int index = -1;

    		//myValueはvalueの後ろにデリミタを追加した文字列（最後の一個がうまく取れないので）
    		String myValue = value;
    		if(!value.endsWith(delimiter)){
    			myValue = value + delimiter;
    		}

    		while((index = myValue.indexOf(delimiter, fromIndex)) >= 0){
    			String s = myValue.substring(fromIndex,index);
    			fromIndex = fromIndex + s.length() + delimLength;

    			//空っぽ要素の出力OKなら確認せずに追加
    			//空っぽ要素の出力NGなら確認してから追加
    			if(outputEmpty || !isEmpty(s)){
    				l.add(s);
    			}
    		}
    	}

    	//終わってみて、返すものが何もない場合には、引数valueをそのままを入れたリストを作る
    	if(l.isEmpty() && value != null){
    		if(outputEmpty || ! isEmpty(value)){
    			l.add(value);
    		}
    	}

    	return l;
    }

   /*
    * JSONで返す値の文字列に対して、正常に返すためにエスケープ処理します。<br>
    * 特定の記号文字を置換することでエスケープします。
    *
    * @deprecated Do not use this one, because this method is not standard way. Please use JSON-lib
    * @param string エスケープ対象の文字列
    * @return エスケープ処理した文字列
    */
	public static String jsonEscape(String string) {
		if (string == null || string.equals("")) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		char[] array = string.toCharArray();
		for (int index = 0; index < array.length ; index++) {
			switch (array[index]) {
			case '[':
				sb.append("［");
				break;
			case ']':
				sb.append("］");
				break;
			default:
				sb.append(array[index]);
			}
		}

		return sb.toString();
	}


}
