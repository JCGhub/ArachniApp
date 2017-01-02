package htmlparser;

import org.apache.commons.lang3.StringEscapeUtils;

public class DataFixer{
	public DataFixer(){}

	public String fix(String str){
		String strCod = StringEscapeUtils.unescapeHtml4(str);			                    
		String strFixed = strCod.replace("\n", "");

		return strFixed;
	}
}