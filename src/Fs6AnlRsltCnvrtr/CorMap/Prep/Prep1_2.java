package Fs6AnlRsltCnvrtr.CorMap.Prep;

import Fs6AnlRsltCnvrtr.Util.*;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * constants.Rに書き込む
 * グルーピングのための文字列を生成するプログラム。
 * 現状使い捨てな感じで書かれているが、汎用化も有用かもしれない。
 * Created by issey on 2017/05/25.
 */
public class Prep1_2 {

    public static void main(String[] args) {

        /**
         * Statsファイルに関する準備
         */
        //String FS6_SBJECT_FLD_PATH = "C:\\Users\\issey\\Downloads\\20170601";
        String FS6_SBJECT_FLD_PATH = "H:\\20170601";
        String COR_MAP_DATA_FLD_PATH = "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\Data\\FS6_ANL_RSLT_20170601";
        //String COR_MAP_DATA_FLS_PATH ="C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\Data\\FS6_ANL_RSLT_20170601";
        extractStatsFiles(FS6_SBJECT_FLD_PATH, COR_MAP_DATA_FLD_PATH);


        /**
         * Demographicデータに関する準備
         */
        //String ORIGINAL_DEMOGRAPHIC_FILE_PATH = "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\Data\\DemographicFile.txt";
        String ORIGINAL_DEMOGRAPHIC_FILE_PATH = "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\Data\\gene_BDNFV66M.txt";
        //String ORIGINAL_DEMOGRAPHIC_FILE_PATH = "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\Data\\gene_COMTV108_158M.txt";
        //String ORIGINAL_DEMOGRAPHIC_FILE_PATH = "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\Data\\gene_NAT_G1287A.txt";
        //String ORIGINAL_DEMOGRAPHIC_FILE_PATH = "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\Data\\gene_NAT_T-182C.txt";
        //Demographic Data File は
        //１行目に「ID」「SEX」「AGE」「GROUPING」を持ち、
        //２行目以降にそれらに対応したデータが
        // 「タブ」または「半角スペース」または「コンマ」で区切られて記載された
        // テキストファイルを想定している。
        //
        String NEW_DEMOGRAPHIC_FILE_PATH = "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\Data\\NewDemographicFile.txt";
        remakeDemographicFile(ORIGINAL_DEMOGRAPHIC_FILE_PATH, NEW_DEMOGRAPHIC_FILE_PATH);

    }


    /**
     * Statsファイルの取り出し
     *
     * @param src_fld_path
     * @param dest_fld_path
     * @return
     */
    public static boolean extractStatsFiles(String src_fld_path, String dest_fld_path) {

        File src_fld = new File(src_fld_path);
        System.out.println(src_fld.getAbsolutePath());
        File read_target_fld;
        String OriginalSubjectID;
        String GivenSubjectID;
        for (int i = 0; i < src_fld.list(FileNameFilterConstructor_OnlyDir.getOnlyDirFilter()).length; i++) {

            OriginalSubjectID = src_fld.list(FileNameFilterConstructor_OnlyDir.getOnlyDirFilter())[i];//Subjectフォルダ内のフォルダ名
            GivenSubjectID = String.format("%03d", i + 1);//通し番号

            //元々の症例IDと新しく付けるIDの組み合わせを記録しておく
            OrigID_GivenID_Map.put(OriginalSubjectID, GivenSubjectID);

            read_target_fld = new File(src_fld.getAbsolutePath().replaceAll("\\\\", "/") + "/" + OriginalSubjectID + "/stats");
            System.out.println("read_target_fld=" + read_target_fld.getAbsolutePath());
            if (checkStatsFileExistance(read_target_fld.getAbsolutePath())) {

                //Lt_Aparc、Rt_Aparc、Asegファイルが正しく１つずつ存在する場合

                FilenameFilter filter = FileNameFilterConstructor_Regex.getRegexFilter(".*(lh\\.aparc\\.DKTatlas\\.stats)");
                File Lt_Aparc_File = read_target_fld.listFiles(filter)[0];
                filter = FileNameFilterConstructor_Regex.getRegexFilter(".*(rh\\.aparc\\.DKTatlas\\.stats)");
                File Rt_Aparc_File = read_target_fld.listFiles(filter)[0];
                filter = FileNameFilterConstructor_Regex.getRegexFilter(".*(aseg\\.stats)");
                File Aseg_File = read_target_fld.listFiles(filter)[0];

                //コピー
                FileCopyTools_ver2.copy(Lt_Aparc_File.getAbsolutePath(), dest_fld_path + "/" + GivenSubjectID + "_" + Lt_Aparc_File.getName());
                FileCopyTools_ver2.copy(Rt_Aparc_File.getAbsolutePath(), dest_fld_path + "/" + GivenSubjectID + "_" + Rt_Aparc_File.getName());
                FileCopyTools_ver2.copy(Aseg_File.getAbsolutePath(), dest_fld_path + "/" + GivenSubjectID + "_" + Aseg_File.getName());

            } else {
                System.out.println("    このフォルダからstatsファイルを取り出すのに失敗しました。");
            }
        }

        //OrigID_GivenID_Mapの書き出し
        CollectionWriterAndReader_ver7.outputToTextfile(OrigID_GivenID_Map, new File(dest_fld_path + "_OrigID_GivenID_Map.txt"));


        //結果表示
        JOptionPane.showMessageDialog(null, src_fld_path + "から\n" + dest_fld_path + "へ\n" + "statsファイルをコピーしました。");
        return true;
    }


