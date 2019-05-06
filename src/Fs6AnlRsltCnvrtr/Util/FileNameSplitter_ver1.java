/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Fs6AnlRsltCnvrtr.Util;

import java.util.regex.Pattern;

/**
 *
 * @author issey
 */
public class FileNameSplitter_ver1 {

    //フィールド
    //コンストラクタ
    //メソッド
    public static String getExtension(String FileNameWithExtension) {
        Pattern splitMark = Pattern.compile("\\.");
        String[] SplitResult = splitMark.split(FileNameWithExtension);

        /*拡張子を分離*/
        String Extension = SplitResult[SplitResult.length - 1];
        return Extension;
    }

    //メソッド
    /*拡張子を分離にはこんな方法もある*/
    private String getExtension2(String filename) {
        int i = filename.lastIndexOf('.');
        if (i > 0 && i < filename.length() - 1){
            return filename.substring(i + 1).toLowerCase();
        };
        return "";
    }

    //メソッド
    public static String getNameOnly(String FileNameWithExtension) {
        Pattern splitMark = Pattern.compile("\\.");
        String[] SplitResult = splitMark.split(FileNameWithExtension);

        /*拡張子以外を分離*/
        String FileName = SplitResult[0];
        for (int c = 1; c < SplitResult.length - 1; c++) {
            FileName = FileName + "." + SplitResult[c];
        }
        return FileName;

    }
}
