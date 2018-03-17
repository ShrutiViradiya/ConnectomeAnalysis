package CorMapper_v6_0.Fs6AnlRsltCnvrtr.Converter;

import CorMapper_v6_0.Fs6AnlRsltCnvrtr.DataClass.*;
import IuCollectionIO.DataFrameConstructer;

import javax.swing.*;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by issey on 2016/11/27.
 */
public class Prep3_DataExtractionFromStatsFiles {
    /**
     * 症例毎にまとめられたテキストデータを脳領域毎にまとめ直すツール。
     * TargetFolder内にある「*_aseg.stats」「*_rh.aparc.DKTatlas.stats」「*_lh.aparc.DKTatlas.stats」から体積数値を抜き出し、
     * 77脳領域別にまとめ直す。OutputFolder内には、「eTIV.txt」「DataOrderInfo.txt」「VolDataOfEachArea（フォルダ）」が出力される。
     * VolDataOfEadAreaフォルダ内には77個の各納領域に対応したファイルが収められる。各ファイル内の数字の並び順はDataOrderInfo.txt内の症例IDの並び順に対応している。
     * eTIV.txtファイル内には各症例のEstimated Total Intracranial Volume（頭蓋内容積）が収められている。このファイル内の数字の並び順もDataOrderInfo.txt内の症例IDの並び順に対応している。
     * 第1引数：read_target_folder_path
     * 第2引数：output_folder_path
     * 第3引数：表層領域を出力するか [true / false]
     * 第4引数：深部領域を出力するか [true / false]
     * 第5引数：海馬亜領域を出力するか [true / false]
     * 第6引数：テスト用の６領域のみを出力するか [true / false]
     */
    public static void main(String[] args) {
        if (args.length == 6) {
            core(
                    /* read_target_folder_path*/ args[0],
                    /* output_folder_path*/ args[1],
                    /* should_output_surf*/ Boolean.parseBoolean(args[2]),
                    /* should_output_deep*/ Boolean.parseBoolean(args[3]),
                    /* should_output_hipsub*/ Boolean.parseBoolean(args[4]),
                    /* should_output_6nodes*/ Boolean.parseBoolean(args[5]));

        } else {
            core("./src/main/java/CorMapper_v6_0/Fs6AnlRsltCnvrtr/ExtractedFromFS6/", "./src/main/java/CorMapper_v6_0/data00", true, true, false, false);
            //System.out.println("引数を確認してください。");
        }

    }

