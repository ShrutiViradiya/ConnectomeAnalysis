package Fs6AnlRsltCnvrtr1st.Converter;


import IuFileIO.PathResolver_v1;

import java.io.*;
import java.util.ArrayList;

/**
 * constants.Rの生成クラス
 */
public class Prep4_CreateConstants {

    /**
     * constants.Rを生成する。
     * args[0]: ConvertedData/surf+deep/DataOrderInfo.txtファイルへのパス。何行目に誰のデータが収められているかが記録されている。
     * args[1]: SubjectProfileファイルへのパス。Prep1_1で生成したもの。
     * args[2]: RCodeフォルダへのパス。保存先。
     *
     * @param args
     */
    public static void main(String[] args) {

        //args = new String[]{
        //        "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.05\\StatsData_converted\\KumaMCI\\surf+deep\\DataOrderInfo.txt",//並び替えたStatsファイルデータの並びの定義ファイル
        //        "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.05\\ProfileData_converted\\Profile_KumaMCI_with_givenid.txt",//プロファイル
        //        "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.05\\RCode\\KumaMCI"};//RCcodeの所在

        //args = new String[]{
         //       "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.05\\StatsData_converted\\MDDGENE\\surf+deep\\DataOrderInfo.txt",//並び替えたStatsファイルデータの並びの定義ファイル
           //     "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.05\\ProfileData_converted\\Profile_with_givenid.txt",//プロファイル
             //   "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.05\\RCode\\MDDGENE\\BDNF"};//RCcodeの所在

        Prep4_CreateConstants dc2 = new Prep4_CreateConstants(args[0], args[1], args[2]);
        dc2.makeNewConstantsR();

    }

    ArrayList<String> GivenIdList_in_DemographicDataFile = new ArrayList<>();
    ArrayList<String> AgeList_in_DemographicDataFile = new ArrayList<>();
    ArrayList<String> SexList_in_DemographicDataFile = new ArrayList<>();
    ArrayList<String> GroupingList_in_DemographicDataFile = new ArrayList<>();
    ArrayList<String> GivenIdList_in_DataOrderInfoFile = new ArrayList<>();
    String DataOrderInfoFilePathStr = "";
    String SubjectProfilePathStr = "";
    String RCodeFldPathStr = "";


