package Fs6AnlRsltCnvrtr1st.Converter;

/**
 * 体積データの並び替え
 * DATAをConvertするクラス。
 * 症例ごと⇒脳領域毎 データにする。
 * Created by issey on 2017/01/26.
 */
public class Prep_all {

    static final String ROOT_FLD_PATH = "C:\\Users\\issey\\Documents\\Dropbox\\NetbeansProjects_xps14z\\ConnectomeAnalysis\\src\\test\\java\\CorMapper_v1.06\\";

    public static void main(String[] args) {
        /**
         * Gene
         */
        args = new String[]{
                ROOT_FLD_PATH + "ProfileData_orig\\SubjectProfile.txt",
                ROOT_FLD_PATH + ROOT_FLD_PATH + "ProfileData_converted",
                ""};
        Prep2_CreateNewDemographicFile.main(args);

        //args = new String[]{
        //        "ProfileData_orig\\Profile_COMTV108_158M.txt",
        //        ROOT_FLD_PATH + "ProfileData_converted",
        //        ""};
        //Prep2_CreateNewDemographicFile.main(args);

        //args = new String[]{
        //        "ProfileData_orig\\Profile_NAT_G1287A.txt",
        //        ROOT_FLD_PATH + "ProfileData_converted",
        //        ""};
        //Prep2_CreateNewDemographicFile.main(args);

        //args = new String[]{
        //        "ProfileData_orig\\Profile_NAT_T182C.txt",
        //        ROOT_FLD_PATH + "ProfileData_converted",
        //        ""};
        //Prep2_CreateNewDemographicFile.main(args);

        args = new String[]{
                ROOT_FLD_PATH + "StatsData_orig\\MDDGENE",
                ROOT_FLD_PATH + "StatsData_converted\\MDDGENE\\surf+deep",
                "true", "true", "false", "false"};
        Prep3_DataExtractionFromStatsFiles.main(args);

        //args = new String[]{
        //        ROOT_FLD_PATH + "StatsData_converted\\MDDGENE\\surf+deep\\DataOrderInfo.txt",
        //        ROOT_FLD_PATH + "ProfileData_converted\\Profile_with_givenid.txt",
        //        "RCode\\MDDGENE\\BDNF"};
        //Prep4_CreateConstants.main(args);

        //args = new String[]{
        //        ROOT_FLD_PATH + "StatsData_converted\\MDDGENE\\surf+deep\\DataOrderInfo.txt",
        //        ROOT_FLD_PATH + "ProfileData_converted\\Profile_COMTV108_158M_with_givenid.txt",
        //        "RCode\\MDDGENE\\COMT"};
        //Prep4_CreateConstants.main(args);

        //args = new String[]{
        //        ROOT_FLD_PATH + "StatsData_converted\\MDDGENE\\surf+deep\\DataOrderInfo.txt",
        //        ROOT_FLD_PATH + "ProfileData_converted\\Profile_NAT_G1287A_with_givenid.txt",
        //        "RCode\\MDDGENE\\G1287A"};
        //Prep4_CreateConstants.main(args);

        //args = new String[]{
        //        ROOT_FLD_PATH + "StatsData_converted\\MDDGENE\\surf+deep\\DataOrderInfo.txt",
        //        ROOT_FLD_PATH + "ProfileData_converted\\Profile_NAT_T182C_with_givenid.txt",
        //        "RCode\\MDDGENE\\T182C"};
        //Prep4_CreateConstants.main(args);

        args = new String[]{
                "RCode\\MDDGENE\\BDNF"};
        //CorMapper_v5.Fs6AnlRsltCnvrtr.Converter.Prep5.main(args);

        //args = new String[]{
        //        "RCode\\MDDGENE\\COMT"};
        //Prep5.main(args);

        //args = new String[]{
        //        "RCode\\MDDGENE\\G1287A"};
        //Prep5.main(args);

        //args = new String[]{
        //        "RCode\\MDDGENE\\T182C"};
        //Prep5.main(args);

        /**
         * Kuma
         */
        //args = new String[]{
        //"ProfileData_orig\\Profile_KumaMCI.txt",
        //        ROOT_FLD_PATH + "ProfileData_converted",
        //        ""};
        //Prep2_CreateNewDemographicFile.main(args);
//
//        args = new String[]{
//                ROOT_FLD_PATH + "StatsData_orig\\KumaMCI",
//                ROOT_FLD_PATH + "StatsData_converted\\KumaMCI\\surf+deep",//stats_data_converted
//                "true", "true", "false", "false"};
//        Prep3_DataExtractionFromStatsFiles.main(args);
//
//        args = new String[]{
//                ROOT_FLD_PATH + "StatsData_converted\\KumaMCI\\surf+deep\\DataOrderInfo.txt",//並び替えたStatsファイルデータの並びの定義ファイル
//                ROOT_FLD_PATH + "ProfileData_converted\\Profile_KumaMCI_with_givenid.txt",//プロファイル
//                "RCode\\KumaMCI"};//RCcodeの所在
//        Prep4_CreateConstants.main(args);
//
//        args = new String[]{
//                "RCode\\KumaMCI"};
//        Prep5.main(args);
//
//        /**
//         * Before and After
//         */
//        //args = new String[]{"H:\\20170601",
//        //        ROOT_FLD_PATH + "StatsData_orig\\MDDBandA"};
//        //Prep1_GatheringStatsFiles.main(args);
//
//        args = new String[]{"ProfileData_orig\\Profile_MDDBandA.txt",
//                ROOT_FLD_PATH + "ProfileData_converted",
//                ROOT_FLD_PATH + "StatsData_orig\\MDDBandA_OrigID_GivenID_Map.txt"};
//        Prep2_CreateNewDemographicFile.main(args);
//
//        args = new String[]{
//                ROOT_FLD_PATH + "StatsData_orig\\MDDBandA",
//                ROOT_FLD_PATH + "StatsData_converted\\MDDBandA\\surf+deep",
//                "true", "true", "false", "false"};
//        Prep3_DataExtractionFromStatsFiles.main(args);
//
//        //args = new String[]{
//        //        ROOT_FLD_PATH + "StatsData_orig\\MDDBandA",
        //        ROOT_FLD_PATH + "StatsData_converted\\MDDBandA\\surf",
        //        "true", "false", "false", "false"};
        //Prep3_DataExtractionFromStatsFiles.main(args);

        //args = new String[]{
        //        ROOT_FLD_PATH + "StatsData_orig\\MDDBandA",
        //        ROOT_FLD_PATH + "StatsData_converted\\MDDBandA\\deep",
        //        "false", "true", "false", "false"};
        //Prep3_DataExtractionFromStatsFiles.main(args);

        //args = new String[]{
        //        ROOT_FLD_PATH + "StatsData_orig\\MDDBandA",
        //        ROOT_FLD_PATH + "StatsData_converted\\MDDBandA\\hipposub",
        //        "false", "false", "true", "false"};
        //Prep3_DataExtractionFromStatsFiles.main(args);

//        args = new String[]{
//                ROOT_FLD_PATH + "StatsData_orig\\MDDBandA",
//                ROOT_FLD_PATH + "StatsData_converted\\MDDBandA\\6nodes",
//                "false", "false", "false", "true"};
//        Prep3_DataExtractionFromStatsFiles.main(args);
//
//        args = new String[]{
//                ROOT_FLD_PATH + "StatsData_converted\\MDDBandA\\surf+deep\\DataOrderInfo.txt",
//                ROOT_FLD_PATH + "ProfileData_converted\\Profile_MDDBandA_with_givenid.txt",
//                "RCode\\MDDBandA"};
//        Prep4_CreateConstants.main(args);
//
//        args = new String[]{
//                "RCode\\MDDBandA"};
//        Prep5.main(args);
//
//
    }

}
