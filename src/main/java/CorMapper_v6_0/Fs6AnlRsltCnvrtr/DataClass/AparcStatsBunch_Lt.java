package CorMapper_v6_0.Fs6AnlRsltCnvrtr.DataClass;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by issey on 2016/11/16.
 */
public class AparcStatsBunch_Lt extends Bunch {

    public static void main(String[] args) {
        String folder_path_str = "file:/C:/Users/issey/Documents/Dropbox/docroot/20161124_FS6_ANL_RSLT";

        //コンストラクタ
        AparcStatsBunch_Lt bas = new AparcStatsBunch_Lt(folder_path_str);
        System.out.println(bas.getListOf_SubjectName());
        System.out.println(bas.getListOf_TotalNumVert());
        System.out.println();
    }

    //読み取り対象ファイル絞込用フィルタに使う文字列
    static String FilteringString = "lh.aparc.DKTatlas.stats";

    //ファイル名とAparcStatsの組の束
    ArrayList<AparcStats> DataListOf_AparcStats = new ArrayList();//aparc.statsの束

    //横断的情報の束
    ArrayList<Double> ListOf_Caudalanteriorcingulate_Vol = new ArrayList();
    ArrayList<Double> ListOf_Caudalmiddlefrontal_Vol = new ArrayList();
    ArrayList<Double> ListOf_Cuneus_Vol = new ArrayList();
    ArrayList<Double> ListOf_Entorhinal_Vol = new ArrayList();
    ArrayList<Double> ListOf_Fusiform_Vol = new ArrayList();
    ArrayList<Double> ListOf_Inferiorparietal_Vol = new ArrayList();
    ArrayList<Double> ListOf_Inferiortemporal_Vol = new ArrayList();
    ArrayList<Double> ListOf_Isthmuscingulate_Vol = new ArrayList();
    ArrayList<Double> ListOf_Lateraloccipital_Vol = new ArrayList();
    ArrayList<Double> ListOf_Lateralorbitofrontal_Vol = new ArrayList();
    ArrayList<Double> ListOf_Lingual_Vol = new ArrayList();
    ArrayList<Double> ListOf_Medialorbitofrontal_Vol = new ArrayList();
    ArrayList<Double> ListOf_Middletemporal_Vol = new ArrayList();
    ArrayList<Double> ListOf_Parahippocampal_Vol = new ArrayList();
    ArrayList<Double> ListOf_Paracentral_Vol = new ArrayList();
    ArrayList<Double> ListOf_Parsopercularis_Vol = new ArrayList();
    ArrayList<Double> ListOf_Parsorbitalis_Vol = new ArrayList();
    ArrayList<Double> ListOf_Parstriangularis_Vol = new ArrayList();
    ArrayList<Double> ListOf_Pericalcarine_Vol = new ArrayList();
    ArrayList<Double> ListOf_Postcentral_Vol = new ArrayList();
    ArrayList<Double> ListOf_Posteriorcingulate_Vol = new ArrayList();
    ArrayList<Double> ListOF_Precentral_Vol = new ArrayList();
    ArrayList<Double> ListOf_Precuneus_Vol = new ArrayList();
    ArrayList<Double> ListOf_Rostralanteriorcingulate_Vol = new ArrayList();
    ArrayList<Double> ListOf_Rostralmiddlefrontal_Vol = new ArrayList();
    ArrayList<Double> ListOf_Superiorfrontal_Vol = new ArrayList();
    ArrayList<Double> ListOf_Superiorparietal_Vol = new ArrayList();
    ArrayList<Double> ListOf_Superiortemporal_Vol = new ArrayList();
    ArrayList<Double> ListOf_Supramarginal_Vol = new ArrayList();
    ArrayList<Double> ListOf_Transversetemporal_Vol = new ArrayList();
    ArrayList<Double> ListOf_Insula_Vol = new ArrayList();
    ArrayList<Double> ListOf_Temporalpole_Vol = new ArrayList();

