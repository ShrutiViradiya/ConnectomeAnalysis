/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Fs6AnlRsltCnvrtr.Util;

import java.io.File;
import java.io.FilenameFilter;

/**
 *  ファイル名が指定する正規表現のパターンにマッチするファイルを取得します。
 *  source:http://www.syboos.jp/java/doc/listfiles-by-filefilter.html
 * 
 *  <使用例>
 *  File file = new File("c:\\");  
 *  //ファイル名は8つの数字で構成され、拡張子は.htmlのファイルを取得します。  
 *  File[] numberHtmlFiles = file.listFiles(getFileRegexFilter("[0-9]{8}\\.html"));  
 */
public class FileNameFilterConstructor_Regex {

    public static FilenameFilter getRegexFilter(String regex) {
        final String regex_ = regex;
        return new FilenameFilter() {

            @Override
            public boolean accept(File file, String name) {
                boolean ret = name.matches(regex_);
                return ret;
            }
        };
    }
}