    public static void core(String read_target_folder_path, String output_folder_path,
                            boolean should_output_surf, boolean should_output_deep, boolean should_output_hipsub,
                            boolean should_output_6nodes) {

        //読み取り対象フォルダのパスの調整
        read_target_folder_path = read_target_folder_path.replaceAll("\\\\", "/");
        if (read_target_folder_path.startsWith("./")) {
            File currentDirectory = new File(".");
            String currentDirectoryAbsolutePath = currentDirectory.getAbsolutePath();
            read_target_folder_path = currentDirectoryAbsolutePath.substring(0, currentDirectoryAbsolutePath.length() - 2) + read_target_folder_path.substring(1, read_target_folder_path.length()) + "/";
        }
        read_target_folder_path = read_target_folder_path.replaceAll("\\\\", "/");
        if (!read_target_folder_path.endsWith("/")) {
            read_target_folder_path = read_target_folder_path + "/";
        }

        URI read_target_folder_path_uri = null;
        try {
            read_target_folder_path_uri = new URI("file:/" + read_target_folder_path);
        } catch (URISyntaxException urise) {
            JOptionPane.showMessageDialog(null, urise.getMessage());
        }


        //出力先フォルダパスの調整
        output_folder_path = output_folder_path.replaceAll("\\\\", "/");
        if (output_folder_path.startsWith("./")) {
            File currentDirectory = new File(".");
            String currentDirectoryAbsolutePath = currentDirectory.getAbsolutePath();
            output_folder_path = currentDirectoryAbsolutePath.substring(0, currentDirectoryAbsolutePath.length() - 2) + output_folder_path.substring(1, output_folder_path.length()) + "/";
        }
        if (!output_folder_path.endsWith("/")) {
            output_folder_path = output_folder_path + "/";
        }

        String message = "read_target_folder_path=" + read_target_folder_path + "\n"
                + "read_target_folder_path_uri=" + read_target_folder_path_uri.getPath() + "\n"
                + "output_folder_path=" + output_folder_path + "\n";
        JOptionPane.showMessageDialog(null, message);
        //System.exit(-1);

        //Dataのリストをまず生成
        /*
         * ファイルを横断的に走査、出力
         */
        AsegStatsBunch Asg = new AsegStatsBunch(read_target_folder_path_uri.getPath());
        AparcStatsBunch_Rt AprcR = new AparcStatsBunch_Rt(read_target_folder_path_uri.getPath());
        AparcStatsBunch_Lt AprcL = new AparcStatsBunch_Lt(read_target_folder_path_uri.getPath());
        HippoSfVolsBunch_Rt HS_Rt = new HippoSfVolsBunch_Rt(read_target_folder_path_uri.getPath());
        HippoSfVolsBunch_Lt HS_Lt = new HippoSfVolsBunch_Lt(read_target_folder_path_uri.getPath());


        /**
         * 症例IDを出力
         */
        //CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_SubjectName(), new File(output_folder_path + "DataOrderInfo.txt"));
        ArrayList new_col_val = Asg.getListOf_SubjectName();
        new_col_val.add(0, "ID");
        DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");


        /**
         * 77領域の体積を横断的に出力
         * （83-6領域）
         * TODO: できれば83領域ぐらいでやりたいのだが、そもそも領域名がなかったり、欠損値があったり
         */
        if (should_output_surf) {
            //1 R.LOFC ctx-rh-lateralorbitofrontal 26.76 32.66 -12.88 右外側眼窩前頭皮質 -
            new_col_val = AprcR.getListOf_Lateralorbitofrontal_Vol();
            new_col_val.add(0, "R_LOFC");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //2 R.OIFG ctx-rh-parsorbitalis 45.46 36.29 -10.44 右眼窩部 -
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Parsorbitalis_Vol();
            new_col_val.add(0, "R_OIFG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //3 R.FRTP ctx-rh-frontalpole 14.11 60.27 -12.49 右前頭極 大脳の一番前方の部分
        if (should_output_surf) {
            //none
            //new_col_val.add(0, "R_FRTP");
            //DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //4 R.MOFC ctx-rh-medialorbitofrontal 10.32 41.87 -12.61 右中眼窩前頭皮質 -
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Medialorbitofrontal_Vol();
            new_col_val.add(0, "R_MOFC");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //5 R.PTRI ctx-rh-parstriangularis 46.35 33.36 4.39 右三角部 前頭葉の左下前頭回にあるブローカ野の一部でブロードマンの脳地図の45野
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Parstriangularis_Vol();
            new_col_val.add(0, "R_PTRI");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //6 R.POPE ctx-rh-parsopercularis 47.95 14.60 13.80 右弁蓋部 味覚に関与
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Parsopercularis_Vol();
            new_col_val.add(0, "R_POPE");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //7 R.RMFG ctx-rh-rostralmiddlefrontal 34.98 42.64 16.63 右吻側中前頭回 -
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Rostralmiddlefrontal_Vol();
            new_col_val.add(0, "R_RMFG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //8 R.SFG ctx-rh-superiorfrontal 16.85 25.18 42.49 右上前頭回 -
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Superiorfrontal_Vol();
            new_col_val.add(0, "R_SFG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //9 R.CMFG ctx-rh-caudalmiddlefrontal 39.99 7.29 47.68 右尾側中前頭回 -
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Caudalmiddlefrontal_Vol();
            new_col_val.add(0, "R_CMFG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //10 R.PREC ctx-rh-precentral 39.91 -11.66 47.82 右中心前回 体性運動
        if (should_output_surf) {
            new_col_val = AprcR.getListOF_Precentral_Vol();
            new_col_val.add(0, "R_PREC");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //11 R.PRCL ctx-rh-paracentral 11.31 -31.56 59.81 右中心傍小葉 下腿部と足の運動を司る
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Paracentral_Vol();
            new_col_val.add(0, "R_PRCL");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //12 R.RCIN ctx-rh-rostralanteriorcingulate 10.2 40.36 4.09 右吻側前帯状回 -
        if (should_output_deep) {
            new_col_val = AprcR.getListOf_Rostralanteriorcingulate_Vol();
            new_col_val.add(0, "R_RCIN");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //13 R.CCIN ctx-rh-caudalanteriorcingulate 9.58 14.58 28.80 右尾側前帯状回 -
        if (should_output_deep) {
            new_col_val = AprcR.getListOf_Caudalanteriorcingulate_Vol();
            new_col_val.add(0, "R_CCIN");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //14 R.PCIN ctx-rh-posteriorcingulate 9.61 -21.42 39.98 右帯状回後部 -
        if (should_output_deep) {
            new_col_val = AprcR.getListOf_Posteriorcingulate_Vol();
            new_col_val.add(0, "R_PCIN");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //15 R.ICIN ctx-rh-isthmuscingulate 10.14 -45.29 21.70 右帯状回峡 -
        if (should_output_deep) {
            new_col_val = AprcR.getListOf_Isthmuscingulate_Vol();
            new_col_val.add(0, "R_ICIN");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //16 R.PSTCG ctx-rh-postcentral 44.75 -25.37 49.15 右中心後回 体性感覚野
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Postcentral_Vol();
            new_col_val.add(0, "R_PSTCG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //17 R.SPMG ctx-rh-supramarginal 51.68 -37.02 38.04 右縁上回 -
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Supramarginal_Vol();
            new_col_val.add(0, "R_SPMG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //18 R.SPL ctx-rh-superiorparietal 21.95 -65.86 50.08 右上頭頂葉 -
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Superiorparietal_Vol();
            new_col_val.add(0, "R_SPL");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //19 R.IPL ctx-rh-inferiorparietal 42.27 -67.11 30.38 右下頭頂葉 -
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Inferiorparietal_Vol();
            new_col_val.add(0, "R_IPL");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //20 R.PCUN ctx-rh-precuneus 11.06 -58.35 41.54 右楔前部 -
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Precuneus_Vol();
            new_col_val.add(0, "R_PCUN");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //21 R.CUNE ctx-rh-cuneus 7.49 -80.82 22.13 右楔状葉 -
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Cuneus_Vol();
            new_col_val.add(0, "R_CUNE");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //22 R.CALS ctx-rh-pericalcarine 12.19 -81.97 10.37 右鳥距溝周囲 視覚連合野
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Pericalcarine_Vol();
            new_col_val.add(0, "R_CALS");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //23 R.LOS ctx-rh-lateraloccipital 30.66 -88.75 -0.05 右後頭極 和名正しい？
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Lateraloccipital_Vol();
            new_col_val.add(0, "R_LOS");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //24 R.LING ctx-rh-lingual 13.30 -68.60 0.88 右舌状回 後頭葉内側面で鳥距溝の下面に存在
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Lingual_Vol();
            new_col_val.add(0, "R_LING");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //25 R.FUSI ctx-rh-fusiform 33.68 -47.45 -16.25 右紡錘状回 側頭葉の一部。後頭側頭回とも呼ばれる。
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Fusiform_Vol();
            new_col_val.add(0, "R_FUSI");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //26 R.PHIP ctx-rh-parahippocampal 27.61 -32.33 -14.71 右海馬傍回 -
        if (should_output_deep) {
            new_col_val = AprcR.getListOf_Parahippocampal_Vol();
            new_col_val.add(0, "R_PHIP");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //27 R.ENTO ctx-rh-entorhinal 25.02 -6.43 -30.61 右嗅内野 連合野と海馬体との情報連絡を仲介
        if (should_output_deep) {
            new_col_val = AprcR.getListOf_Entorhinal_Vol();
            new_col_val.add(0, "R_ENTO");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //28 R.TMPP ctx-rh-temporalpole 36.73 10.12 -34.77 右側頭極 側頭葉の前端
        if (should_output_surf) {
            //none
            //new_col_val = AprcR.getListOf_Temporalpole_Vol();
            //new_col_val.add(0, "R_TMPP");
            //DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //29 R.ITG ctx-rh-inferiortemporal 50.66 -32.22 -22.62 右下側頭回 -
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Inferiortemporal_Vol();
            new_col_val.add(0, "R_ITG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //30 R.MTG ctx-rh-middletemporal 56.29 -25.56 -12.02 右中側頭回 -
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Middletemporal_Vol();
            new_col_val.add(0, "R_MTG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //31 R.BSTS ctx-rh-bankssts 50.30 -44.37 11.71 右上側頭溝壁 和名正しい？banks-of-the-superior-temporal-sulcus
        if (should_output_surf) {
            //none
            //new_col_val.add(0, "R_BSTS");
            //DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //32 R.STG ctx-rh-superiortemporal 54.57 -14.21 -3.53 右上側頭回 -
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Superiortemporal_Vol();
            new_col_val.add(0, "R_STG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //33 R.TTG ctx-rh-transversetemporal 47.34 -22.37 9.44 右横側頭回 聴覚中枢
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Transversetemporal_Vol();
            new_col_val.add(0, "R_TTG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //34 R.INSU ctx-rh-insula 37.28 -3.14 2.58 右島 -
        if (should_output_surf) {
            new_col_val = AprcR.getListOf_Insula_Vol();
            new_col_val.add(0, "R_INSU");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //35 R.THAL Right-Thalamus-Proper 12.38 -18.04 8.04 右視床 -
        if (should_output_deep) {
            new_col_val = Asg.getListOf_RightThalamusProper_Vol();
            new_col_val.add(0, "R_THAL");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //36 R.CAUD Right-Caudate 16.32 8.63 10.71 右尾状核 -
        if (should_output_deep) {
            new_col_val = Asg.getListOf_RightCaudate_Vol();
            new_col_val.add(0, "R_CAUD");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //37 R.PUTA Right-Putamen 28.05 0.14 -0.23 右被殻 -
        if (should_output_deep) {
            new_col_val = Asg.getListOf_RightPutamen_Vol();
            new_col_val.add(0, "R_PUTA");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //38 R.PALL Right-Pallidum 21.69 -4.00 -1.68 右淡蒼球 -
        if (should_output_deep) {
            new_col_val = Asg.getListOf_RightPallidum_Vol();
            new_col_val.add(0, "R_PALL");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //39 R.ACMB Right-Accumbens-area 10.62 11.13 -7.53 右側坐核 新皮質-線条体-淡蒼球という経路と並行の回路を形成
        if (should_output_deep) {
            new_col_val = Asg.getListOf_RightAccumbensarea_Vol();
            new_col_val.add(0, "R_ACMB");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //40 R.HIPP Right-Hippocampus 25.66 -22.55 -11.85 右海馬 -
        if (should_output_deep) {
            new_col_val = Asg.getListOf_RightHippocampus_Vol();
            new_col_val.add(0, "R_HIPP");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //41 R.AMYG Right-Amygdala 25.56 -4.28 -19.74 右扁桃体 -
        if (should_output_deep) {
            new_col_val = Asg.getListOf_RightAmygdala_Vol();
            new_col_val.add(0, "R_AMYG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //42 L.LOFC ctx-lh-lateralorbitofrontal -21.81 34.18 -13.40 左外側眼窩前頭皮質 -
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Lateralorbitofrontal_Vol();
            new_col_val.add(0, "L_LOFC");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //43 L.OIFG ctx-lh-parsorbitalis -40.58 37.38 -8.54 左眼窩部 -
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Parsorbitalis_Vol();
            new_col_val.add(0, "L_OIFG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //44 L.FRTP ctx-lh-frontalpole -8.55 61.09 -4.09 左前頭極 大脳の一番前方の部分
        if (should_output_surf) {
            //none
            //new_col_val.add(0, "L_FRTP");
            //DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //45 L.MOFC ctx-lh-medialorbitofrontal -6.18 42.72 -10.32 左中眼窩前頭皮質 -
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Medialorbitofrontal_Vol();
            new_col_val.add(0, "L_MOFC");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //46 L.PTRI ctx-lh-parstriangularis -40.05 35.96 6.58 左三角部 前頭葉の左下前頭回にあるブローカ野の一部でブロードマンの脳地図の45野
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Parstriangularis_Vol();
            new_col_val.add(0, "L_PTRI");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //47 L.POPE ctx-lh-parsopercularis -42.97 17.76 14.33 左弁蓋部 味覚に関与
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Parsopercularis_Vol();
            new_col_val.add(0, "L_POPE");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //48 L.RMFG ctx-lh-rostralmiddlefrontal -28.78 43.39 18.16 左吻側中前頭回 -
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Rostralmiddlefrontal_Vol();
            new_col_val.add(0, "L_RMFG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //49 L.SFG ctx-lh-superiorfrontal -9.47 25.77 44.12 左上前頭回 -
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Superiorfrontal_Vol();
            new_col_val.add(0, "L_SFG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //50 L.CMFG ctx-lh-caudalmiddlefrontal -30.68 10.09 48.59 左尾側中前頭回 -
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Caudalmiddlefrontal_Vol();
            new_col_val.add(0, "L_CMFG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //51 L.PREC ctx-lh-precentral -35.49 -9.59 47.33 左中心前回 体性運動
        if (should_output_surf) {
            new_col_val = AprcL.getListOF_Precentral_Vol();
            new_col_val.add(0, "L_PREC");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //52 L.PRCL ctx-lh-paracentral -8.94 -28.30 62.68 左中心傍小葉 下腿部と足の運動を司る
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Paracentral_Vol();
            new_col_val.add(0, "L_PRCL");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //53 L.RCIN ctx-lh-rostralanteriorcingulate -4.09 39.54 6.87 左吻側前帯状回 -
        if (should_output_deep) {
            new_col_val = AprcL.getListOf_Rostralanteriorcingulate_Vol();
            new_col_val.add(0, "L_RCIN");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //54 L.CCIN ctx-lh-caudalanteriorcingulate -3.59 17.47 28.97 左尾側前帯状回 -
        if (should_output_deep) {
            new_col_val = AprcL.getListOf_Caudalanteriorcingulate_Vol();
            new_col_val.add(0, "L_CCIN");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //55 L.PCIN ctx-lh-posteriorcingulate -6.35 -22.66 38.46 左帯状回後部 -
        if (should_output_deep) {
            new_col_val = AprcL.getListOf_Posteriorcingulate_Vol();
            new_col_val.add(0, "L_PCIN");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //56 L.ICIN ctx-lh-isthmuscingulate -9.06 -44.99 19.87 左帯状回峡 -
        if (should_output_deep) {
            new_col_val = AprcL.getListOf_Isthmuscingulate_Vol();
            new_col_val.add(0, "L_ICIN");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //57 L.PSTCG ctx-lh-postcentral -41.98 -23.73 49.50 左中心後回 体性感覚野
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Postcentral_Vol();
            new_col_val.add(0, "L_PSTCG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //58 L.SPMG ctx-lh-supramarginal -49.48 -37.05 36.35 左縁上回 -
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Supramarginal_Vol();
            new_col_val.add(0, "L_SPMG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //59 L.SPL ctx-lh-superiorparietal -20.71 -65.49 47.99 左上頭頂葉 -
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Superiorparietal_Vol();
            new_col_val.add(0, "L_SPL");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //60 L.IPL ctx-lh-inferiorparietal -38.87 -71.16 29.99 左下頭頂葉 -
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Inferiorparietal_Vol();
            new_col_val.add(0, "L_IPL");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //61 L.PCUN ctx-lh-precuneus -11.04 -57.34 40.44 左楔前部 -
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Precuneus_Vol();
            new_col_val.add(0, "L_PCUN");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //62 L.CUNE ctx-lh-cuneus -6.88 -81.61 19.57 左楔状葉 -
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Cuneus_Vol();
            new_col_val.add(0, "L_CUNE");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //63 L.CALS ctx-lh-pericalcarine -12.66 -82.54 8.88 左鳥距溝周囲 視覚連合野
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Pericalcarine_Vol();
            new_col_val.add(0, "L_CALS");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //64 L.LOS ctx-lh-lateraloccipital -28.61 -90.79 -1.00 左後頭極 和名正しい？
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Lateraloccipital_Vol();
            new_col_val.add(0, "L_LOS");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //65 L.LING ctx-lh-lingual -14.04 -71.75 -0.47 左舌状回 後頭葉内側面で鳥距溝の下面に存在
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Lingual_Vol();
            new_col_val.add(0, "L_LING");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //66 L.FUSI ctx-lh-fusiform -36.53 -42.62 -17.62 左紡錘状回 側頭葉の一部。後頭側頭回とも呼ばれる。
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Fusiform_Vol();
            new_col_val.add(0, "L_FUSI");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //67 L.PHIP ctx-lh-parahippocampal -25.81 -30.39 -15.37 左海馬傍回 -
        if (should_output_deep) {
            new_col_val = AprcL.getListOf_Parahippocampal_Vol();
            new_col_val.add(0, "L_PHIP");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //68 L.ENTO ctx-lh-entorhinal -25.20 -5.71 -31.43 左嗅内野 連合野と海馬体との情報連絡を仲介
        if (should_output_deep) {
            new_col_val = AprcL.getListOf_Entorhinal_Vol();
            new_col_val.add(0, "L_ENTO");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //69 L.TMPP ctx-lh-temporalpole -32.05 10.66 -36.41 左側頭極 側頭葉の前端
        if (should_output_surf) {
            //none
            //new_col_val = AprcL.getListOf_Temporalpole_Vol();
            //new_col_val.add(0, "L_TMPP");
            //DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //70 L.ITG ctx-lh-inferiortemporal -51.47 -34.39 -18.96 左下側頭回 -
        if (should_output_surf) {
            System.out.println("L.ITG");
            new_col_val = AprcL.getListOf_Inferiortemporal_Vol();
            new_col_val.add(0, "L_ITG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //71 L.MTG ctx-lh-middletemporal -55.93 -26.30 -10.17 左中側頭回 -
        if (should_output_surf) {
            System.out.println("L.MTG");
            new_col_val = AprcL.getListOf_Middletemporal_Vol();
            new_col_val.add(0, "L_MTG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //72 L.BSTS ctx-lh-bankssts -50.01 -42.98 12.14 左上側頭溝壁 和名正しい？banks-of-the-superior-temporal-sulcus
        if (should_output_surf) {
            //k
            //new_col_val.add(0, "L_BSTS");
            //DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //73 L.STG ctx-lh-superiortemporal -51.31 -11.13 -3.50 左上側頭回 -
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Superiortemporal_Vol();
            new_col_val.add(0, "L_STG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //74 L.TTG ctx-lh-transversetemporal -45.75 -20.05 8.55 左横側頭回 聴覚中枢
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Transversetemporal_Vol();
            new_col_val.add(0, "L_TTG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //75 L.INSU ctx-lh-insula -34.39 -1.46 2.96 左島 -
        if (should_output_surf) {
            new_col_val = AprcL.getListOf_Insula_Vol();
            new_col_val.add(0, "L_INSU");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //76 L.THAL Left-Thalamus-Proper -10.64 -18.12 7.32 左視床 -
        if (should_output_deep) {
            new_col_val = Asg.getListOf_LeftThalamusProper_Vol();
            new_col_val.add(0, "L_THAL");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //77 L.CAUD Left-Caudate -12.35 8.68 10.48 左尾状核 -
        if (should_output_deep) {
            new_col_val = Asg.getListOf_LeftCaudate_Vol();
            new_col_val.add(0, "L_CAUD");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //78 L.PUTA Left-Putamen -25.72 0.27 0.36 左被殻 -
        if (should_output_deep) {
            new_col_val = Asg.getListOf_LeftPutamen_Vol();
            new_col_val.add(0, "L_PUTA");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //79 L.PALL Left-Pallidum -18.87 -2.77 -2.72 左淡蒼球 -
        if (should_output_deep) {
            new_col_val = Asg.getListOf_LeftPallidum_Vol();
            new_col_val.add(0, "L_PALL");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //80 L.ACMB Left-Accumbens-area -5.84 11.80 -7.18 左側坐核 新皮質-線条体-淡蒼球という経路と並行の回路を形成
        if (should_output_deep) {
            new_col_val = Asg.getListOf_LeftAccumbensarea_Vol();
            new_col_val.add(0, "L_ACMB");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //81 L.HIPP Left-Hippocampus -24.32 -20.34 -14.12 左海馬 -
        if (should_output_deep) {
            new_col_val = Asg.getListOf_LeftHippocampus_Vol();
            new_col_val.add(0, "L_HIPP");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //82 L.AMYG Left-Amygdala -20.69 -1.36 -21.72 左扁桃体 -
        if (should_output_deep) {
            new_col_val = Asg.getListOf_LeftAmygdala_Vol();
            new_col_val.add(0, "L_AMYG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //83 BSTM Brain-Stem -0.58 -29.84 -27.45 脳幹 -
        if (should_output_deep) {
            new_col_val = Asg.getListOf_BrainStem_Vol();
            new_col_val.add(0, "BSTM");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        /**
         * Hippo Subfields
         */
        //Rt Hippocampal Tail
        if (should_output_hipsub) {
            new_col_val = HS_Rt.getListOf_HippocampalTail_Vol();
            new_col_val.add(0, "Rt_HippoTail");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Rt Subiculum
        if (should_output_hipsub) {
            new_col_val = HS_Rt.getListOf_Subiculum_Vol();
            new_col_val.add(0, "Rt_Subiculum");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Rt CA1
        if (should_output_hipsub) {
            new_col_val = HS_Rt.getListOf_CA1_Vol();
            new_col_val.add(0, "Rt_CA1");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Rt Hippocampal fissure
        if (should_output_hipsub) {
            new_col_val = HS_Rt.getListOf_HippocampalFissure_Vol();
            new_col_val.add(0, "Rt_HP_Fissure");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Rt Presubiculum
        if (should_output_hipsub) {
            new_col_val = HS_Rt.getListOf_Presubiculum_Vol();
            new_col_val.add(0, "Rt_Presubiculum");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Rt Parasubiculum
        if (should_output_hipsub) {
            new_col_val = HS_Rt.getListOf_Parasubiculum_Vol();
            new_col_val.add(0, "Rt_Parasubiculum");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Rt Molecular_layer_HP
        if (should_output_hipsub) {
            new_col_val = HS_Rt.getListOf_MolecularLayerHP_Vol();
            new_col_val.add(0, "Rt_MolecularLayerHP");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Rt GC_ML_DG
        if (should_output_hipsub) {
            new_col_val = HS_Rt.getListOf_GC_ML_DG_Vol();
            new_col_val.add(0, "Rt_GC_ML_DG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Rt CA3
        if (should_output_hipsub) {
            new_col_val = HS_Rt.getListOf_CA3_Vol();
            new_col_val.add(0, "Rt_CA3");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Rt CA4
        if (should_output_hipsub) {
            new_col_val = HS_Rt.getListOf_CA4_Vol();
            new_col_val.add(0, "Rt_CA4");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Rt Fimbria
        if (should_output_hipsub) {
            new_col_val = HS_Rt.getListOf_Fimbria_Vol();
            new_col_val.add(0, "Rt_Fimbria");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Rt HATA
        if (should_output_hipsub) {
            new_col_val = HS_Rt.getListOf_HATA_Vol();
            new_col_val.add(0, "Rt_HATA");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Rt Whole_hippocampus
        if (should_output_hipsub) {
            new_col_val = HS_Rt.getListOf_WholeHippocampus_Vol();
            new_col_val.add(0, "Rt_WholeHippo");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Lt Hippocampal Tail
        if (should_output_hipsub) {
            new_col_val = HS_Lt.getListOf_HippocampalTail_Vol();
            new_col_val.add(0, "Lt_HippoTail");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Lt Subiculum
        if (should_output_hipsub) {
            new_col_val = HS_Lt.getListOf_Subiculum_Vol();
            new_col_val.add(0, "Lt_Subiculum");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Lt CA1
        if (should_output_hipsub) {
            new_col_val = HS_Lt.getListOf_CA1_Vol();
            new_col_val.add(0, "Lt_CA1");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Lt Hippocampal fissure
        if (should_output_hipsub) {
            new_col_val = HS_Lt.getListOf_HippocampalFissure_Vol();
            new_col_val.add(0, "Lt_HP_Fissure");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Lt Presubiculum
        if (should_output_hipsub) {
            new_col_val = HS_Lt.getListOf_Presubiculum_Vol();
            new_col_val.add(0, "Lt_Presubiculum");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Lt Parasubiculum
        if (should_output_hipsub) {
            System.out.println("Lt Parasubiculum");
            new_col_val = HS_Lt.getListOf_Parasubiculum_Vol();
            new_col_val.add(0, "Lt_Parasubiculum");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Lt Molecular_layer_HP
        if (should_output_hipsub) {
            System.out.println("Lt Molecular_layer_HP");
            new_col_val = HS_Lt.getListOf_MolecularLayerHP_Vol();
            new_col_val.add(0, "Lt_MolecularLayerHP");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Lt GC_ML_DG
        if (should_output_hipsub) {
            new_col_val = HS_Lt.getListOf_GC_ML_DG_Vol();
            new_col_val.add(0, "Lt_GC_ML_DG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Lt CA3
        if (should_output_hipsub) {
            new_col_val = HS_Lt.getListOf_CA3_Vol();
            new_col_val.add(0, "Lt_CA3");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Lt CA4
        if (should_output_hipsub) {
            new_col_val = HS_Lt.getListOf_CA4_Vol();
            new_col_val.add(0, "Lt_CA4");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Lt Fimbria
        if (should_output_hipsub) {
            new_col_val = HS_Lt.getListOf_Fimbria_Vol();
            new_col_val.add(0, "Lt_Fimbria");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Lt HATA
        if (should_output_hipsub) {
            new_col_val = HS_Lt.getListOf_HATA_Vol();
            new_col_val.add(0, "Lt_HATA");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //Lt Whole_hippocampus
        if (should_output_hipsub) {
            new_col_val = HS_Lt.getListOf_WholeHippocampus_Vol();
            new_col_val.add(0, "Lt_WholeHippo");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }


        /**
         * テスト用６領域を出力
         */
        //1 R.LOFC ctx-rh-lateralorbitofrontal 26.76 32.66 -12.88 右外側眼窩前頭皮質 -
        if (should_output_6nodes) {
            new_col_val = AprcR.getListOf_Lateralorbitofrontal_Vol();
            new_col_val.add(0, "R_LOFC");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //2 R.OIFG ctx-rh-parsorbitalis 45.46 36.29 -10.44 右眼窩部 -
        if (should_output_6nodes) {
            new_col_val = AprcR.getListOf_Parsorbitalis_Vol();
            new_col_val.add(0, "R_OIFG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //36 R.CAUD Right-Caudate 16.32 8.63 10.71 右尾状核 -
        if (should_output_6nodes) {
            new_col_val = Asg.getListOf_RightCaudate_Vol();
            new_col_val.add(0, "R_CAUD");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //42 L.LOFC ctx-lh-lateralorbitofrontal -21.81 34.18 -13.40 左外側眼窩前頭皮質 -
        if (should_output_6nodes) {
            new_col_val = AprcL.getListOf_Lateralorbitofrontal_Vol();
            new_col_val.add(0, "L_LOFC");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //43 L.OIFG ctx-lh-parsorbitalis -40.58 37.38 -8.54 左眼窩部 -
        if (should_output_6nodes) {
            new_col_val = AprcL.getListOf_Parsorbitalis_Vol();
            new_col_val.add(0, "L_OIFG");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        //77 L.CAUD Left-Caudate -12.35 8.68 10.48 左尾状核 -
        if (should_output_6nodes) {
            new_col_val = Asg.getListOf_LeftCaudate_Vol();
            new_col_val.add(0, "L_CAUD");
            DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");
        }

        /**
         * 頭蓋内体積を出力
         *
         */
        //Estimated Total Intracranial Volume
        new_col_val = Asg.getListOf_eTIV();
        new_col_val.add(0, "eTIV");
        DataFrameConstructer.addValuesAsNewCol(new_col_val, new File(output_folder_path + "StartPointDataFrame.txt"), "\t");

    }

}

