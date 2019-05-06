package Fs6AnlRsltCnvrtr.ForSimpleSummary;

import Fs6AnlRsltCnvrtr.ForVolCorNet.ForVolCorNet_v2;

/**
 * Created by issey on 2017/01/26.
 */
public class Starter {

    public static void main(String[] args){

        args = new String[]{
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\VolCon_v5.00\\Data\\20161124_FS6_ANL_RSLT_ALL",
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\VolCon_v5.00\\ConvertedData\\surf+deep",
                "true", "true", "false"};
        ForVolCorNet_v2.main(args);

        args = new String[]{
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\VolCon_v5.00\\Data\\20161124_FS6_ANL_RSLT_ALL",
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\VolCon_v5.00\\ConvertedData\\surf",
                "true", "false", "false"};
        ForVolCorNet_v2.main(args);

        args = new String[]{
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\VolCon_v5.00\\Data\\20161124_FS6_ANL_RSLT_ALL",
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\VolCon_v5.00\\ConvertedData\\deep",
                "false", "true", "flse"};
        ForVolCorNet_v2.main(args);


        args = new String[]{
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\VolCon_v5.00\\Data\\20161124_FS6_ANL_RSLT_ALL",
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\VolCon_v5.00\\ConvertedData\\hipposub",
                "false", "false", "true"};
        ForVolCorNet_v2.main(args);
    }

}
