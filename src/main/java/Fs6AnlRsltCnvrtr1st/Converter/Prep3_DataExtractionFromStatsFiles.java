package Fs6AnlRsltCnvrtr1st.Converter;

import Fs6AnlRsltCnvrtr1st.DataClass.*;
import IuCollectionIO.CollectionWriterAndReader_ver7;

import javax.swing.*;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

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
            core(args[0], args[1], Boolean.parseBoolean(args[2]), Boolean.parseBoolean(args[3]), Boolean.parseBoolean(args[4]), Boolean.parseBoolean(args[5]));
            //core(args[0], args[1], Boolean.parseBoolean(args[2]), Boolean.parseBoolean(args[3]));
            //core("./Data/20161124_FS6_ANL_RSLT", "./ConvertedData", true, true);
        } else {
            System.out.println("引数を確認してください。");
        }

    }

    public static void core(String read_target_folder_path, String output_folder_path,
                            boolean should_output_surf, boolean should_output_deep, boolean should_output_hipsub,
                            boolean should_output_6nodes) {

        read_target_folder_path = read_target_folder_path.replaceAll("\\\\", "/");
        //読み取り対象フォルダのパスの調整
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
        CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_SubjectName(), new File(output_folder_path + "DataOrderInfo.txt"));

        /**
         * 77領域の体積を横断的に出力
         * （83-6領域）
         * TODO: できれば83領域ぐらいでやりたいのだが、そもそも領域名がなかったり、欠損値があったり
         */
        //1 R.LOFC ctx-rh-lateralorbitofrontal 26.76 32.66 -12.88 右外側眼窩前頭皮質 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Lateralorbitofrontal_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_LOFC.txt"));
        }

        //2 R.OIFG ctx-rh-parsorbitalis 45.46 36.29 -10.44 右眼窩部 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Parsorbitalis_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_OIFG.txt"));
        }

        //3 R.FRTP ctx-rh-frontalpole 14.11 60.27 -12.49 右前頭極 大脳の一番前方の部分
        if (should_output_surf) {
            //none
        }

        //4 R.MOFC ctx-rh-medialorbitofrontal 10.32 41.87 -12.61 右中眼窩前頭皮質 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Medialorbitofrontal_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_MOFC.txt"));
        }

        //5 R.PTRI ctx-rh-parstriangularis 46.35 33.36 4.39 右三角部 前頭葉の左下前頭回にあるブローカ野の一部でブロードマンの脳地図の45野
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Parstriangularis_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_PTRI.txt"));
        }

        //6 R.POPE ctx-rh-parsopercularis 47.95 14.60 13.80 右弁蓋部 味覚に関与
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Parsopercularis_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_POPE.txt"));
        }

        //7 R.RMFG ctx-rh-rostralmiddlefrontal 34.98 42.64 16.63 右吻側中前頭回 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Rostralmiddlefrontal_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_RMFG.txt"));
        }

        //8 R.SFG ctx-rh-superiorfrontal 16.85 25.18 42.49 右上前頭回 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Superiorfrontal_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_SFG.txt"));
        }

        //9 R.CMFG ctx-rh-caudalmiddlefrontal 39.99 7.29 47.68 右尾側中前頭回 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Caudalmiddlefrontal_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_CMFG.txt"));
        }

        //10 R.PREC ctx-rh-precentral 39.91 -11.66 47.82 右中心前回 体性運動
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOF_Precentral_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_PREC.txt"));
        }

        //11 R.PRCL ctx-rh-paracentral 11.31 -31.56 59.81 右中心傍小葉 下腿部と足の運動を司る
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Paracentral_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_PRCL.txt"));
        }

        //12 R.RCIN ctx-rh-rostralanteriorcingulate 10.2 40.36 4.09 右吻側前帯状回 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Rostralanteriorcingulate_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_RCIN.txt"));
        }

        //13 R.CCIN ctx-rh-caudalanteriorcingulate 9.58 14.58 28.80 右尾側前帯状回 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Caudalanteriorcingulate_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_CCIN.txt"));
        }

        //14 R.PCIN ctx-rh-posteriorcingulate 9.61 -21.42 39.98 右帯状回後部 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Posteriorcingulate_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_PCIN.txt"));
        }

        //15 R.ICIN ctx-rh-isthmuscingulate 10.14 -45.29 21.70 右帯状回峡 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Isthmuscingulate_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_ICIN.txt"));
        }

        //16 R.PSTCG ctx-rh-postcentral 44.75 -25.37 49.15 右中心後回 体性感覚野
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Postcentral_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_PSTCGG.txt"));
        }

        //17 R.SPMG ctx-rh-supramarginal 51.68 -37.02 38.04 右縁上回 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Supramarginal_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_SPMG.txt"));
        }

        //18 R.SPL ctx-rh-superiorparietal 21.95 -65.86 50.08 右上頭頂葉 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Superiorparietal_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_SPL.txt"));
        }

        //19 R.IPL ctx-rh-inferiorparietal 42.27 -67.11 30.38 右下頭頂葉 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Inferiorparietal_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_IPL.txt"));
        }

        //20 R.PCUN ctx-rh-precuneus 11.06 -58.35 41.54 右楔前部 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Precuneus_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_PCUN.txt"));
        }

        //21 R.CUNE ctx-rh-cuneus 7.49 -80.82 22.13 右楔状葉 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Cuneus_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_CUNE.txt"));
        }

        //22 R.CALS ctx-rh-pericalcarine 12.19 -81.97 10.37 右鳥距溝周囲 視覚連合野
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Pericalcarine_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_CALS.txt"));
        }

        //23 R.LOS ctx-rh-lateraloccipital 30.66 -88.75 -0.05 右後頭極 和名正しい？
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Lateraloccipital_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_LOS.txt"));
        }

        //24 R.LING ctx-rh-lingual 13.30 -68.60 0.88 右舌状回 後頭葉内側面で鳥距溝の下面に存在
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Lingual_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_LING.txt"));
        }

        //25 R.FUSI ctx-rh-fusiform 33.68 -47.45 -16.25 右紡錘状回 側頭葉の一部。後頭側頭回とも呼ばれる。
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Fusiform_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_FUSI.txt"));
        }

        //26 R.PHIP ctx-rh-parahippocampal 27.61 -32.33 -14.71 右海馬傍回 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Parahippocampal_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_PHIP.txt"));
        }

        //27 R.ENTO ctx-rh-entorhinal 25.02 -6.43 -30.61 右嗅内野 連合野と海馬体との情報連絡を仲介
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Entorhinal_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_ENTO.txt"));
        }

        //28 R.TMPP ctx-rh-temporalpole 36.73 10.12 -34.77 右側頭極 側頭葉の前端
        if (should_output_surf) {
            //CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Temporalpole_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_TMPP.txt"));
        }

        //29 R.ITG ctx-rh-inferiortemporal 50.66 -32.22 -22.62 右下側頭回 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Inferiortemporal_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_ITG.txt"));
        }

        //30 R.MTG ctx-rh-middletemporal 56.29 -25.56 -12.02 右中側頭回 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Middletemporal_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_MTG.txt"));
        }

        //31 R.BSTS ctx-rh-bankssts 50.30 -44.37 11.71 右上側頭溝壁 和名正しい？banks-of-the-superior-temporal-sulcus
        if (should_output_surf) {
            //none
        }

        //32 R.STG ctx-rh-superiortemporal 54.57 -14.21 -3.53 右上側頭回 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Superiortemporal_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_STG.txt"));
        }

        //33 R.TTG ctx-rh-transversetemporal 47.34 -22.37 9.44 右横側頭回 聴覚中枢
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Transversetemporal_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_TTG.txt"));
        }

        //34 R.INSU ctx-rh-insula 37.28 -3.14 2.58 右島 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Insula_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_INSU.txt"));
        }

        //35 R.THAL Right-Thalamus-Proper 12.38 -18.04 8.04 右視床 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_RightThalamusProper_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_THAL.txt"));
        }

        //36 R.CAUD Right-Caudate 16.32 8.63 10.71 右尾状核 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_RightCaudate_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_CAUD.txt"));
        }

        //37 R.PUTA Right-Putamen 28.05 0.14 -0.23 右被殻 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_RightPutamen_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_PUTA.txt"));
        }

        //38 R.PALL Right-Pallidum 21.69 -4.00 -1.68 右淡蒼球 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_RightPallidum_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_PALL.txt"));
        }

        //39 R.ACMB Right-Accumbens-area 10.62 11.13 -7.53 右側坐核 新皮質-線条体-淡蒼球という経路と並行の回路を形成
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_RightAccumbensarea_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_ACMB.txt"));
        }

        //40 R.HIPP Right-Hippocampus 25.66 -22.55 -11.85 右海馬 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_RightHippocampus_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_HIPP.txt"));
        }

        //41 R.AMYG Right-Amygdala 25.56 -4.28 -19.74 右扁桃体 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_RightAmygdala_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_AMYG.txt"));
        }

        //42 L.LOFC ctx-lh-lateralorbitofrontal -21.81 34.18 -13.40 左外側眼窩前頭皮質 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Lateralorbitofrontal_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_LOFC.txt"));
        }

        //43 L.OIFG ctx-lh-parsorbitalis -40.58 37.38 -8.54 左眼窩部 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Parsorbitalis_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_OIFG.txt"));
        }

        //44 L.FRTP ctx-lh-frontalpole -8.55 61.09 -4.09 左前頭極 大脳の一番前方の部分
        if (should_output_surf) {
            //none
        }

        //45 L.MOFC ctx-lh-medialorbitofrontal -6.18 42.72 -10.32 左中眼窩前頭皮質 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Medialorbitofrontal_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_MOFC.txt"));
        }

        //46 L.PTRI ctx-lh-parstriangularis -40.05 35.96 6.58 左三角部 前頭葉の左下前頭回にあるブローカ野の一部でブロードマンの脳地図の45野
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Parstriangularis_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_PTRI.txt"));
        }

        //47 L.POPE ctx-lh-parsopercularis -42.97 17.76 14.33 左弁蓋部 味覚に関与
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Parsopercularis_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_POPE.txt"));
        }

        //48 L.RMFG ctx-lh-rostralmiddlefrontal -28.78 43.39 18.16 左吻側中前頭回 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Rostralmiddlefrontal_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_RMFG.txt"));
        }

        //49 L.SFG ctx-lh-superiorfrontal -9.47 25.77 44.12 左上前頭回 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Superiorfrontal_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_SFG.txt"));
        }

        //50 L.CMFG ctx-lh-caudalmiddlefrontal -30.68 10.09 48.59 左尾側中前頭回 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Caudalmiddlefrontal_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_CMFG.txt"));
        }

        //51 L.PREC ctx-lh-precentral -35.49 -9.59 47.33 左中心前回 体性運動
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOF_Precentral_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_PREC.txt"));
        }

        //52 L.PRCL ctx-lh-paracentral -8.94 -28.30 62.68 左中心傍小葉 下腿部と足の運動を司る
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Paracentral_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_PRCL.txt"));
        }

        //53 L.RCIN ctx-lh-rostralanteriorcingulate -4.09 39.54 6.87 左吻側前帯状回 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Rostralanteriorcingulate_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_RCIN.txt"));
        }

        //54 L.CCIN ctx-lh-caudalanteriorcingulate -3.59 17.47 28.97 左尾側前帯状回 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Caudalanteriorcingulate_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_CCIN.txt"));
        }

        //55 L.PCIN ctx-lh-posteriorcingulate -6.35 -22.66 38.46 左帯状回後部 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Posteriorcingulate_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_PCIN.txt"));
        }

        //56 L.ICIN ctx-lh-isthmuscingulate -9.06 -44.99 19.87 左帯状回峡 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Isthmuscingulate_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_ICIN.txt"));
        }

        //57 L.PSTCG ctx-lh-postcentral -41.98 -23.73 49.50 左中心後回 体性感覚野
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Postcentral_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_PSTCGG.txt"));
        }

        //58 L.SPMG ctx-lh-supramarginal -49.48 -37.05 36.35 左縁上回 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Supramarginal_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_SPMG.txt"));
        }

        //59 L.SPL ctx-lh-superiorparietal -20.71 -65.49 47.99 左上頭頂葉 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Superiorparietal_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_SPL.txt"));
        }

        //60 L.IPL ctx-lh-inferiorparietal -38.87 -71.16 29.99 左下頭頂葉 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Inferiorparietal_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_IPL.txt"));
        }

        //61 L.PCUN ctx-lh-precuneus -11.04 -57.34 40.44 左楔前部 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Precuneus_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_PCUN.txt"));
        }

        //62 L.CUNE ctx-lh-cuneus -6.88 -81.61 19.57 左楔状葉 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Cuneus_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_CUNE.txt"));
        }

        //63 L.CALS ctx-lh-pericalcarine -12.66 -82.54 8.88 左鳥距溝周囲 視覚連合野
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Pericalcarine_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_CALS.txt"));
        }

        //64 L.LOS ctx-lh-lateraloccipital -28.61 -90.79 -1.00 左後頭極 和名正しい？
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Lateraloccipital_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_LOS.txt"));
        }

        //65 L.LING ctx-lh-lingual -14.04 -71.75 -0.47 左舌状回 後頭葉内側面で鳥距溝の下面に存在
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Lingual_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_LING.txt"));
        }

        //66 L.FUSI ctx-lh-fusiform -36.53 -42.62 -17.62 左紡錘状回 側頭葉の一部。後頭側頭回とも呼ばれる。
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Fusiform_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_FUSI.txt"));
        }

        //67 L.PHIP ctx-lh-parahippocampal -25.81 -30.39 -15.37 左海馬傍回 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Parahippocampal_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_PHIP.txt"));
        }

        //68 L.ENTO ctx-lh-entorhinal -25.20 -5.71 -31.43 左嗅内野 連合野と海馬体との情報連絡を仲介
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Entorhinal_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_ENTO.txt"));
        }

        //69 L.TMPP ctx-lh-temporalpole -32.05 10.66 -36.41 左側頭極 側頭葉の前端
        if (should_output_surf) {
            //CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Temporalpole_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_TMPP.txt"));
        }

        //70 L.ITG ctx-lh-inferiortemporal -51.47 -34.39 -18.96 左下側頭回 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Inferiortemporal_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_ITG.txt"));
        }

        //71 L.MTG ctx-lh-middletemporal -55.93 -26.30 -10.17 左中側頭回 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Middletemporal_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_MTG.txt"));
        }

        //72 L.BSTS ctx-lh-bankssts -50.01 -42.98 12.14 左上側頭溝壁 和名正しい？banks-of-the-superior-temporal-sulcus
        if (should_output_surf) {
            //none
        }

        //73 L.STG ctx-lh-superiortemporal -51.31 -11.13 -3.50 左上側頭回 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Superiortemporal_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_STG.txt"));
        }

        //74 L.TTG ctx-lh-transversetemporal -45.75 -20.05 8.55 左横側頭回 聴覚中枢
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Transversetemporal_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_TTG.txt"));
        }

        //75 L.INSU ctx-lh-insula -34.39 -1.46 2.96 左島 -
        if (should_output_surf) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Insula_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_INSU.txt"));
        }

        //76 L.THAL Left-Thalamus-Proper -10.64 -18.12 7.32 左視床 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_LeftThalamusProper_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_THAL.txt"));
        }

        //77 L.CAUD Left-Caudate -12.35 8.68 10.48 左尾状核 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_LeftCaudate_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_CAUD.txt"));
        }

        //78 L.PUTA Left-Putamen -25.72 0.27 0.36 左被殻 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_LeftPutamen_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_PUTA.txt"));
        }

        //79 L.PALL Left-Pallidum -18.87 -2.77 -2.72 左淡蒼球 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_LeftPallidum_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_PALL.txt"));
        }

        //80 L.ACMB Left-Accumbens-area -5.84 11.80 -7.18 左側坐核 新皮質-線条体-淡蒼球という経路と並行の回路を形成
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_LeftAccumbensarea_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_ACMB.txt"));
        }

        //81 L.HIPP Left-Hippocampus -24.32 -20.34 -14.12 左海馬 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_LeftHippocampus_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_HIPP.txt"));
        }

        //82 L.AMYG Left-Amygdala -20.69 -1.36 -21.72 左扁桃体 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_LeftAmygdala_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_AMYG.txt"));
        }

        //83 BSTM Brain-Stem -0.58 -29.84 -27.45 脳幹 -
        if (should_output_deep) {
            CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_BrainStem_Vol(), new File(output_folder_path + "VolDataOfEachArea/BSTM.txt"));
        }

        /**
         * Hippo Subfields
         */
        //Rt Hippocampal Tail
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Rt.getListOf_HippocampalTail_Vol(), new File(output_folder_path + "VolDataOfEachArea/Rt_HippoTail.txt"));
        }

        //Rt Subiculum
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Rt.getListOf_Subiculum_Vol(), new File(output_folder_path + "VolDataOfEachArea/Rt_Subiculum.txt"));
        }
        //Rt CA1
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Rt.getListOf_CA1_Vol(), new File(output_folder_path + "VolDataOfEachArea/Rt_CA1.txt"));
        }
        //Rt Hippocampal fissure
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Rt.getListOf_HippocampalFissure_Vol(), new File(output_folder_path + "VolDataOfEachArea/Rt_HP_Fissure.txt"));
        }
        //Rt Presubiculum
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Rt.getListOf_Presubiculum_Vol(), new File(output_folder_path + "VolDataOfEachArea/Rt_Presubiculum.txt"));
        }
        //Rt Parasubiculum
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Rt.getListOf_Parasubiculum_Vol(), new File(output_folder_path + "VolDataOfEachArea/Rt_Parasubiculum.txt"));
        }
        //Rt Molecular_layer_HP
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Rt.getListOf_MolecularLayerHP_Vol(), new File(output_folder_path + "VolDataOfEachArea/Rt_MolecularLayerHP.txt"));
        }
        //Rt GC_ML_DG
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Rt.getListOf_GC_ML_DG_Vol(), new File(output_folder_path + "VolDataOfEachArea/Rt_GC_ML_DG.txt"));
        }
        //Rt CA3
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Rt.getListOf_CA3_Vol(), new File(output_folder_path + "VolDataOfEachArea/Rt_CA3.txt"));
        }
        //Rt CA4
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Rt.getListOf_CA4_Vol(), new File(output_folder_path + "VolDataOfEachArea/Rt_CA4.txt"));
        }
        //Rt Fimbria
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Rt.getListOf_Fimbria_Vol(), new File(output_folder_path + "VolDataOfEachArea/Rt_Fimbria.txt"));
        }
        //Rt HATA
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Rt.getListOf_HATA_Vol(), new File(output_folder_path + "VolDataOfEachArea/Rt_HATA.txt"));
        }
        //Rt Whole_hippocampus
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Rt.getListOf_WholeHippocampus_Vol(), new File(output_folder_path + "VolDataOfEachArea/Rt_WholeHippo.txt"));
        }
        //Lt Hippocampal Tail
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Lt.getListOf_HippocampalTail_Vol(), new File(output_folder_path + "VolDataOfEachArea/Lt_HippoTail.txt"));
        }

        //Lt Subiculum
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Lt.getListOf_Subiculum_Vol(), new File(output_folder_path + "VolDataOfEachArea/Lt_Subiculum.txt"));
        }
        //Lt CA1
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Lt.getListOf_CA1_Vol(), new File(output_folder_path + "VolDataOfEachArea/Lt_CA1.txt"));
        }
        //Lt Hippocampal fissure
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Lt.getListOf_HippocampalFissure_Vol(), new File(output_folder_path + "VolDataOfEachArea/Lt_HP_Fissure.txt"));
        }
        //Lt Presubiculum
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Lt.getListOf_Presubiculum_Vol(), new File(output_folder_path + "VolDataOfEachArea/Lt_Presubiculum.txt"));
        }
        //Lt Parasubiculum
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Lt.getListOf_Parasubiculum_Vol(), new File(output_folder_path + "VolDataOfEachArea/Lt_Parasubiculum.txt"));
        }
        //Lt Molecular_layer_HP
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Lt.getListOf_MolecularLayerHP_Vol(), new File(output_folder_path + "VolDataOfEachArea/Lt_MolecularLayerHP.txt"));
        }
        //Lt GC_ML_DG
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Lt.getListOf_GC_ML_DG_Vol(), new File(output_folder_path + "VolDataOfEachArea/Lt_GC_ML_DG.txt"));
        }
        //Lt CA3
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Lt.getListOf_CA3_Vol(), new File(output_folder_path + "VolDataOfEachArea/Lt_CA3.txt"));
        }
        //Lt CA4
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Lt.getListOf_CA4_Vol(), new File(output_folder_path + "VolDataOfEachArea/Lt_CA4.txt"));
        }
        //Lt Fimbria
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Lt.getListOf_Fimbria_Vol(), new File(output_folder_path + "VolDataOfEachArea/Lt_Fimbria.txt"));
        }
        //Lt HATA
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Lt.getListOf_HATA_Vol(), new File(output_folder_path + "VolDataOfEachArea/Lt_HATA.txt"));
        }
        //Lt Whole_hippocampus
        if (should_output_hipsub) {
            CollectionWriterAndReader_ver7.outputToTextfile(HS_Lt.getListOf_WholeHippocampus_Vol(), new File(output_folder_path + "VolDataOfEachArea/Lt_WholeHippo.txt"));
        }

        /**
         * テスト用６領域を出力
         */
        //1 R.LOFC ctx-rh-lateralorbitofrontal 26.76 32.66 -12.88 右外側眼窩前頭皮質 -
        if (should_output_6nodes) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Lateralorbitofrontal_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_LOFC.txt"));
        }

        //2 R.OIFG ctx-rh-parsorbitalis 45.46 36.29 -10.44 右眼窩部 -
        if (should_output_6nodes) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcR.getListOf_Parsorbitalis_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_OIFG.txt"));
        }

        //36 R.CAUD Right-Caudate 16.32 8.63 10.71 右尾状核 -
        if (should_output_6nodes) {
            CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_RightCaudate_Vol(), new File(output_folder_path + "VolDataOfEachArea/R_CAUD.txt"));
        }

        //42 L.LOFC ctx-lh-lateralorbitofrontal -21.81 34.18 -13.40 左外側眼窩前頭皮質 -
        if (should_output_6nodes) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Lateralorbitofrontal_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_LOFC.txt"));
        }

        //43 L.OIFG ctx-lh-parsorbitalis -40.58 37.38 -8.54 左眼窩部 -
        if (should_output_6nodes) {
            CollectionWriterAndReader_ver7.outputToTextfile(AprcL.getListOf_Parsorbitalis_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_OIFG.txt"));
        }


        //77 L.CAUD Left-Caudate -12.35 8.68 10.48 左尾状核 -
        if (should_output_6nodes) {
            CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_LeftCaudate_Vol(), new File(output_folder_path + "VolDataOfEachArea/L_CAUD.txt"));
        }
        /**
         * 頭蓋内体積を出力
         */
        CollectionWriterAndReader_ver7.outputToTextfile(Asg.getListOf_eTIV(), new File(output_folder_path + "eTIV.txt")); //Estimated Total Intracranial Volume

    }

}

