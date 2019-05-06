package Fs6AnlRsltCnvrtr1st.Converter;

import FileFilter.FilterConstructor.IoFileNameFilterConstructor_OnlyDir;
import FileFilter.FilterConstructor.IoFileNameFilterConstructor_Regex;
import IuCollectionIO.CollectionWriterAndReader_ver7;
import IuFileManagement.FileCopyTools_ver2;

import javax.swing.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.TreeMap;

/**
 * FreeSurfer6 が生成する statsファイルを取り出すクラス
 * 単一フォルダに集める。
 * IDも使いやすいものに置き換える。
 * 変換マップも生成する。
 * Created by issey on 2017/05/25.
 */
public class Prep1_GatheringStatsFiles {

    //static final String INPUT_FOLDER = "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.06";
    static final String INPUT_FOLDER = "C:\\Users\\issey\\Documents\\Dropbox\\NetbeansProjects_xps14z\\ConnectomeAnalysis\\src\\test\\java\\CorMapper_v1.06\\StatsData_orig\\MDDGENE";
    static final String OUTPUT_FOLDER = "./src/main/java/CorMapper_v5/Fd6AnlRsltCnvrtr/Temp";
    /**
     * Statsファイルに関する準備
     * FreeSurfer6のSubjectsフォルダから必要なstatsファイルを取り出すメソッド
     * arg[0]: FreeSurferのSubjectフォルダへのパス
     * arg[1]: statsファイルの保存先フォルダパス
     */
    public static void main(String[] args) {
        if (args.length == 2) {
            //String FS6_SBJECT_FLD_PATH = "C:\\Users\\issey\\Downloads\\20170601";
            //String FS6_SBJECT_FLD_PATH = "H:\\20170601";
            //String COR_MAP_DATA_FLD_PATH = "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.05\\Data\\FS6_ANL_RSLT_20170601";
            extractStatsFiles(/*src_fld_path*/ args[0], /*dest_fld_path*/args[1]);
        } else {
            extractStatsFiles(/*src_fld_path*/ INPUT_FOLDER, /*dest_fld_path*/ OUTPUT_FOLDER);
            //System.out.println("引数を確認してください。");
        }
    }


    /**
     * Statsファイルの取り出し
     *
     * @param src_fld_path: FreeSurferのSubjectフォルダへのPath
     * @param dest_fld_path: 抜き出した *.stats ファイルを格納する先
     * @return
     */
    public static boolean extractStatsFiles(String src_fld_path, String dest_fld_path) {

        File src_fld = new File(src_fld_path);
        System.out.println(src_fld.getAbsolutePath());
        File read_target_fld;
        String OriginalSubjectID;
        String GivenSubjectID;
        for (int i = 0; i < src_fld.list(IoFileNameFilterConstructor_OnlyDir.getOnlyDirFilter()).length; i++) {

            OriginalSubjectID = src_fld.list(IoFileNameFilterConstructor_OnlyDir.getOnlyDirFilter())[i];//Subjectフォルダ内のフォルダ名
            GivenSubjectID = String.format("%03d", i + 1);//通し番号

            //元々の症例IDと新しく付けるIDの組み合わせを記録しておく
            OrigID_GivenID_Map_.put(OriginalSubjectID, GivenSubjectID);

            read_target_fld = new File(src_fld.getAbsolutePath().replaceAll("\\\\", "/") + "/" + OriginalSubjectID + "/stats");
            System.out.println("read_target_fld=" + read_target_fld.getAbsolutePath());
            if (checkStatsFileExistance(read_target_fld.getAbsolutePath())) {

                //Lt_Aparc、Rt_Aparc、Asegファイルが正しく１つずつ存在する場合

                FilenameFilter filter = IoFileNameFilterConstructor_Regex.getRegexFilter(".*(lh\\.aparc\\.DKTatlas\\.stats)");
                File Lt_Aparc_File = read_target_fld.listFiles(filter)[0];
                filter = IoFileNameFilterConstructor_Regex.getRegexFilter(".*(rh\\.aparc\\.DKTatlas\\.stats)");
                File Rt_Aparc_File = read_target_fld.listFiles(filter)[0];
                filter = IoFileNameFilterConstructor_Regex.getRegexFilter(".*(aseg\\.stats)");
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
        CollectionWriterAndReader_ver7.outputToTextfile(OrigID_GivenID_Map_, new File(dest_fld_path + "_OrigID_GivenID_Map.txt"));


        //結果表示
        JOptionPane.showMessageDialog(null, src_fld_path + "から\n" + dest_fld_path + "へ\n" + "statsファイルをコピーしました。");
        return true;
    }


    /* ----------------------------------------------------------------------------------- */


    /**
     *
     */
    private static TreeMap<String, String> OrigID_GivenID_Map_ = new TreeMap<>();

    /**
     * FreeSerfer結果ファイルの数の確認
     */
    private static boolean checkStatsFileExistance(String fs6_anl_rslt_fld_path) {
        //フォルダの存在確認
        File fs6_anl_rslt_fld = new File(fs6_anl_rslt_fld_path);
        if (!fs6_anl_rslt_fld.exists()) {
            System.out.println("    " + fs6_anl_rslt_fld_path + "は存在しません。");
            return false;
        }


        File FS6_ANL_RSLT_FLD = new File(fs6_anl_rslt_fld_path);

        System.out.println("    FS6_ANL_RSLT_FLD内のファイル数= " + FS6_ANL_RSLT_FLD.list().length);

        FilenameFilter filter = IoFileNameFilterConstructor_Regex.getRegexFilter(".*(lh\\.aparc\\.DKTatlas\\.stats)");
        File[] Lt_Aparc_Files = FS6_ANL_RSLT_FLD.listFiles(filter);
        System.out.println("    Lt_Aparc_Files の数=" + Lt_Aparc_Files.length);

        filter = IoFileNameFilterConstructor_Regex.getRegexFilter(".*(rh\\.aparc\\.DKTatlas\\.stats)");
        File[] Rt_Aparc_Files = FS6_ANL_RSLT_FLD.listFiles(filter);
        System.out.println("    Rt_Aparc_Files の数=" + Rt_Aparc_Files.length);

        filter = IoFileNameFilterConstructor_Regex.getRegexFilter(".*(aseg\\.stats)");
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
