package Fs6AnlRsltCnvrtr1st.DataClass;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by issey on 2016/11/15.
 */
public class AsegStatsBunch extends Bunch {

    public static void main(String[] args) {
        //String folder_path_str = "file:/C:/Users/issey/Documents/Dropbox/docroot/VolConSample";
        String folder_path_str = "file:/C:/Users/issey/Documents/Dropbox/docroot/20161124_FS6_ANL_RSLT";

        //コンストラクタ
        AsegStatsBunch bas = new AsegStatsBunch(folder_path_str);
        System.out.println(bas.getListOf_SubjectName());
        System.out.println(bas.getListOf_3rdVentricle_Vol());
        System.out.println(bas.getListOf_WmHypointensities_Vol());
    }


    //読み取り対象ファイル絞込用フィルタに使う文字列
    static String FilteringString = "aseg.stats";

    ArrayList<AsegStats> dataListOf_AsegStats = new ArrayList();

    ArrayList<Double> ListOf_LeftAmygdala_Vol = new ArrayList();
    ArrayList<Double> ListOf_RightVentralDC_Vol = new ArrayList();
    ArrayList<Double> ListOf_LeftVentralDC_Vol = new ArrayList();
    ArrayList<Double> ListOf_RightInfLatVentVol = new ArrayList();
    ArrayList<Double> ListOf_LeftWmHypointensities_Vol = new ArrayList();
    ArrayList<Double> ListOf_LeftInfLatVent_Vol = new ArrayList();
    ArrayList<Double> ListOf_LeftCaudate_Vol = new ArrayList();
    ArrayList<Double> ListOf_Rightvessel_Vol = new ArrayList();
    ArrayList<Double> ListOf_LeftLateralVentricle_Vol = new ArrayList();
    ArrayList<Double> ListOf_LeftThalamusProper_Vol = new ArrayList();
    ArrayList<Double> ListOf_RightCerebellumWhiteMatter_Vol = new ArrayList();
    ArrayList<Double> ListOf_NonWmHypointensities_Vol = new ArrayList();
    ArrayList<Double> ListOf_LeftNonWmHypointensities_Vol = new ArrayList();
    ArrayList<Double> ListOf_RightnonWMhypointensities_Vol = new ArrayList();
    ArrayList<Double> ListOf_OpticChiasm_Vol = new ArrayList();
    ArrayList<Double> ListOf_CSF_Vol = new ArrayList();
    ArrayList<Double> ListOf_RightHippocampus_Vol = new ArrayList();
    ArrayList<Double> ListOf_Leftvessel_Vol = new ArrayList();
    ArrayList<Double> ListOf_LeftPallidum_Vol = new ArrayList();
    ArrayList<Double> ListOf_RightPutamen_Vol = new ArrayList();
    ArrayList<Double> ListOf_CC_Anterior_Vol = new ArrayList();
    ArrayList<Double> ListOf_RightCerebellumCortex_Vol = new ArrayList();
    ArrayList<Double> ListOf_CC_Posterior_Vol = new ArrayList();
    ArrayList<Double> ListOf_LeftPutamen_Vol = new ArrayList();
    ArrayList<Double> ListOf_Rightchoroidplexus_Vol = new ArrayList();
    ArrayList<Double> ListOf_CC_Mid_Posterior_Vol = new ArrayList();
    ArrayList<Double> ListOf_RightAccumbensarea_Vol = new ArrayList();
    ArrayList<Double> ListOf_CC_Central_Vol = new ArrayList();
    ArrayList<Double> ListOf_LeftCerebellumCortex_Vol = new ArrayList();
    ArrayList<Double> ListOf_RightWmHypointensities_Vol = new ArrayList();
    ArrayList<Double> ListOf_RightThalamusProper_Vol = new ArrayList();
    ArrayList<Double> ListOf_LeftHippocampus_Vol = new ArrayList();
    ArrayList<Double> ListOf_LeftAccumbensarea_Vol = new ArrayList();
    ArrayList<Double> ListOf_Leftchoroidplexus_Vol = new ArrayList();
    ArrayList<Double> ListOf_5thVentricle_Vol = new ArrayList();
    ArrayList<Double> ListOf_4thVentricle_Vol = new ArrayList();
    ArrayList<Double> ListOf_WmHypointensities_Vol = new ArrayList();
    ArrayList<Double> ListOf_CC_Mid_Anterior_Vol = new ArrayList();
    ArrayList<Double> ListOf_3rdVentricle_Vol = new ArrayList();
    ArrayList<Double> ListOf_RightPallidum_Vol = new ArrayList();
    ArrayList<Double> ListOf_RightCaudate_Vol = new ArrayList();
    ArrayList<Double> ListOf_RightAmygdala_Vol = new ArrayList();
    ArrayList<Double> ListOf_RightLateralVentricle_Vol = new ArrayList();
    ArrayList<Double> ListOf_LeftCerebellumWhiteMatter_Vol = new ArrayList();
    ArrayList<Double> ListOf_BrainStem_Vol = new ArrayList();