    /**
     * コンストラクタ
     *
     * @param data_order_info_file_path: DataOrderInfo.txtファイルへのパス。何行目に誰のデータが収められているかが記録されている。
     * @param subject_profile_file_path
     */
    public Prep4_CreateConstants(String data_order_info_file_path, String subject_profile_file_path, String rcode_fld_path) {
        this.RCodeFldPathStr = rcode_fld_path;
        this.DataOrderInfoFilePathStr = data_order_info_file_path;
        this.SubjectProfilePathStr = subject_profile_file_path;

        /**
         * DataOrderInfo.txt 内での症例の行目を確認
         */
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(DataOrderInfoFilePathStr), "UTF-8"));

            String line;
            String[] words;
            while ((line = br.readLine()) != null) {
                GivenIdList_in_DataOrderInfoFile.add(line);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
            }
        }
        System.out.println("GivenIdList_in_DataOrderInfoFile=" + GivenIdList_in_DataOrderInfoFile);

        /**
         * DemographicDataの確認
         */
        GivenIdList_in_DemographicDataFile = new ArrayList<>();
        AgeList_in_DemographicDataFile = new ArrayList<>();
        SexList_in_DemographicDataFile = new ArrayList<>();
        GroupingList_in_DemographicDataFile = new ArrayList<>();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(subject_profile_file_path), "UTF-8"));

            String line;
            String[] words;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                words = line.split("\\t");
                for (int i = 0; i < words.length; i++) {
                    //System.out.print(words[i] + " ");
                }
                //System.out.println();
                GivenIdList_in_DemographicDataFile.add(words[0]);//1列目にID
                AgeList_in_DemographicDataFile.add(words[1]);//2列目にAge
                SexList_in_DemographicDataFile.add(words[2]);//3列目にSex
                GroupingList_in_DemographicDataFile.add(words[3]);//4列目にGrouping
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
            }
        }
        //列名に相当するデータを取り除く
        GivenIdList_in_DemographicDataFile.remove(0);
        AgeList_in_DemographicDataFile.remove(0);
        SexList_in_DemographicDataFile.remove(0);
        GroupingList_in_DemographicDataFile.remove(0);
        System.out.println("GivenIdList_in_DemographicDataFile.size()=" + GivenIdList_in_DemographicDataFile.size());
        System.out.println("AgeList_in_DemographicDataFile.size()=" + AgeList_in_DemographicDataFile.size());
        System.out.println("SexList_in_DemographicDataFile.size()=" + SexList_in_DemographicDataFile.size());
        System.out.println("GroupingList_in_DemographicDataFile.size()=" + GroupingList_in_DemographicDataFile.size());
    }

    /**
     * constans.Rファイルを生成する。
     */
    public void makeNewConstantsR() {

        //出力先の確認
        File save_fld = new File(RCodeFldPathStr);
        save_fld.mkdirs();
        String new_constants_r_file_path = save_fld.getAbsolutePath() + "/" + "0_constants.R";

        //骨格となるオリジナルのconstants.Rファイルへパス
        System.out.println("user.dir: " + System.getProperty("user.dir"));//User working directory
        String wd_path = System.getProperty("user.dir");
        BufferedReader br = null;
        BufferedWriter bw = null;
        String constants_r_file_path = wd_path + "\\src\\NotUseButNotTrash.RemakeData.Fs6AnlRsltCnvrtr\\CorMap\\RCode\\0_constants.R";

        //書き写す
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(constants_r_file_path), "UTF-8"));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new_constants_r_file_path), "UTF-8"));

            String line;
            while ((line = br.readLine()) != null) {
                if (line.endsWith("#----Replaced By Java 1----")) line = getComparisonPattern();
                if (line.endsWith("#----Replaced By Java 2----")) line = getGroupingCode();
                if (line.endsWith("#----Replaced By Java 3----")) line = getOriginalGroupXindexCode();
                if (line.endsWith("#----Replaced By Java 4----"))
                    line = "statsdata_converted <- \"" + getRelativePathStr_ToStatsDataConverted_FromRCode() + "\"";
                if (line.endsWith("#----Replaced By Java 5----"))
                    line = "SBJECT_PROFILE_FILE_PATH <- \"" + getRelativePathStr_ToSubjectProfileFilePath_FromRCode() + "\"";
                bw.write(line + "\n");
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * グルーピングを定義するRコードの生成
     * <p>
     * ConvertedDataフォルダ内での各症例の格納行数に由来するconstants.Rに書き込む文字列を生成する。
     * 【例】
     * group_NMLMET <- c(1, 3, 4, 6, 7, 9, 10, 11, 14, 15, 16, 17, 18, 21, 22, 23, 24, 25, 26, 27, 29, 33, 34, 37, 39, 41, 43, 44, 46, 47, 48, 49) #35人
     * group_NMLVAL <- c(2, 5, 8, 12, 13, 19, 20, 28, 30, 31, 32, 35, 36, 38, 42, 45) #16人
     * group_MDDMET <- c(80, 81, 82, 84, 86, 87, 89, 90, 91, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 106, 107, 108, 109) #23人
     * group_MDDVAL <- c(83, 85, 88, 92, 104, 110, 111, 112, 113, 114, 115) #11人
     */
    public String getGroupingCode() {

        //グルーピングの種類の把握
        ArrayList<String> group_label_list = new ArrayList<>();
        String a_group_label = "";
        for (int i = 0; i < GroupingList_in_DemographicDataFile.size(); i++) {
            a_group_label = GroupingList_in_DemographicDataFile.get(i);
            if (!group_label_list.contains(a_group_label)) {
                group_label_list.add(a_group_label);
            }
        }
        System.out.print("グループの種類数：" + group_label_list.size() + "    ");
        System.out.println(group_label_list);

        //グルーピングを定義する文字列の生成
        String[] group_number_sequence_list = new String[group_label_list.size()];// {"3, 4, 8, 9", "1, 5, 7, 10", "2, 6, 11, 12", ...}
        String a_group_number_seguence; // "3, 4, 8, 9"
        String a_given_id_in_demographicdatafile; // 001, 002, 003, ...
        int line_number_in_ConvertedDataFile = 1; //ConvertedDataFolder内でその症例が何番目にかくのうされているか
        for (int i = 0; i < group_label_list.size(); i++) {//group_label_listは "NML" とか "MDD”だとか
            a_group_label = group_label_list.get(i);  //"NML" とか "MDD”だとか

            a_group_number_seguence = "";
            for (int j = 0; j < GivenIdList_in_DemographicDataFile.size(); j++) {//DemographicData内の全てのSubjectに対してループ
                a_given_id_in_demographicdatafile = GivenIdList_in_DemographicDataFile.get(j);// 001, 002, 003, ...
                line_number_in_ConvertedDataFile = GivenIdList_in_DataOrderInfoFile.indexOf(a_given_id_in_demographicdatafile) + 1;
                System.out.println(a_given_id_in_demographicdatafile);
                System.out.println(line_number_in_ConvertedDataFile);
                if (line_number_in_ConvertedDataFile != 0) {
                    if (GroupingList_in_DemographicDataFile.get(j).equals(a_group_label)) {
                        a_group_number_seguence += line_number_in_ConvertedDataFile + ", ";
                    }
                }
            }

            a_group_number_seguence = a_group_number_seguence.substring(0, a_group_number_seguence.length() - 2);
            group_number_sequence_list[i] = a_group_number_seguence;
        }

        String result_str = "";
        for (int i = 0; i < group_label_list.size(); i++) {
            //System.out.println("group_" + group_label_list.get(i) + " <-  c(" + group_number_sequence_list[i] + ")");
            result_str += "group_" + group_label_list.get(i) + " <-  c(" + group_number_sequence_list[i] + ")\n";
        }


        return result_str;
    }


    /**
     * 比較対象グループを指定するRコードの生成
     * 【例】
     * #ComparisonPattern <- "MDDMET_vs_MDDVAL"
     * #ComparisonPattern <- "MDDMET_vs_NMLMET"
     * #ComparisonPattern <- "MDDMET_vs_NMLVAL"
     * #ComparisonPattern <- "MDDVAL_vs_NMLMET"
     * #ComparisonPattern <- "MDDVAL_vs_NMLVAL"
     * #ComparisonPattern <- "NMLMET_vs_NMLVAL"
     */
    public String getComparisonPattern() {
        //グルーピングの種類の把握
        ArrayList<String> group_label_list = new ArrayList<>();
        String a_group_label = "";
        for (int i = 0; i < GroupingList_in_DemographicDataFile.size(); i++) {
            a_group_label = GroupingList_in_DemographicDataFile.get(i);
            if (!group_label_list.contains(a_group_label)) {
                group_label_list.add(a_group_label);
            }
        }
        System.out.print("グループの種類数：" + group_label_list.size() + "    ");
        System.out.println(group_label_list);

        String rslt_str = "";
        for (int i = 0; i < group_label_list.size(); i++) {
            for (int j = 0; j < group_label_list.size(); j++) {
                if (i < j) {
                    rslt_str += "#COMPARISON_PATTERN <- \"" + group_label_list.get(i) + "_vs_" + group_label_list.get(j) + "\"\n";
                }
            }
        }
        rslt_str = rslt_str.replaceFirst("#", "");
        return rslt_str;
    }


    /**
     * 比較対象グループの関連の変数設定
     * 【例】
     * ##
     * ## A vs B
     * # ORIGINAL_GROUP_A_INDEXES <- group_A
     * # ORIGINAL_GROUP_B_INDEXES <- group_B
     * ##
     * ## A vs C
     * # ORIGINAL_GROUP_A_INDEXES <- group_A
     * # ORIGINAL_GROUP_B_INDEXES <- group_C
     * ##
     * ## B vs C
     * # ORIGINAL_GROUP_A_INDEXES <- group_B
     * # ORIGINAL_GROUP_B_INDEXES <- group_C
     */
    public String getOriginalGroupXindexCode() {
        //グルーピングの種類の把握
        ArrayList<String> group_label_list = new ArrayList<>();
        String a_group_label = "";
        for (int i = 0; i < GroupingList_in_DemographicDataFile.size(); i++) {
            a_group_label = GroupingList_in_DemographicDataFile.get(i);
            if (!group_label_list.contains(a_group_label)) {
                group_label_list.add(a_group_label);
            }
        }
        System.out.print("グループの種類数：" + group_label_list.size() + "    ");
        System.out.println(group_label_list);

        String rslt_str = "";
        for (int i = 0; i < group_label_list.size(); i++) {
            for (int j = 0; j < group_label_list.size(); j++) {
                if (i < j) {
                    rslt_str += "## " + group_label_list.get(i) + "_vs_" + group_label_list.get(j) + "\n";
                    rslt_str += "if(COMPARISON_PATTERN==\"" + group_label_list.get(i) + "_vs_" + group_label_list.get(j) + "\") ";
                    rslt_str += "ORIGINAL_GROUP_A_INDEXES <- group_" + group_label_list.get(i) + "\n";
                    rslt_str += "if(COMPARISON_PATTERN==\"" + group_label_list.get(i) + "_vs_" + group_label_list.get(j) + "\") ";
                    rslt_str += "ORIGINAL_GROUP_B_INDEXES <- group_" + group_label_list.get(j) + "\n";
                }
            }
        }
        return rslt_str;

    }

    /**
     * @param count
     * @return
     */
    public String getPrmttItrnCntCode(int count) {
        return "iteration_count <- " + String.valueOf(count) + " # シミュレーション回数。マシンの能力に合わせて調節。";
    }


    public String getRelativePathStr_ToStatsDataConverted_FromRCode() {
        return PathResolver_v1.getRelativePathStr(new File(DataOrderInfoFilePathStr).getParent(), RCodeFldPathStr);
    }

    public String getRelativePathStr_ToSubjectProfileFilePath_FromRCode() {
        return PathResolver_v1.getRelativePathStr(SubjectProfilePathStr, RCodeFldPathStr);
    }
}
