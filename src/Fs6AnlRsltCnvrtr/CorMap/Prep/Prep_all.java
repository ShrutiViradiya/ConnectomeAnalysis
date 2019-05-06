package Fs6AnlRsltCnvrtr.CorMap.Prep;

/**
 * 体積データの並び替え
 * DATAをConvertするクラス。
 * 症例ごと⇒脳領域毎 データにする。
 * Created by issey on 2017/01/26.
 */
public class Prep_all {

    public static void main(String[] args) {

        /**
         * Before and After
         */
        args = new String[]{
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\Data\\FS6_ANL_RSLT_20170601",
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\ConvertedData\\surf+deep",
                "true", "true", "false", "false"};
        Prep2_Core.main(args);

        args = new String[]{
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\Data\\FS6_ANL_RSLT_20170601",
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\ConvertedData\\surf",
                "true", "false", "false", "false"};
        Prep2_Core.main(args);

        args = new String[]{
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\Data\\FS6_ANL_RSLT_20170601",
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\ConvertedData\\deep",
                "false", "true", "false", "false"};
        Prep2_Core.main(args);

        //args = new String[]{
        //        "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\Data\\FS6_ANL_RSLT_20170601",
        //        "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\ConvertedData\\hipposub",
        //        "false", "false", "true", "false"};
        //Prep2_Core.main(args);

        args = new String[]{
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\Data\\FS6_ANL_RSLT_20170601",
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\ConvertedData\\6nodes",
                "false", "false", "false", "true"};
        Prep2_Core.main(args);


        /**
         * Gene
         */
        args = new String[]{
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\Data\\FS6_ANL_RSLT_20170525",
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\ConvertedData\\surf+deep",
                "true", "true", "false", "false"};
        Prep2_Core.main(args);


        /**
         * Kuma
         */
        args = new String[]{
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\Data\\FS6_ANL_RSLT_20171124",
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\ConvertedData\\surf+deep",
                "true", "true", "false", "false"};
        Prep2_Core.main(args);

    }

}