    ArrayList<Double> ListOf_BrainSegVol = new ArrayList();
    ArrayList<Double> ListOf_BrainSegVolNotVent = new ArrayList();
    ArrayList<Double> ListOf_BrainSegVolNotVentSurf = new ArrayList();
    ArrayList<Double> ListOf_VentricleChoroidVol = new ArrayList();
    ArrayList<Double> ListOf_lhCortexVol = new ArrayList();
    ArrayList<Double> ListOf_rhCortexVol = new ArrayList();
    ArrayList<Double> ListOf_CortexVol = new ArrayList();
    ArrayList<Double> ListOf_lhCerebralWhiteMatterVol = new ArrayList();
    ArrayList<Double> ListOf_rhCerebralWhiteMatterVol = new ArrayList();
    ArrayList<Double> ListOf_CerebralWhiteMatterVol = new ArrayList();
    ArrayList<Double> ListOf_SubCortGrayVol = new ArrayList();
    ArrayList<Double> ListOf_TotalGrayVol = new ArrayList();
    ArrayList<Double> ListOf_SupraTentorialVol = new ArrayList();
    ArrayList<Double> ListOf_SupraTentorialVolNotVent = new ArrayList();
    ArrayList<Double> ListOf_SupraTentorialVolNotVentVox = new ArrayList();
    ArrayList<Double> ListOf_MaskVol = new ArrayList();
    ArrayList<Double> ListOf_BrainSegVol_to_eTIV = new ArrayList();
    ArrayList<Double> ListOf_MaskVol_to_eTIV = new ArrayList();
    ArrayList<Integer> ListOf_lhSurfaceHoles = new ArrayList();
    ArrayList<Integer> ListOf_rhSurfaceHoles = new ArrayList();
    ArrayList<Integer> ListOf_SurfaceHoles = new ArrayList();
    ArrayList<Double> ListOf_eTIV = new ArrayList();

    /* ----------------------------------------------------- */