    ArrayList<Integer> ListOf_TotalNumVert = new ArrayList();
    ArrayList<Double> ListOf_WhiteSurfArea = new ArrayList();
    ArrayList<Double> ListOf_MeanThickness = new ArrayList();
    ArrayList<Double> ListOf_BrainSegVol = new ArrayList();
    ArrayList<Double> ListOf_BrainSegVolNotVent = new ArrayList();
    ArrayList<Double> ListOf_BrainSegVolNotVentSurf = new ArrayList();
    ArrayList<Double> ListOf_CortexVol = new ArrayList();
    ArrayList<Double> ListOf_SupraTentorialVol = new ArrayList();
    ArrayList<Double> ListOf_SupraTentorialVolNotVent = new ArrayList();
    ArrayList<Double> ListOf_eTIV = new ArrayList();

    /* --------------------------------------------------------------------------- */

    public AparcStatsBunch_Lt(String aparc_stats_folder_path) {
        //スーパークラスのコンストラクト
        super(aparc_stats_folder_path, FilteringString);//これによりReadTargetFilesにデータが格納される

        System.out.println("読み取り対象ファイル:");
        for (File file : ReadTargetFiles) {
            System.out.println("    " + file.getAbsolutePath());
            DataListOf_AparcStats.add(new AparcStats(file.getAbsolutePath()));
        }
        System.out.println("    「*" + FilteringString + "」ファイル数: " + ReadTargetFiles.length);
        System.out.println();


        /**
         * 症例名、各脳領域の体積値のArrayListへの格納
         */
        //aseg_statsファイルの束
        HashMap<String, AparcStats.AreaInfo> area_info_map;
        Iterator it_for_area_info_map_key;
        String area_name;
        AparcStats.AreaInfo an_areainfo;

        //全てのaseg_statsの束の要素　および　全てのbrain_area情報の束の要素　について、値を取り出す。
        for (AparcStats data : DataListOf_AparcStats) {
            System.out.println("Now reading " + data.getFileName());
            //各Subjectがどのような順番で呼び出され処理されているかを記録するためSunjectListを作る
            ListOf_SubjectName.add(data.getSubjectName());

            //各脳領域情報の束を取り出す
            area_info_map = data.getAreaInfoMap();
            it_for_area_info_map_key = data.getAreaInfoMap().keySet().iterator();
            while (it_for_area_info_map_key.hasNext()) {
                area_name = (String) it_for_area_info_map_key.next();
                an_areainfo = area_info_map.get(area_name);

                if (area_name.equals("caudalanteriorcingulate")) {
                    ListOf_Caudalanteriorcingulate_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("caudalmiddlefrontal")) {
                    ListOf_Caudalmiddlefrontal_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("cuneus")) {
                    ListOf_Cuneus_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("entorhinal")) {
                    ListOf_Entorhinal_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("fusiform")) {
                    ListOf_Fusiform_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("inferiorparietal")) {
                    ListOf_Inferiorparietal_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("inferiortemporal")) {
                    ListOf_Inferiortemporal_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("isthmuscingulate")) {
                    ListOf_Isthmuscingulate_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("lateraloccipital")) {
                    ListOf_Lateraloccipital_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("lateralorbitofrontal")) {
                    ListOf_Lateralorbitofrontal_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("lingual")) {
                    ListOf_Lingual_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("medialorbitofrontal")) {
                    ListOf_Medialorbitofrontal_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("middletemporal")) {
                    ListOf_Middletemporal_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("parahippocampal")) {
                    ListOf_Parahippocampal_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("paracentral")) {
                    ListOf_Paracentral_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("parsopercularis")) {
                    ListOf_Parsopercularis_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("parsorbitalis")) {
                    ListOf_Parsorbitalis_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("parstriangularis")) {
                    ListOf_Parstriangularis_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("pericalcarine")) {
                    ListOf_Pericalcarine_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("postcentral")) {
                    ListOf_Postcentral_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("posteriorcingulate")) {
                    ListOf_Posteriorcingulate_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("precentral")) {
                    ListOF_Precentral_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("precuneus")) {
                    ListOf_Precuneus_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("rostralanteriorcingulate")) {
                    ListOf_Rostralanteriorcingulate_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("rostralmiddlefrontal")) {
                    ListOf_Rostralmiddlefrontal_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("superiorfrontal")) {
                    ListOf_Superiorfrontal_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("superiorparietal")) {
                    ListOf_Superiorparietal_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("superiortemporal")) {
                    ListOf_Superiortemporal_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("supramarginal")) {
                    ListOf_Supramarginal_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("transversetemporal")) {
                    ListOf_Transversetemporal_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("insula")) {
                    ListOf_Insula_Vol.add(an_areainfo.getGrayVol());
                } else if (area_name.equals("temporalpole")) {
                    ListOf_Temporalpole_Vol.add(an_areainfo.getGrayVol());
                } else {
                    System.err.println("    " + area_name + "は未定義です。");

                }
            }// end of while

            ListOf_TotalNumVert.add(data.getTotalNumVert());
            ListOf_WhiteSurfArea.add(data.getWhiteSurfArea());
            ListOf_MeanThickness.add(data.getMeanThickness());
            ListOf_BrainSegVol.add(data.getBrainSegVol());
            ListOf_BrainSegVolNotVent.add(data.getBrainSegVolNotVent());
            ListOf_BrainSegVolNotVentSurf.add(data.getBrainSegVolNotVentSurf());
            ListOf_CortexVol.add(data.getCortexVol());
            ListOf_SupraTentorialVol.add(data.getSupraTentorialVol());
            ListOf_SupraTentorialVolNotVent.add(data.getSupraTentorialVolNotVent());
            ListOf_eTIV.add(data.geteTIV());

        }// end of for
        System.out.println();
    }