    /**
     * Demographicデータの確認
     */
    public static boolean remakeDemographicFile(String original_demographic_file_path, String new_demographic_file_path) {
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
            br = new BufferedReader(new InputStreamReader(new FileInputStream(original_demographic_file_path), "UTF-8"));

            String line;
            String[] words;
            int line_number = 0;
            int OriginalIdColNum = 0;
            int AgeColNum = 0;
            int SexColNum = 0;
            int GroupingColNum = 0;

            while ((line = br.readLine()) != null) {
                line_number++;
                words = line.split("[,\\t\\s]+");
                if (line_number == 1) {//見出し行の処理
                    for (int i = 0; i < words.length; i++) {
                        if (words[i].equalsIgnoreCase("ID")) OriginalIdColNum = i;
                        if (words[i].equalsIgnoreCase("SEX")) SexColNum = i;
                        if (words[i].equalsIgnoreCase("AGE")) AgeColNum = i;
                        if (words[i].equalsIgnoreCase("GROUPING") || words[i].equalsIgnoreCase("GROUP"))
                            GroupingColNum = i;
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
        OrigID_GivenID_Map = CollectionWriterAndReader_ver7.loadToTreeMap(new File("C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.04\\Data\\FS6_ANL_RSLT_20170601_renamemap.txt"));

        /**
         * データチェック
         */
        if (OrigID_GivenID_Map.size() == 0) {
            System.out.println("OrigID_GivenID_Mapが空のようです。先にextractStatsFiles()を走らせてください。");
            return false;
        }
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
        File original_demographic_file = new File(original_demographic_file_path);
        String original_demographic_filename_ext = FileNameSplitter_ver1.getExtension(original_demographic_file.getName());
        String original_demographic_filename_nameonly = FileNameSplitter_ver1.getNameOnly(original_demographic_file.getName());
        new_demographic_file_path = new File(new_demographic_file_path).getParent() + "/" + original_demographic_filename_nameonly + "_remaked_" + "." + original_demographic_filename_ext;

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
                    JOptionPane.showMessageDialog(null, orig_id + " が OrigID_GivenID_Map 内に見つかりません。");
                }else{
                    bw.write(given_id + "\t" + AgeList.get(i) + "\t" + SexList.get(i) + "\t" + GroupingList.get(i) + "\t" + orig_id + "\n");
                }
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

    /**
     * FreeSerfer結果ファイルの数の確認
     */
    private static boolean checkStatsFileExistance(String fs6_anl_rslt_fld_path) {
        //フォルダの存在確認
        File fs6_anl_rslt_fld = new File(fs6_anl_rslt_fld_path);
        if (!fs6_anl_rslt_fld.exists()) {
            System.out.println(fs6_anl_rslt_fld_path + "は存在しません。");
            return false;
        }


        File FS6_ANL_RSLT_FLD = new File(fs6_anl_rslt_fld_path);

        System.out.println("    FS6_ANL_RSLT_FLD内のファイル数= " + FS6_ANL_RSLT_FLD.list().length);

        FilenameFilter filter = FileNameFilterConstructor_Regex.getRegexFilter(".*(lh\\.aparc\\.DKTatlas\\.stats)");
        File[] Lt_Aparc_Files = FS6_ANL_RSLT_FLD.listFiles(filter);
        System.out.println("    Lt_Aparc_Files の数=" + Lt_Aparc_Files.length);

        filter = FileNameFilterConstructor_Regex.getRegexFilter(".*(rh\\.aparc\\.DKTatlas\\.stats)");
        File[] Rt_Aparc_Files = FS6_ANL_RSLT_FLD.listFiles(filter);
        System.out.println("    Rt_Aparc_Files の数=" + Rt_Aparc_Files.length);

        filter = FileNameFilterConstructor_Regex.getRegexFilter(".*(aseg\\.stats)");
        File[] Aseg_Files = FS6_ANL_RSLT_FLD.listFiles(filter);
        System.out.println("    Aseg_Files の数=" + Rt_Aparc_Files.length);

        if (Lt_Aparc_Files.length * Rt_Aparc_Files.length * Rt_Aparc_Files.length == 0) {
            System.out.println("    FreeSerferから収集した結果ファイルが見つかりません。");
            return false;
        }

        if (Lt_Aparc_Files.length != 1) {
            System.out.println("    FreeSerferから収集したLt_Aparcファイルが複数あります。");
            return false;
        }
        if (Rt_Aparc_Files.length != 1) {
            System.out.println("    FreeSerferから収集したRt_Aparcファイルが複数あります。");
            return false;
        }
        if (Aseg_Files.length != 1) {
            System.out.println("    FreeSerferから収集したAsegファイルが複数あります。");
            return false;
        }

        System.out.println("    FreeSerferから収集した結果ファイルが正しく存在します。");
        return true;

    }

}
