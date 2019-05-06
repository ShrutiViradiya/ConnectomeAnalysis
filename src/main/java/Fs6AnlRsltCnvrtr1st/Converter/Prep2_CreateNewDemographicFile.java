package Fs6AnlRsltCnvrtr1st.Converter;

import IuCollectionIO.CollectionWriterAndReader_ver7;
import IuFileIO.FileNameSplitter_ver1;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * 使いやすいDemographic Data を生成するクラス
 * ID変換マップを同時に投げ込むひつようがあるが、
 * 空文字列を入れれば無効化。
 * Created by issey on 2017/05/25.
 */
public class Prep2_CreateNewDemographicFile {

    /**
     * CorMap用のSubjectProfileファイルを生成する。
     * args[0]: 元となるProfileデータへのパス。１行目に「ID」「SEX」「AGE」「GROUPING」を持ち、<br>
     *     ２行目以降にそれらに対応したデータが<br>
     *         「タブ」または「半角スペース」または「コンマ」で区切られて記載されたテキストファイルを想定している。
     * args[1]: 保存先へのパス
     * args[2]: ORIGID_GIVENID_MAP_FILE_PATH。Prep1_1が自動生成するオリジナルのIDと新たなIDの対応表へのパス。なければ空文字列で良い。
     * @param args
     */
    public static void main(String[] args) {
        remakeProfileFile(/*original_profile_file_path*/ args[0], /*save_fld_path*/ args[1], /*origid_givenid_map_file_path*/ args[2]);
    }


    /**
     * Demographicデータの修正
     *
     * @param original_profile_file_path
     * @param save_fld_path
     * @param origid_givenid_map_file_path
     * @return
     */
    public static boolean remakeProfileFile(String original_profile_file_path, String save_fld_path, String origid_givenid_map_file_path) {
        //集める情報
        ArrayList<String> OriginalIdList = new ArrayList<>();
        ArrayList<String> GivenIdList = new ArrayList<>();
        ArrayList<String> AgeList = new ArrayList<>();
        ArrayList<String> SexList = new ArrayList<>();
        ArrayList<String> GroupingList = new ArrayList<>();


        /**
         * まずはオリジナルのDemographicデータを読み込む
         */
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(original_profile_file_path), "UTF-8"));
            String line;
            String[] words;
            int line_number = 0;
            int OriginalIdColNum = 0;
            int AgeColNum = 0;
            int SexColNum = 0;
            int GroupingColNum = 0;
            while ((line = br.readLine()) != null) {
                line_number++;
                words = line.split("[,\\t\\s]+");//半角スペース、タブ、コンマを区切り文字として認識
                if (line_number == 1) {//見出し行の処理
                    for (int i = 0; i < words.length; i++) {
                        if (words[i].equalsIgnoreCase("ID")) OriginalIdColNum = i;
                        if (words[i].equalsIgnoreCase("SEX")) SexColNum = i;
                        if (words[i].equalsIgnoreCase("AGE")) AgeColNum = i;
                        if (words[i].equalsIgnoreCase("GROUPING") || words[i].equalsIgnoreCase("GROUP")) GroupingColNum = i;
                    }
                } else {//2行目以降の処理
                    OriginalIdList.add(words[OriginalIdColNum]);
                    SexList.add(words[SexColNum]);
                    AgeList.add(words[AgeColNum]);
                    GroupingList.add(words[GroupingColNum]);
                }
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

        /**
         * OrigID_GivenID_Map のロード
         */
        if (origid_givenid_map_file_path == "") {
            OrigID_GivenID_Map = new TreeMap<>();
        } else {
            OrigID_GivenID_Map = CollectionWriterAndReader_ver7.loadToTreeMap(new File(origid_givenid_map_file_path));
        }

        /**
         * データチェック
         */
        //if (OrigID_GivenID_Map.size() == 0) {
        //    System.out.println("OrigID_GivenID_Mapが空のようです。先にextractStatsFiles()を走らせてください。");
        //    return false;
        //}
        if (AgeList.size() != OriginalIdList.size()) {
            System.out.println("Demographicファイル内の、Age列の行数とId列の行数が一致しません。");
            return false;
        }
        if (SexList.size() != OriginalIdList.size()) {
            System.out.println("Demographicファイル内の、Sex列の行数とId列の行数が一致しません。");
            return false;
        }
        if (GroupingList.size() != OriginalIdList.size()) {
            System.out.println("Demographicファイル内の、Grouping列の行数とId列の行数が一致しません。");
            return false;
        }

        /**
         * 新しいDemographicデータファイルの書き出し
         */
        File original_demographic_file = new File(original_profile_file_path);
        String original_demographic_filename_ext = FileNameSplitter_ver1.getExtension(original_demographic_file.getName());
        String original_demographic_filename_nameonly = FileNameSplitter_ver1.getNameOnly(original_demographic_file.getName());
        String new_demographic_file_path = save_fld_path + "/" + original_demographic_filename_nameonly + "_with_givenid" + "." + original_demographic_filename_ext;

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new_demographic_file_path), "UTF-8"));

            String line;
            bw.write("GIVEN_ID" + "\t" + "AGE" + "\t" + "SEX" + "\t" + "GROUPING" + "\t" + "ORIGINAL_ID" + "\n");
            String given_id = "";
            String orig_id = "";
            for (int i = 0; i < OriginalIdList.size(); i++) {
                orig_id = OriginalIdList.get(i);
                given_id = OrigID_GivenID_Map.get(orig_id);
                if (given_id == null) {
                    //JOptionPane.showMessageDialog(null, orig_id + " が OrigID_GivenID_Map 内に見つかりません。");
                    given_id = orig_id;
                }
                bw.write(given_id + "\t" + AgeList.get(i) + "\t" + SexList.get(i) + "\t" + GroupingList.get(i) + "\t" + orig_id + "\n");
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                return false;
            }
        }
        JOptionPane.showMessageDialog(null, "新しいDemographicデータファイル\n「" + new_demographic_file_path + "」\nを書き出しました。");
        return true;
    }


    /* ----------------------------------------------------------------------------------- */

    /**
     *
     */
    private static TreeMap<String, String> OrigID_GivenID_Map = new TreeMap<>();

}