    /* --------------------------------------------------------------------------- */

    public ArrayList<AparcStats> getDataListOf_AparcStats() {
        return DataListOf_AparcStats;
    }

    /* --------------------------------------------------------------------------- */
    public ArrayList<Double> getListOf_Caudalanteriorcingulate_Vol() {
        return ListOf_Caudalanteriorcingulate_Vol;
    }

    public ArrayList<Double> getListOf_Caudalmiddlefrontal_Vol() {
        return ListOf_Caudalmiddlefrontal_Vol;
    }

    public ArrayList<Double> getListOf_Cuneus_Vol() {
        return ListOf_Cuneus_Vol;
    }

    public boolean isDebug() {
        return debug;
    }

    public ArrayList<Double> getListOf_Entorhinal_Vol() {
        return ListOf_Entorhinal_Vol;
    }

    public ArrayList<Double> getListOf_Fusiform_Vol() {
        return ListOf_Fusiform_Vol;
    }

    public ArrayList<Double> getListOf_Inferiorparietal_Vol() {
        return ListOf_Inferiorparietal_Vol;
    }

    public ArrayList<Double> getListOf_Inferiortemporal_Vol() {
        return ListOf_Inferiortemporal_Vol;
    }

    public ArrayList<Double> getListOf_Insula_Vol() {
        return ListOf_Insula_Vol;
    }

    public ArrayList<Double> getListOf_Isthmuscingulate_Vol() {
        return ListOf_Isthmuscingulate_Vol;
    }

    public ArrayList<Double> getListOf_Lateraloccipital_Vol() {
        return ListOf_Lateraloccipital_Vol;
    }

    public ArrayList<Double> getListOf_Lateralorbitofrontal_Vol() {
        return ListOf_Lateralorbitofrontal_Vol;
    }

    public ArrayList<Double> getListOf_Lingual_Vol() {
        return ListOf_Lingual_Vol;
    }

