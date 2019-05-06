package Fs6AnlRsltCnvrtr.Garbage;

import Fs6AnlRsltCnvrtr.ForVolCorNet.Prep2_Core;

/**
 * Created by issey on 2017/01/26.
 */
public class Starter_for_Sugimoto {

    public static void main(String[] args){

        args = new String[]{
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.02\\Data\\FS6_ANL_RSLT_20170525",
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.02\\ConvertedData\\surf+deep",
                "true", "true", "false"};
        Prep2_Core.main(args);

        args = new String[]{
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.02\\Data\\FS6_ANL_RSLT_20170525",
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.02\\ConvertedData\\surf",
                "true", "false", "false"};
        Prep2_Core.main(args);

        args = new String[]{
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.02\\Data\\FS6_ANL_RSLT_20170525",
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.02\\ConvertedData\\deep",
                "false", "true", "flse"};
        Prep2_Core.main(args);


        args = new String[]{
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.02\\Data\\FS6_ANL_RSLT_20170525",
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.02\\ConvertedData\\hipposub",
                "false", "false", "true"};
        Prep2_Core.main(args);
    }

}
