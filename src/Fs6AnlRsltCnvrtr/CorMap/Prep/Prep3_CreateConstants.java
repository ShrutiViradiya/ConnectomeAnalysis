package Fs6AnlRsltCnvrtr.CorMap.Prep;

import java.io.*;
import java.util.ArrayList;

/**
 * constants.Rに書き込む
 * グルーピングのための文字列を生成するプログラム。
 * 現状使い捨てな感じで書かれているが、汎用化も有用かもしれない。
 * Created by issey on 2017/05/25.
 */
public class Prep3_CreateConstants {

    public static void main(String[] args) {

        String SUBJECT_NAME_FILE_PATH = "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\ConvertedData\\surf+deep\\SubjectName.txt";
        String NEW_DEMOGRAPHIC_FILE_PATH = "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\Data\\NewDemographicFile.txt";

        Prep3_CreateConstants dc2 = new Prep3_CreateConstants(SUBJECT_NAME_FILE_PATH, NEW_DEMOGRAPHIC_FILE_PATH);
        System.out.println(dc2.getGroupingCode());
        System.out.println(dc2.getPrmttItrnCntCode(1000));
        System.out.println(dc2.getComparisonPattern());


    dc2.makeNewConstantsR("C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\RCode\\constants.R");

    }

    static ArrayList<String> GivenIdList_in_DemographicDataFile = new ArrayList<>();
    static ArrayList<String> AgeList_in_DemographicDataFile = new ArrayList<>();
    static ArrayList<String> SexList_in_DemographicDataFile = new ArrayList<>();
    static ArrayList<String> GroupingList_in_DemographicDataFile = new ArrayList<>();
    static ArrayList<String> GivenIdList_in_SubjestNameFile = new ArrayList<>();

    /**
     * コンストラクタ
     *
     * @param subject_name_list_file_path
     * @param demographic_data_file_path
     */
    public Prep3_CreateConstants(String subject_name_list_file_path, String demographic_data_file_path) {

        /**
         * SubjectName.txt 内での症例の行目を確認
         */
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(subject_name_list_file_path), "UTF-8"));

            String line;
            String[] words;
            while ((line = br.readLine()) != null) {
                GivenIdList_in_SubjestNameFile.add(line);
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
        System.out.println("GivenIdList_in_SubjestNameFile=" + GivenIdList_in_SubjestNameFile);

        /**
         * DemographicDataの確認
         */
        GivenIdList_in_DemographicDataFile = new ArrayList<>();
        AgeList_in_DemographicDataFile = new ArrayList<>();
        SexList_in_DemographicDataFile = new ArrayList<>();
        GroupingList_in_DemographicDataFile = new ArrayList<>();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(demographic_data_file_path), "UTF-8"));

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
                line_number_in_ConvertedDataFile = GivenIdList_in_SubjestNameFile.indexOf(a_given_id_in_demographicdatafile) + 1;
                if (GroupingList_in_DemographicDataFile.get(j).equals(a_group_label)) {
                    a_group_number_seguence += line_number_in_ConvertedDataFile + ", ";
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
    public String getOriginalGroupXindexCode(){
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


    /**
     *
     */
    public void makeNewConstantsR(String new_file_path){

        System.out.println("user.dir: " + System.getProperty("user.dir"));//User working directory
        String wd_path = System.getProperty("user.dir");
        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(wd_path + "\\src\\Fs6AnlRsltCnvrtr\\CorMap\\RCode\\constants.R"), "UTF-8"));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new_file_path), "UTF-8"));

            String line;
            while ((line = br.readLine()) != null) {
                if(line.endsWith("#----Replaced By Java 1----")) line = getComparisonPattern();
                if(line.endsWith("#----Replaced By Java 2----")) line = getGroupingCode();
                if(line.endsWith("#----Replaced By Java 3----")) line = getOriginalGroupXindexCode();

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

}