    public AsegStatsBunch(String aseg_stats_folder_path) {
        //スーパークラスのコンストラクト
        super(aseg_stats_folder_path, FilteringString);//これによりReadTargetFilesにデータが格納される

        System.out.println("読み取り対象ファイル:");
        for (File file : ReadTargetFiles) {
            System.out.println("    " + file.getAbsolutePath());
            dataListOf_AsegStats.add(new AsegStats(file.getAbsolutePath()));
        }
        System.out.println("    「*" + FilteringString + "」ファイル数: " + ReadTargetFiles.length);
        System.out.println();

        /**
         * 症例名、各脳領域の体積値のArrayListへの格納
         */
        //全てのaseg_statsの束の要素　および　全てのbrain_area情報の束の要素　について、値を取り出す。
        AsegStats an_aseg_stats;
        HashMap<String, AsegStats.SegmentInfo> segment_info_map;
        Iterator it_for_segment_map_key;
        String area_name;
        AsegStats.SegmentInfo a_segmentInfo;

        for (AsegStats data : dataListOf_AsegStats) {
            System.out.println("Now reading " + data.getFileName());

            //各Subjectがどのような順番で呼び出され処理されているかを記録するためSunjectListを作る
            ListOf_SubjectName.add(data.getSubjectName());

            //各脳領域情報の束を取り出す
            segment_info_map = data.getSengemtInfoMap();

            it_for_segment_map_key = segment_info_map.keySet().iterator();
            while (it_for_segment_map_key.hasNext()) {
                area_name = (String) it_for_segment_map_key.next();
                a_segmentInfo = segment_info_map.get(area_name);
                if (area_name.equals("Left-Lateral-Ventricle")) {
                    ListOf_LeftLateralVentricle_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Left-Inf-Lat-Vent")) {
                    ListOf_LeftInfLatVent_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Left-Cerebellum-White-Matter")) {
                    ListOf_LeftCerebellumWhiteMatter_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Left-Cerebellum-Cortex")) {
                    ListOf_LeftCerebellumCortex_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Left-Thalamus-Proper")) {
                    ListOf_LeftThalamusProper_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Left-Caudate")) {
                    ListOf_LeftCaudate_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Left-Putamen")) {
                    ListOf_LeftPutamen_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Left-Pallidum")) {
                    ListOf_LeftPallidum_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("3rd-Ventricle")) {
                    ListOf_3rdVentricle_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("4th-Ventricle")) {
                    ListOf_4thVentricle_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Brain-Stem")) {
                    ListOf_BrainStem_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Left-Hippocampus")) {
                    ListOf_LeftHippocampus_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Left-Amygdala")) {
                    ListOf_LeftAmygdala_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("CSF")) {
                    ListOf_CSF_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Left-Accumbens-area")) {
                    ListOf_LeftAccumbensarea_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Left-VentralDC")) {
                    ListOf_LeftVentralDC_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Left-vessel")) {
                    ListOf_Leftvessel_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Left-choroid-plexus")) {
                    ListOf_Leftchoroidplexus_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Right-Lateral-Ventricle")) {
                    ListOf_RightLateralVentricle_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Right-Inf-Lat-Vent")) {
                    ListOf_RightInfLatVentVol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Right-Cerebellum-White-Matter")) {
                    ListOf_RightCerebellumWhiteMatter_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Right-Cerebellum-Cortex")) {
                    ListOf_RightCerebellumCortex_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Right-Thalamus-Proper")) {
                    ListOf_RightThalamusProper_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Right-Caudate")) {
                    ListOf_RightCaudate_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Right-Putamen")) {
                    ListOf_RightPutamen_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Right-Pallidum")) {
                    ListOf_RightPallidum_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Right-Hippocampus")) {
                    ListOf_RightHippocampus_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Right-Amygdala")) {
                    ListOf_RightAmygdala_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Right-Accumbens-area")) {
                    ListOf_RightAccumbensarea_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Right-VentralDC")) {
                    ListOf_RightVentralDC_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Right-vessel")) {
                    ListOf_Rightvessel_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Right-choroid-plexus")) {
                    ListOf_Rightchoroidplexus_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("5th-Ventricle")) {
                    ListOf_5thVentricle_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("WM-hypointensities")) {
                    ListOf_WmHypointensities_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Left-WM-hypointensities")) {
                    ListOf_LeftWmHypointensities_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Right-WM-hypointensities")) {
                    ListOf_RightWmHypointensities_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("non-WM-hypointensities")) {
                    ListOf_NonWmHypointensities_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Left-non-WM-hypointensities")) {
                    ListOf_LeftNonWmHypointensities_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Right-non-WM-hypointensities")) {
                    ListOf_RightnonWMhypointensities_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("Optic-Chiasm")) {
                    ListOf_OpticChiasm_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("CC_Posterior")) {
                    ListOf_CC_Posterior_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("CC_Mid_Posterior")) {
                    ListOf_CC_Mid_Posterior_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("CC_Central")) {
                    ListOf_CC_Central_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("CC_Mid_Anterior")) {
                    ListOf_CC_Mid_Anterior_Vol.add(a_segmentInfo.getVolume_mm3());
                } else if (area_name.equals("CC_Anterior")) {
                    ListOf_CC_Anterior_Vol.add(a_segmentInfo.getVolume_mm3());
                } else {
                    System.err.println("    " + area_name + "は未定義です。");
                }
            }//end of while

            ListOf_BrainSegVol.add(data.getBrainSegVol());
            ListOf_BrainSegVolNotVent.add(data.getBrainSegVolNotVent());
            ListOf_BrainSegVolNotVentSurf.add(data.getBrainSegVolNotVentSurf());
            ListOf_VentricleChoroidVol.add(data.getVentricleChoroidVol());
            ListOf_lhCortexVol.add(data.getLhCortexVol());
            ListOf_rhCortexVol.add(data.getRhCortexVol());
            ListOf_CortexVol.add(data.getCortexVol());
            ListOf_lhCerebralWhiteMatterVol.add(data.getLhCerebralWhiteMatterVol());
            ListOf_rhCerebralWhiteMatterVol.add(data.getRhCerebralWhiteMatterVol());
            ListOf_CerebralWhiteMatterVol.add(data.getCerebralWhiteMatterVol());
            ListOf_SubCortGrayVol.add(data.getSubCortGrayVol());
            ListOf_TotalGrayVol.add(data.getTotalGrayVol());
            ListOf_SupraTentorialVol.add(data.getSupraTentorialVol());
            ListOf_SupraTentorialVolNotVent.add(data.getSupraTentorialVolNotVent());
            ListOf_SupraTentorialVolNotVentVox.add(data.getSupraTentorialVolNotVentVox());
            ListOf_MaskVol.add(data.getMaskVol());
            ListOf_BrainSegVol_to_eTIV.add(data.getBrainSegVol_to_eTIV());
            ListOf_MaskVol_to_eTIV.add(data.getMaskVol_to_eTIV());
            ListOf_lhSurfaceHoles.add(data.getLhSurfaceHoles());
            ListOf_rhSurfaceHoles.add(data.getRhSurfaceHoles());
            ListOf_SurfaceHoles.add(data.getSurfaceHoles());
            ListOf_eTIV.add(data.geteTIV());


        }//end of for
        System.out.println();

    }