    public ArrayList<Double> getListOf_Medialorbitofrontal_Vol() {
        return ListOf_Medialorbitofrontal_Vol;
    }

    public ArrayList<Double> getListOf_Middletemporal_Vol() {
        return ListOf_Middletemporal_Vol;
    }

    public ArrayList<Double> getListOf_Paracentral_Vol() {
        return ListOf_Paracentral_Vol;
    }

    public ArrayList<Double> getListOf_Parahippocampal_Vol() {
        return ListOf_Parahippocampal_Vol;
    }

    public ArrayList<Double> getListOf_Parsopercularis_Vol() {
        return ListOf_Parsopercularis_Vol;
    }

    public ArrayList<Double> getListOf_Parsorbitalis_Vol() {
        return ListOf_Parsorbitalis_Vol;
    }

    public ArrayList<Double> getListOf_Parstriangularis_Vol() {
        return ListOf_Parstriangularis_Vol;
    }

    public ArrayList<Double> getListOf_Pericalcarine_Vol() {
        return ListOf_Pericalcarine_Vol;
    }

    public ArrayList<Double> getListOf_Postcentral_Vol() {
        return ListOf_Postcentral_Vol;
    }

    public ArrayList<Double> getListOf_Posteriorcingulate_Vol() {
        return ListOf_Posteriorcingulate_Vol;
    }

    public ArrayList<Double> getListOF_Precentral_Vol() {
        return ListOF_Precentral_Vol;
    }

    public ArrayList<Double> getListOf_Precuneus_Vol() {
        return ListOf_Precuneus_Vol;
    }

    public ArrayList<Double> getListOf_Rostralanteriorcingulate_Vol() {
        return ListOf_Rostralanteriorcingulate_Vol;
    }

    public ArrayList<Double> getListOf_Rostralmiddlefrontal_Vol() {
        return ListOf_Rostralmiddlefrontal_Vol;
    }

    public ArrayList<Double> getListOf_Superiorfrontal_Vol() {
        return ListOf_Superiorfrontal_Vol;
    }

    public ArrayList<Double> getListOf_Superiorparietal_Vol() {
        return ListOf_Superiorparietal_Vol;
    }

    public ArrayList<Double> getListOf_Superiortemporal_Vol() {
        return ListOf_Superiortemporal_Vol;
    }

    public ArrayList<Double> getListOf_Supramarginal_Vol() {
        return ListOf_Supramarginal_Vol;
    }

    public ArrayList<Double> getListOf_Transversetemporal_Vol() {
        return ListOf_Transversetemporal_Vol;
    }

    public ArrayList<Double> getListOf_Temporalpole_Vol() {
        return ListOf_Temporalpole_Vol;
    }

    public ArrayList<Double> getListOf_BrainSegVol() {
        return ListOf_BrainSegVol;
    }

    public ArrayList<Double> getListOf_BrainSegVolNotVent() {
        return ListOf_BrainSegVolNotVent;
    }

    public ArrayList<Double> getListOf_BrainSegVolNotVentSurf() {
        return ListOf_BrainSegVolNotVentSurf;
    }

    public ArrayList<Double> getListOf_CortexVol() {
        return ListOf_CortexVol;
    }

    public ArrayList<Double> getListOf_eTIV() {
        return ListOf_eTIV;
    }

    public ArrayList<Double> getListOf_MeanThickness() {
        return ListOf_MeanThickness;
    }

    public ArrayList<Integer> getListOf_TotalNumVert() {
        return ListOf_TotalNumVert;
    }

    public ArrayList<Double> getListOf_SupraTentorialVol() {
        return ListOf_SupraTentorialVol;
    }

    public ArrayList<Double> getListOf_SupraTentorialVolNotVent() {
        return ListOf_SupraTentorialVolNotVent;
    }

    public ArrayList<Double> getListOf_WhiteSurfArea() {
        return ListOf_WhiteSurfArea;
    }
}