    /* ----------------------------------------------------- */

    public ArrayList<AsegStats> getDataListOf_AsegStats() {
        return dataListOf_AsegStats;
    }

   /* ---------------------------------------------- */

    public ArrayList<Double> getListOf_3rdVentricle_Vol() {
        return ListOf_3rdVentricle_Vol;
    }

    public ArrayList<Double> getListOf_4thVentricle_Vol() {
        return ListOf_4thVentricle_Vol;
    }

    public ArrayList<Double> getListOf_5thVentricle_Vol() {
        return ListOf_5thVentricle_Vol;
    }

    public ArrayList<Double> getListOf_BrainStem_Vol() {
        return ListOf_BrainStem_Vol;
    }

    public ArrayList<Double> getListOf_CC_Anterior_Vol() {
        return ListOf_CC_Anterior_Vol;
    }

    public ArrayList<Double> getListOf_CC_Central_Vol() {
        return ListOf_CC_Central_Vol;
    }

    public ArrayList<Double> getListOf_CC_Mid_Anterior_Vol() {
        return ListOf_CC_Mid_Anterior_Vol;
    }

    public ArrayList<Double> getListOf_CC_Mid_Posterior_Vol() {
        return ListOf_CC_Mid_Posterior_Vol;
    }

    public ArrayList<Double> getListOf_CC_Posterior_Vol() {
        return ListOf_CC_Posterior_Vol;
    }

    public ArrayList<Double> getListOf_CSF_Vol() {
        return ListOf_CSF_Vol;
    }

    public ArrayList<Double> getListOf_LeftAccumbensarea_Vol() {
        return ListOf_LeftAccumbensarea_Vol;
    }

    public ArrayList<Double> getListOf_LeftAmygdala_Vol() {
        return ListOf_LeftAmygdala_Vol;
    }

    public ArrayList<Double> getListOf_LeftCaudate_Vol() {
        return ListOf_LeftCaudate_Vol;
    }

    public ArrayList<Double> getListOf_LeftCerebellumCortex_Vol() {
        return ListOf_LeftCerebellumCortex_Vol;
    }

    public ArrayList<Double> getListOf_LeftCerebellumWhiteMatter_Vol() {
        return ListOf_LeftCerebellumWhiteMatter_Vol;
    }

    public ArrayList<Double> getListOf_Leftchoroidplexus_Vol() {
        return ListOf_Leftchoroidplexus_Vol;
    }

    public ArrayList<Double> getListOf_LeftHippocampus_Vol() {
        return ListOf_LeftHippocampus_Vol;
    }

    public ArrayList<Double> getListOf_LeftInfLatVent_Vol() {
        return ListOf_LeftInfLatVent_Vol;
    }

    public ArrayList<Double> getListOf_LeftLateralVentricle_Vol() {
        return ListOf_LeftLateralVentricle_Vol;
    }

    public ArrayList<Double> getListOf_LeftNonWmHypointensities_Vol() {
        return ListOf_LeftNonWmHypointensities_Vol;
    }

    public ArrayList<Double> getListOf_LeftPallidum_Vol() {
        return ListOf_LeftPallidum_Vol;
    }

    public ArrayList<Double> getListOf_LeftPutamen_Vol() {
        return ListOf_LeftPutamen_Vol;
    }

    public ArrayList<Double> getListOf_LeftThalamusProper_Vol() {
        return ListOf_LeftThalamusProper_Vol;
    }

    public ArrayList<Double> getListOf_LeftVentralDC_Vol() {
        return ListOf_LeftVentralDC_Vol;
    }

    public ArrayList<Double> getListOf_Leftvessel_Vol() {
        return ListOf_Leftvessel_Vol;
    }

    public ArrayList<Double> getListOf_LeftWmHypointensities_Vol() {
        return ListOf_LeftWmHypointensities_Vol;
    }

    public ArrayList<Double> getListOf_NonWmHypointensities_Vol() {
        return ListOf_NonWmHypointensities_Vol;
    }

    public ArrayList<Double> getListOf_OpticChiasm_Vol() {
        return ListOf_OpticChiasm_Vol;
    }

    public ArrayList<Double> getListOf_RightAccumbensarea_Vol() {
        return ListOf_RightAccumbensarea_Vol;
    }

    public ArrayList<Double> getListOf_RightAmygdala_Vol() {
        return ListOf_RightAmygdala_Vol;
    }

    public ArrayList<Double> getListOf_RightCaudate_Vol() {
        return ListOf_RightCaudate_Vol;
    }

    public ArrayList<Double> getListOf_RightCerebellumCortex_Vol() {
        return ListOf_RightCerebellumCortex_Vol;
    }

    public ArrayList<Double> getListOf_RightCerebellumWhiteMatter_Vol() {
        return ListOf_RightCerebellumWhiteMatter_Vol;
    }

    public ArrayList<Double> getListOf_Rightchoroidplexus_Vol() {
        return ListOf_Rightchoroidplexus_Vol;
    }

    public ArrayList<Double> getListOf_RightHippocampus_Vol() {
        return ListOf_RightHippocampus_Vol;
    }

    public ArrayList<Double> getListOf_RightInfLatVent_Vol() {
        return ListOf_RightInfLatVentVol;
    }

    public ArrayList<Double> getListOf_RightLateralVentricle_Vol() {
        return ListOf_RightLateralVentricle_Vol;
    }

    public ArrayList<Double> getListOf_RightNonWMhypointensities_Vol() {
        return ListOf_RightnonWMhypointensities_Vol;
    }

    public ArrayList<Double> getListOf_RightPallidum_Vol() {
        return ListOf_RightPallidum_Vol;
    }

    public ArrayList<Double> getListOf_RightPutamen_Vol() {
        return ListOf_RightPutamen_Vol;
    }

    public ArrayList<Double> getListOf_RightThalamusProper_Vol() {
        return ListOf_RightThalamusProper_Vol;
    }

    public ArrayList<Double> getListOf_RightVentralDC_Vol() {
        return ListOf_RightVentralDC_Vol;
    }

    public ArrayList<Double> getListOf_Rightvessel_Vol() {
        return ListOf_Rightvessel_Vol;
    }

    public ArrayList<Double> getListOf_RightWmHypointensities_Vol() {
        return ListOf_RightWmHypointensities_Vol;
    }

    public ArrayList<Double> getListOf_WmHypointensities_Vol() {
        return ListOf_WmHypointensities_Vol;
    }

    /* ---------------------------------------------- */

    public ArrayList<Double> getListOf_BrainSegVol() {
        return ListOf_BrainSegVol;
    }

    public ArrayList<Double> getListOf_BrainSegVol_to_eTIV() {
        return ListOf_BrainSegVol_to_eTIV;
    }

    public ArrayList<Double> getListOf_BrainSegVolNotVent() {
        return ListOf_BrainSegVolNotVent;
    }

    public ArrayList<Double> getListOf_BrainSegVolNotVentSurf() {
        return ListOf_BrainSegVolNotVentSurf;
    }

    public ArrayList<Double> getListOf_CerebralWhiteMatterVol() {
        return ListOf_CerebralWhiteMatterVol;
    }

    public ArrayList<Double> getListOf_CortexVol() {
        return ListOf_CortexVol;
    }

    public ArrayList<Double> getListOf_eTIV() {
        return ListOf_eTIV;
    }

    public ArrayList<Double> getListOf_lhCerebralWhiteMatterVol() {
        return ListOf_lhCerebralWhiteMatterVol;
    }

    public ArrayList<Double> getListOf_lhCortexVol() {
        return ListOf_lhCortexVol;
    }

    public ArrayList<Integer> getListOf_lhSurfaceHoles() {
        return ListOf_lhSurfaceHoles;
    }

    public ArrayList<Double> getListOf_MaskVol() {
        return ListOf_MaskVol;
    }

    public ArrayList<Double> getListOf_MaskVol_to_eTIV() {
        return ListOf_MaskVol_to_eTIV;
    }

    public ArrayList<Double> getListOf_rhCerebralWhiteMatterVol() {
        return ListOf_rhCerebralWhiteMatterVol;
    }

    public ArrayList<Double> getListOf_rhCortexVol() {
        return ListOf_rhCortexVol;
    }

    public ArrayList<Integer> getListOf_rhSurfaceHoles() {
        return ListOf_rhSurfaceHoles;
    }

    public ArrayList<Double> getListOf_SubCortGrayVol() {
        return ListOf_SubCortGrayVol;
    }

    public ArrayList<Double> getListOf_SupraTentorialVol() {
        return ListOf_SupraTentorialVol;
    }

    public ArrayList<Double> getListOf_SupraTentorialVolNotVent() {
        return ListOf_SupraTentorialVolNotVent;
    }

    public ArrayList<Double> getListOf_SupraTentorialVolNotVentVox() {
        return ListOf_SupraTentorialVolNotVentVox;
    }

    public ArrayList<Integer> getListOf_SurfaceHoles() {
        return ListOf_SurfaceHoles;
    }

    public ArrayList<Double> getListOf_TotalGrayVol() {
        return ListOf_TotalGrayVol;
    }

    public ArrayList<Double> getListOf_VentricleChoroidVol() {
        return ListOf_VentricleChoroidVol;
    }
}
