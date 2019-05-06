package Fs6AnlRsltCnvrtr.ForSimpleSummary;

import Fs6AnlRsltCnvrtr.DataClass.*;
import basic_tools.CollectionOdsWriterAndReader_ver3;

import javax.swing.*;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by issey on 2016/11/27.
 */
public class ForSimpleSummary_v1 {
    static String OutputFileName = "20170221_FS6_ANL_RSLT_Watanabe";

    public static void main(String[] args) {
        AparcLt();
        AparcRt();
        Aseg();
        HippoSfLt();
        HippoSfRt();

    }

    public static void HippoSfRt() {
        String read_target_folder_path_str = "file:/C:/Users/issey/Downloads/RESULTS_20170221";
        URI read_target_folder_path = null;
        try {
            read_target_folder_path = new URI(read_target_folder_path_str);
        } catch (URISyntaxException urise) {
            JOptionPane.showMessageDialog(null, urise.getMessage());
        }



        //Dataのリストをまず生成
        HippoSfVolsBunch_Rt ASB = new HippoSfVolsBunch_Rt(read_target_folder_path.getPath());

        ArrayList<String> SubjectNameList = ASB.getListOf_SubjectName();

        ArrayList<Double> Hippocampal_tail_vol_list = ASB.getListOf_HippocampalTail_Vol();
        ArrayList<Double> subiculum_vol_list = ASB.getListOf_Subiculum_Vol();
        ArrayList<Double> CA1_vol_list = ASB.getListOf_CA1_Vol();
        ArrayList<Double> hippocampal_fissure_vol_list = ASB.getListOf_HippocampalFissure_Vol();
        ArrayList<Double> presubiculum_vol_list = ASB.getListOf_Presubiculum_Vol();
        ArrayList<Double> parasubiculum_vol_list = ASB.getListOf_Parasubiculum_Vol();
        ArrayList<Double> molecular_layer_HP_vol_list = ASB.getListOf_MolecularLayerHP_Vol();
        ArrayList<Double> GC_ML_DG_vol_list = ASB.getListOf_GC_ML_DG_Vol();
        ArrayList<Double> CA3_vol_list = ASB.getListOf_CA3_Vol();
        ArrayList<Double> CA4_vol_list = ASB.getListOf_CA4_Vol();
        ArrayList<Double> fimbria_vol_list = ASB.getListOf_Fimbria_Vol();
        ArrayList<Double> HATA_vol_list = ASB.getListOf_HATA_Vol();
        ArrayList<Double> Whole_hippocampus_vol_list = ASB.getListOf_WholeHippocampus_Vol();


        //ODS化するためのツリーマップを用意
        LinkedHashMap<String, String> tm = new LinkedHashMap();
        String the1st_row_str =
                "Hippocampal_tail\t" +
                        "subiculum\t" +
                        "CA1\t" +
                        "hippocampal-fissure\t" +
                        "presubiculum\t" +
                        "parasubiculum\t" +
                        "molecular_layer_HP\t" +
                        "GC-ML-DG\t" +
                        "CA3\t" +
                        "CA4\t" +
                        "fimbria\t" +
                        "HATA\t" +
                        "Whole_hippocampus";

        tm.put("Sbj", the1st_row_str);
        String row_str;
        for (int i = 0; i < ASB.getListOf_SubjectName().size(); i++) {
            row_str = "";

            row_str += Hippocampal_tail_vol_list.get(i) + "\t";
            row_str += subiculum_vol_list.get(i) + "\t";
            row_str += CA1_vol_list.get(i) + "\t";
            row_str += hippocampal_fissure_vol_list.get(i) + "\t";
            row_str += presubiculum_vol_list.get(i) + "\t";
            row_str += parasubiculum_vol_list.get(i) + "\t";
            row_str += molecular_layer_HP_vol_list.get(i) + "\t";
            row_str += GC_ML_DG_vol_list.get(i) + "\t";
            row_str += CA3_vol_list.get(i) + "\t";
            row_str += CA4_vol_list.get(i) + "\t";
            row_str += fimbria_vol_list.get(i) + "\t";
            row_str += HATA_vol_list.get(i) + "\t";
            row_str += Whole_hippocampus_vol_list.get(i) + "\t";

            tm.put(SubjectNameList.get(i), row_str);
        }

        //ODS化
        CollectionOdsWriterAndReader_ver3.addToOdsfile(tm, new File(read_target_folder_path.getPath() + "/" + OutputFileName + ".ods"), "HippoSfRt");

    }

    public static void HippoSfLt() {
        String read_target_folder_path_str = "file:/C:/Users/issey/Downloads/RESULTS_20170221";
        URI read_target_folder_path = null;
        try {
            read_target_folder_path = new URI(read_target_folder_path_str);
        } catch (URISyntaxException urise) {
            JOptionPane.showMessageDialog(null, urise.getMessage());
        }


        //Dataのリストをまず生成
        HippoSfVolsBunch_Lt ASB = new HippoSfVolsBunch_Lt(read_target_folder_path.getPath());

        ArrayList<String> SubjectNameList = ASB.getListOf_SubjectName();

        ArrayList<Double> Hippocampal_tail_vol_list = ASB.getListOf_HippocampalTail_Vol();
        ArrayList<Double> subiculum_vol_list = ASB.getListOf_Subiculum_Vol();
        ArrayList<Double> CA1_vol_list = ASB.getListOf_CA1_Vol();
        ArrayList<Double> hippocampal_fissure_vol_list = ASB.getListOf_HippocampalFissure_Vol();
        ArrayList<Double> presubiculum_vol_list = ASB.getListOf_Presubiculum_Vol();
        ArrayList<Double> parasubiculum_vol_list = ASB.getListOf_Parasubiculum_Vol();
        ArrayList<Double> molecular_layer_HP_vol_list = ASB.getListOf_MolecularLayerHP_Vol();
        ArrayList<Double> GC_ML_DG_vol_list = ASB.getListOf_GC_ML_DG_Vol();
        ArrayList<Double> CA3_vol_list = ASB.getListOf_CA3_Vol();
        ArrayList<Double> CA4_vol_list = ASB.getListOf_CA4_Vol();
        ArrayList<Double> fimbria_vol_list = ASB.getListOf_Fimbria_Vol();
        ArrayList<Double> HATA_vol_list = ASB.getListOf_HATA_Vol();
        ArrayList<Double> Whole_hippocampus_vol_list = ASB.getListOf_WholeHippocampus_Vol();


        //ODS化するためのツリーマップを用意
        LinkedHashMap<String, String> tm = new LinkedHashMap();
        String the1st_row_str =
                "Hippocampal_tail\t" +
                        "subiculum\t" +
                        "CA1\t" +
                        "hippocampal-fissure\t" +
                        "presubiculum\t" +
                        "parasubiculum\t" +
                        "molecular_layer_HP\t" +
                        "GC-ML-DG\t" +
                        "CA3\t" +
                        "CA4\t" +
                        "fimbria\t" +
                        "HATA\t" +
                        "Whole_hippocampus";

        tm.put("Sbj", the1st_row_str);
        String row_str;
        for (int i = 0; i < ASB.getListOf_SubjectName().size(); i++) {
            row_str = "";

            row_str += Hippocampal_tail_vol_list.get(i) + "\t";
            row_str += subiculum_vol_list.get(i) + "\t";
            row_str += CA1_vol_list.get(i) + "\t";
            row_str += hippocampal_fissure_vol_list.get(i) + "\t";
            row_str += presubiculum_vol_list.get(i) + "\t";
            row_str += parasubiculum_vol_list.get(i) + "\t";
            row_str += molecular_layer_HP_vol_list.get(i) + "\t";
            row_str += GC_ML_DG_vol_list.get(i) + "\t";
            row_str += CA3_vol_list.get(i) + "\t";
            row_str += CA4_vol_list.get(i) + "\t";
            row_str += fimbria_vol_list.get(i) + "\t";
            row_str += HATA_vol_list.get(i) + "\t";
            row_str += Whole_hippocampus_vol_list.get(i) + "\t";

            tm.put(SubjectNameList.get(i), row_str);
        }

        //ODS化
        CollectionOdsWriterAndReader_ver3.addToOdsfile(tm, new File(read_target_folder_path.getPath() + "/" + OutputFileName + ".ods"), "HippoSfLt");

    }

    public static void Aseg() {
        String read_target_folder_path_str = "file:/C:/Users/issey/Downloads/RESULTS_20170221";
        URI read_target_folder_path = null;
        try {
            read_target_folder_path = new URI(read_target_folder_path_str);
        } catch (URISyntaxException urise) {
            JOptionPane.showMessageDialog(null, urise.getMessage());
        }


        //Dataのリストをまず生成
        AsegStatsBunch ASB = new AsegStatsBunch(read_target_folder_path.getPath());

        ArrayList<String> SubjectNameList = ASB.getListOf_SubjectName();

        ArrayList<Double> LeftLateralVentricle_vol_list = ASB.getListOf_LeftLateralVentricle_Vol();
        ArrayList<Double> LeftInfLatVent_vol_list = ASB.getListOf_LeftInfLatVent_Vol();
        ArrayList<Double> LeftCerebellumWhiteMatter_vol_list = ASB.getListOf_LeftCerebellumWhiteMatter_Vol();
        ArrayList<Double> LeftCerebellumCortex_vol_list = ASB.getListOf_LeftCerebellumCortex_Vol();
        ArrayList<Double> LeftThalamusProper_vol_list = ASB.getListOf_LeftThalamusProper_Vol();
        ArrayList<Double> LeftCaudate_vol_list = ASB.getListOf_LeftCaudate_Vol();
        ArrayList<Double> LeftPutamen_vol_list = ASB.getListOf_LeftPutamen_Vol();
        ArrayList<Double> LeftPallidum_vol_list = ASB.getListOf_LeftPallidum_Vol();
        ArrayList<Double> ThirdVentricle_vol_list = ASB.getListOf_3rdVentricle_Vol();
        ArrayList<Double> FourthVentricle_vol_list = ASB.getListOf_4thVentricle_Vol();
        ArrayList<Double> BrainStem_vol_list = ASB.getListOf_BrainStem_Vol();
        ArrayList<Double> LeftHippocampus_vol_list = ASB.getListOf_LeftHippocampus_Vol();
        ArrayList<Double> LeftAmygdala_vol_list = ASB.getListOf_LeftAmygdala_Vol();
        ArrayList<Double> CSF_vol_list = ASB.getListOf_CSF_Vol();
        ArrayList<Double> LeftAccumbensarea_vol_list = ASB.getListOf_LeftAccumbensarea_Vol();
        ArrayList<Double> LeftVentralDC_vol_list = ASB.getListOf_LeftVentralDC_Vol();
        ArrayList<Double> Leftvessel_vol_list = ASB.getListOf_Leftvessel_Vol();
        ArrayList<Double> Leftchoroidplexus_vol_list = ASB.getListOf_Leftchoroidplexus_Vol();
        ArrayList<Double> RightLateralVentricle_vol_list = ASB.getListOf_RightLateralVentricle_Vol();
        ArrayList<Double> RightInfLatVent_vol_list = ASB.getListOf_RightInfLatVent_Vol();
        ArrayList<Double> RightCerebellumWhiteMatter_vol_list = ASB.getListOf_RightCerebellumWhiteMatter_Vol();
        ArrayList<Double> RightCerebellumCortex_vol_list = ASB.getListOf_RightCerebellumCortex_Vol();
        ArrayList<Double> RightThalamusProper_vol_list = ASB.getListOf_RightThalamusProper_Vol();
        ArrayList<Double> RightCaudate_vol_list = ASB.getListOf_RightCaudate_Vol();
        ArrayList<Double> RightPutamen_vol_list = ASB.getListOf_RightPutamen_Vol();
        ArrayList<Double> RightPallidum_vol_list = ASB.getListOf_RightPallidum_Vol();
        ArrayList<Double> RightHippocampus_vol_list = ASB.getListOf_RightHippocampus_Vol();
        ArrayList<Double> RightAmygdala_vol_list = ASB.getListOf_RightAmygdala_Vol();
        ArrayList<Double> RightAccumbensarea_vol_list = ASB.getListOf_RightAccumbensarea_Vol();
        ArrayList<Double> RightVentralDC_vol_list = ASB.getListOf_RightVentralDC_Vol();
        ArrayList<Double> Rightvessel_vol_list = ASB.getListOf_Rightvessel_Vol();
        ArrayList<Double> Rightchoroidplexus_vol_list = ASB.getListOf_Rightchoroidplexus_Vol();
        ArrayList<Double> FifthVentricle_vol_list = ASB.getListOf_5thVentricle_Vol();
        ArrayList<Double> WMhypointensities_vol_list = ASB.getListOf_WmHypointensities_Vol();
        ArrayList<Double> LeftWMhypointensities_vol_list = ASB.getListOf_LeftWmHypointensities_Vol();
        ArrayList<Double> RightWMhypointensities_vol_list = ASB.getListOf_RightWmHypointensities_Vol();
        ArrayList<Double> nonWMhypointensities_vol_list = ASB.getListOf_NonWmHypointensities_Vol();
        ArrayList<Double> LeftnonWMhypointensities_vol_list = ASB.getListOf_LeftNonWmHypointensities_Vol();
        ArrayList<Double> RightnonWMhypointensities_vol_list = ASB.getListOf_RightNonWMhypointensities_Vol();
        ArrayList<Double> OpticChiasm_vol_list = ASB.getListOf_OpticChiasm_Vol();
        ArrayList<Double> CC_Posterior_vol_list = ASB.getListOf_CC_Mid_Posterior_Vol();
        ArrayList<Double> CC_Mid_Posterior_vol_list = ASB.getListOf_CC_Mid_Posterior_Vol();
        ArrayList<Double> CC_Central_vol_list = ASB.getListOf_CC_Central_Vol();
        ArrayList<Double> CC_Mid_Anterior_vol_list = ASB.getListOf_CC_Mid_Anterior_Vol();
        ArrayList<Double> CC_Anterior_vol_list = ASB.getListOf_CC_Anterior_Vol();

        ArrayList<Double> BrainSegVol_list = ASB.getListOf_BrainSegVol(); //Brain Segmentation Volume
        ArrayList<Double> BrainSegVolNotVent_list = ASB.getListOf_BrainSegVolNotVent(); //Brain Segmentation Volume Without Ventricles
        ArrayList<Double> BrainSegVolNotVentSurf_list = ASB.getListOf_BrainSegVolNotVentSurf(); //Brain Segmentation Volume Without Ventricles from Surf
        ArrayList<Double> VentricleChoroidVol_list = ASB.getListOf_VentricleChoroidVol(); //Volume of ventricles and choroid plexus
        ArrayList<Double> lhCortexVol_list = ASB.getListOf_lhCortexVol(); //Left hemisphere cortical gray matter volume
        ArrayList<Double> rhCortexVol_list = ASB.getListOf_rhCortexVol(); //Right hemisphere cortical gray matter volume
        ArrayList<Double> CortexVol_list = ASB.getListOf_CortexVol(); //Total cortical gray matter volume
        ArrayList<Double> lhCerebralWhiteMatterVol_list = ASB.getListOf_lhCerebralWhiteMatterVol(); //Left hemisphere cerebral white matter volume
        ArrayList<Double> rhCerebralWhiteMatterVol_list = ASB.getListOf_rhCerebralWhiteMatterVol(); //Right hemisphere cerebral white matter volume
        ArrayList<Double> CerebralWhiteMatterVol_list = ASB.getListOf_CerebralWhiteMatterVol(); //Total cerebral white matter volume
        ArrayList<Double> SubCortGrayVol_list = ASB.getListOf_SubCortGrayVol(); //Subcortical gray matter volume
        ArrayList<Double> TotalGrayVol_list = ASB.getListOf_TotalGrayVol(); //Total gray matter volume
        ArrayList<Double> SupraTentorialVol_list = ASB.getListOf_SupraTentorialVol(); //Supratentorial volume
        ArrayList<Double> SupraTentorialVolNotVent_list = ASB.getListOf_SupraTentorialVolNotVent(); //Supratentorial volume
        ArrayList<Double> SupraTentorialVolNotVentVox_list = ASB.getListOf_SupraTentorialVolNotVentVox(); //Supratentorial volume voxel count
        ArrayList<Double> MaskVol_list = ASB.getListOf_MaskVol(); //Mask Volume
        ArrayList<Double> BrainSegVol_to_eTIV_list = ASB.getListOf_BrainSegVol_to_eTIV(); //Ratio of BrainSegVol to eTIV
        ArrayList<Double> MaskVol_to_eTIV_list = ASB.getListOf_MaskVol_to_eTIV(); //Ratio of MaskVol to eTIV
        ArrayList<Integer> lhSurfaceHoles_list = ASB.getListOf_lhSurfaceHoles(); //Number of defect holes in lh surfaces prior to fixing
        ArrayList<Integer> rhSurfaceHoles_list = ASB.getListOf_rhSurfaceHoles(); //Number of defect holes in rh surfaces prior to fixing
        ArrayList<Integer> SurfaceHoles_list = ASB.getListOf_SurfaceHoles(); //Total number of defect holes in surfaces prior to fixing
        ArrayList<Double> eTIV_list = ASB.getListOf_eTIV(); //Estimated Total Intracranial Volume

        //ODS化するためのツリーマップを用意
        LinkedHashMap<String, String> tm = new LinkedHashMap();
        String the1st_row_str =
                "Left-Lateral-Ventricle\t" +
                        "Left-Inf-Lat-Vent\t" +
                        "Left-Cerebellum-White-Matter\t" +
                        "Left-Cerebellum-Cortex\t" +
                        "Left-Thalamus-Proper\t" +
                        "Left-Caudate\t" +
                        "Left-Putamen\t" +
                        "Left-Pallidum\t" +
                        "3rd-Ventricle\t" +
                        "4th-Ventricle\t" +
                        "Brain-Stem\t" +
                        "Left-Hippocampus\t" +
                        "Left-Amygdala\t" +
                        "CSF\t" +
                        "Left-Accumbens-area\t" +
                        "Left-VentralDC\t" +
                        "Left-vessel\t" +
                        "Left-choroid-plexus\t" +
                        "Right-Lateral-Ventricle\t" +
                        "Right-Inf-Lat-Vent\t" +
                        "Right-Cerebellum-White-Matter\t" +
                        "Right-Cerebellum-Cortex\t" +
                        "Right-Thalamus-Proper\t" +
                        "Right-Caudate\t" +
                        "Right-Putamen\t" +
                        "Right-Pallidum\t" +
                        "Right-Hippocampus\t" +
                        "Right-Amygdala\t" +
                        "Right-Accumbens-area\t" +
                        "Right-VentralDC\t" +
                        "Right-vessel\t" +
                        "Right-choroid-plexus\t" +
                        "5th-Ventricle\t" +
                        "WM-hypointensities\t" +
                        "Left-WM-hypointensities\t" +
                        "Right-WM-hypointensities\t" +
                        "non-WM-hypointensities\t" +
                        "Left-non-WM-hypointensities\t" +
                        "Right-non-WM-hypointensities\t" +
                        "Optic-Chiasm\t" +
                        "CC_Posterior\t" +
                        "CC_Mid_Posterior\t" +
                        "CC_Central\t" +
                        "CC_Mid_Anterior\t" +
                        "CC_Anterior\t" +
                        "BrainSegVol\t" +
                        "BrainSegVolNotVent\t" +
                        "BrainSegVolNotVentSurf\t" +
                        "VentricleChoroidVol\t" +
                        "lhCortexVol\t" +
                        "rhCortexVol\t" +
                        "CortexVol\t" +
                        "lhCerebralWhiteMatterVol\t" +
                        "rhCerebralWhiteMatterVol\t" +
                        "CerebralWhiteMatterVol\t" +
                        "SubCortGrayVol\t" +
                        "TotalGrayVol\t" +
                        "SupraTentorialVol\t" +
                        "SupraTentorialVolNotVent\t" +
                        "SupraTentorialVolNotVentVox\t" +
                        "MaskVol\t" +
                        "BrainSegVol-to-eTIV\t" +
                        "MaskVol-to-eTIV\t" +
                        "lhSurfaceHoles\t" +
                        "rhSurfaceHoles\t" +
                        "SurfaceHoles\t" +
                        "eTIV";


        tm.put("Sbj", the1st_row_str);
        String row_str;
        for (int i = 0; i < ASB.getListOf_SubjectName().size(); i++) {
            row_str = "";

            row_str += LeftLateralVentricle_vol_list.get(i) + "\t";
            row_str += LeftInfLatVent_vol_list.get(i) + "\t";
            row_str += LeftCerebellumWhiteMatter_vol_list.get(i) + "\t";
            row_str += LeftCerebellumCortex_vol_list.get(i) + "\t";
            row_str += LeftThalamusProper_vol_list.get(i) + "\t";
            row_str += LeftCaudate_vol_list.get(i) + "\t";
            row_str += LeftPutamen_vol_list.get(i) + "\t";
            row_str += LeftPallidum_vol_list.get(i) + "\t";
            row_str += ThirdVentricle_vol_list.get(i) + "\t";
            row_str += FourthVentricle_vol_list.get(i) + "\t";
            row_str += BrainStem_vol_list.get(i) + "\t";
            row_str += LeftHippocampus_vol_list.get(i) + "\t";
            row_str += LeftAmygdala_vol_list.get(i) + "\t";
            row_str += CSF_vol_list.get(i) + "\t";
            row_str += LeftAccumbensarea_vol_list.get(i) + "\t";
            row_str += LeftVentralDC_vol_list.get(i) + "\t";
            row_str += Leftvessel_vol_list.get(i) + "\t";
            row_str += Leftchoroidplexus_vol_list.get(i) + "\t";
            row_str += RightLateralVentricle_vol_list.get(i) + "\t";
            row_str += RightInfLatVent_vol_list.get(i) + "\t";
            row_str += RightCerebellumWhiteMatter_vol_list.get(i) + "\t";
            row_str += RightCerebellumCortex_vol_list.get(i) + "\t";
            row_str += RightThalamusProper_vol_list.get(i) + "\t";
            row_str += RightCaudate_vol_list.get(i) + "\t";
            row_str += RightPutamen_vol_list.get(i) + "\t";
            row_str += RightPallidum_vol_list.get(i) + "\t";
            row_str += RightHippocampus_vol_list.get(i) + "\t";
            row_str += RightAmygdala_vol_list.get(i) + "\t";
            row_str += RightAccumbensarea_vol_list.get(i) + "\t";
            row_str += RightVentralDC_vol_list.get(i) + "\t";
            row_str += Rightvessel_vol_list.get(i) + "\t";
            row_str += Rightchoroidplexus_vol_list.get(i) + "\t";
            row_str += FifthVentricle_vol_list.get(i) + "\t";
            row_str += WMhypointensities_vol_list.get(i) + "\t";
            row_str += LeftWMhypointensities_vol_list.get(i) + "\t";
            row_str += RightWMhypointensities_vol_list.get(i) + "\t";
            row_str += nonWMhypointensities_vol_list.get(i) + "\t";
            row_str += LeftnonWMhypointensities_vol_list.get(i) + "\t";
            row_str += RightnonWMhypointensities_vol_list.get(i) + "\t";
            row_str += OpticChiasm_vol_list.get(i) + "\t";
            row_str += CC_Posterior_vol_list.get(i) + "\t";
            row_str += CC_Mid_Posterior_vol_list.get(i) + "\t";
            row_str += CC_Central_vol_list.get(i) + "\t";
            row_str += CC_Mid_Anterior_vol_list.get(i) + "\t";
            row_str += CC_Anterior_vol_list.get(i) + "\t";
            row_str += BrainSegVol_list.get(i) + "\t";
            row_str += BrainSegVolNotVent_list.get(i) + "\t";
            row_str += BrainSegVolNotVentSurf_list.get(i) + "\t";
            row_str += VentricleChoroidVol_list.get(i) + "\t";
            row_str += lhCortexVol_list.get(i) + "\t";
            row_str += rhCortexVol_list.get(i) + "\t";
            row_str += CortexVol_list.get(i) + "\t";
            row_str += lhCerebralWhiteMatterVol_list.get(i) + "\t";
            row_str += rhCerebralWhiteMatterVol_list.get(i) + "\t";
            row_str += CerebralWhiteMatterVol_list.get(i) + "\t";
            row_str += SubCortGrayVol_list.get(i) + "\t";
            row_str += TotalGrayVol_list.get(i) + "\t";
            row_str += SupraTentorialVol_list.get(i) + "\t";
            row_str += SupraTentorialVolNotVent_list.get(i) + "\t";
            row_str += SupraTentorialVolNotVentVox_list.get(i) + "\t";
            row_str += MaskVol_list.get(i) + "\t";
            row_str += BrainSegVol_to_eTIV_list.get(i) + "\t";
            row_str += MaskVol_to_eTIV_list.get(i) + "\t";
            row_str += lhSurfaceHoles_list.get(i) + "\t";
            row_str += rhSurfaceHoles_list.get(i) + "\t";
            row_str += SurfaceHoles_list.get(i) + "\t";
            row_str += eTIV_list.get(i) + "\t";

            tm.put(SubjectNameList.get(i), row_str);
        }

        //ODS化
        CollectionOdsWriterAndReader_ver3.addToOdsfile(tm, new File(read_target_folder_path.getPath() + "/" + OutputFileName + ".ods"), "Aseg");


    }


    public static void AparcRt() {
        String read_target_folder_path_str = "file:/C:/Users/issey/Downloads/RESULTS_20170221";
        URI read_target_folder_path = null;
        try {
            read_target_folder_path = new URI(read_target_folder_path_str);
        } catch (URISyntaxException urise) {
            JOptionPane.showMessageDialog(null, urise.getMessage());
        }


        //Dataのリストをまず生成
        AparcStatsBunch_Rt ASBR = new AparcStatsBunch_Rt(read_target_folder_path.getPath());

        ArrayList<String> SubjectNameList = ASBR.getListOf_SubjectName();

        ArrayList<Double> caudalanteriorcingulate_vol_list = ASBR.getListOf_Caudalanteriorcingulate_Vol();
        ArrayList<Double> caudalmiddlefrontal_vol_list = ASBR.getListOf_Caudalmiddlefrontal_Vol();
        ArrayList<Double> cuneus_vol_list = ASBR.getListOf_Cuneus_Vol();
        ArrayList<Double> entorhinal_vol_list = ASBR.getListOf_Entorhinal_Vol();
        ArrayList<Double> fusiform_vol_list = ASBR.getListOf_Fusiform_Vol();
        ArrayList<Double> inferiorparietal_vol_list = ASBR.getListOf_Inferiorparietal_Vol();
        ArrayList<Double> inferiortemporal_vol_list = ASBR.getListOf_Inferiortemporal_Vol();
        ArrayList<Double> isthmuscingulate_vol_list = ASBR.getListOf_Isthmuscingulate_Vol();
        ArrayList<Double> lateraloccipital_vol_list = ASBR.getListOf_Lateraloccipital_Vol();
        ArrayList<Double> lateralorbitofrontal_vol_list = ASBR.getListOf_Lateralorbitofrontal_Vol();
        ArrayList<Double> lingual_vol_list = ASBR.getListOf_Lingual_Vol();
        ArrayList<Double> medialorbitofrontal_vol_list = ASBR.getListOf_Medialorbitofrontal_Vol();
        ArrayList<Double> middletemporal_vol_list = ASBR.getListOf_Middletemporal_Vol();
        ArrayList<Double> parahippocampal_vol_list = ASBR.getListOf_Parahippocampal_Vol();
        ArrayList<Double> paracentral_vol_list = ASBR.getListOf_Paracentral_Vol();
        ArrayList<Double> parsopercularis_vol_list = ASBR.getListOf_Parsopercularis_Vol();
        ArrayList<Double> parsorbitalis_vol_list = ASBR.getListOf_Parsorbitalis_Vol();
        ArrayList<Double> parstriangularis_vol_list = ASBR.getListOf_Parstriangularis_Vol();
        ArrayList<Double> pericalcarine_vol_list = ASBR.getListOf_Pericalcarine_Vol();
        ArrayList<Double> postcentral_vol_list = ASBR.getListOf_Postcentral_Vol();
        ArrayList<Double> posteriorcingulate_vol_list = ASBR.getListOf_Posteriorcingulate_Vol();
        ArrayList<Double> precentral_vol_list = ASBR.getListOF_Precentral_Vol();
        ArrayList<Double> precuneus_vol_list = ASBR.getListOf_Precuneus_Vol();
        ArrayList<Double> rostralanteriorcingulate_vol_list = ASBR.getListOf_Rostralanteriorcingulate_Vol();
        ArrayList<Double> rostralmiddlefrontal_vol_list = ASBR.getListOf_Rostralmiddlefrontal_Vol();
        ArrayList<Double> superiorfrontal_vol_list = ASBR.getListOf_Superiorfrontal_Vol();
        ArrayList<Double> superiorparietal_vol_list = ASBR.getListOf_Superiorparietal_Vol();
        ArrayList<Double> superiortemporal_vol_list = ASBR.getListOf_Superiortemporal_Vol();
        ArrayList<Double> supramarginal_vol_list = ASBR.getListOf_Supramarginal_Vol();
        ArrayList<Double> transversetemporal_vol_list = ASBR.getListOf_Transversetemporal_Vol();
        ArrayList<Double> insula_vol_list = ASBR.getListOf_Insula_Vol();

        ArrayList<Integer> NumVert_list = ASBR.getListOf_TotalNumVert();
        ArrayList<Double> WhiteSurfArea_list = ASBR.getListOf_WhiteSurfArea();
        ArrayList<Double> MeanThickness_list = ASBR.getListOf_MeanThickness();
        ArrayList<Double> BrainSegVol_list = ASBR.getListOf_BrainSegVol();
        ArrayList<Double> BrainSegVolNotVent_list = ASBR.getListOf_BrainSegVolNotVent();
        ArrayList<Double> BrainSegVolNotVentSurf_list = ASBR.getListOf_BrainSegVolNotVentSurf();
        ArrayList<Double> CortexVol_list = ASBR.getListOf_CortexVol();
        ArrayList<Double> SupraTentorialVol_list = ASBR.getListOf_SupraTentorialVol();
        ArrayList<Double> SupraTentorialVolNotVent_list = ASBR.getListOf_SupraTentorialVolNotVent();
        ArrayList<Double> eTIV_list = ASBR.getListOf_eTIV();


        //ODS化するためのツリーマップを用意
        LinkedHashMap<String, String> tm = new LinkedHashMap();
        String the1st_row_str =
                "caudalanteriorcingulate\t" +
                        "caudalmiddlefrontal\t" +
                        "cuneus\t" +
                        "entorhinal\t" +
                        "fusiform\t" +
                        "inferiorparietal\t" +
                        "inferiortemporal\t" +
                        "isthmuscingulate\t" +
                        "lateraloccipital\t" +
                        "lateralorbitofrontal\t" +
                        "lingual\t" +
                        "medialorbitofrontal\t" +
                        "middletemporal\t" +
                        "parahippocampal\t" +
                        "paracentral\t" +
                        "parsopercularis\t" +
                        "parsorbitalis\t" +
                        "parstriangularis\t" +
                        "pericalcarine\t" +
                        "postcentral\t" +
                        "posteriorcingulate\t" +
                        "precentral\t" +
                        "precuneus\t" +
                        "rostralanteriorcingulate\t" +
                        "rostralmiddlefrontal\t" +
                        "superiorfrontal\t" +
                        "superiorparietal\t" +
                        "superiortemporal\t" +
                        "supramarginal\t" +
                        "transversetemporal\t" +
                        "insula\t" +
                        "NumVert\t" +
                        "WhiteSurfArea\t" +
                        "MeanThickness\t" +
                        "BrainSegVol\t" +
                        "BrainSegVolNotVent\t" +
                        "BrainSegVolNotVentSurf\t" +
                        "CortexVol\t" +
                        "SupraTentorialVol\t" +
                        "SupraTentorialVolNotVent\t" +
                        "eTIV";


        tm.put("Sbj", the1st_row_str);
        String row_str;
        for (int i = 0; i < ASBR.getListOf_SubjectName().size(); i++) {
            row_str = "";

            row_str += caudalanteriorcingulate_vol_list.get(i) + "\t";
            row_str += caudalmiddlefrontal_vol_list.get(i) + "\t";
            row_str += cuneus_vol_list.get(i) + "\t";
            row_str += entorhinal_vol_list.get(i) + "\t";
            row_str += fusiform_vol_list.get(i) + "\t";
            row_str += inferiorparietal_vol_list.get(i) + "\t";
            row_str += inferiortemporal_vol_list.get(i) + "\t";
            row_str += isthmuscingulate_vol_list.get(i) + "\t";
            row_str += lateraloccipital_vol_list.get(i) + "\t";
            row_str += lateralorbitofrontal_vol_list.get(i) + "\t";
            row_str += lingual_vol_list.get(i) + "\t";
            row_str += medialorbitofrontal_vol_list.get(i) + "\t";
            row_str += middletemporal_vol_list.get(i) + "\t";
            row_str += parahippocampal_vol_list.get(i) + "\t";
            row_str += paracentral_vol_list.get(i) + "\t";
            row_str += parsopercularis_vol_list.get(i) + "\t";
            row_str += parsorbitalis_vol_list.get(i) + "\t";
            row_str += parstriangularis_vol_list.get(i) + "\t";
            row_str += pericalcarine_vol_list.get(i) + "\t";
            row_str += postcentral_vol_list.get(i) + "\t";
            row_str += posteriorcingulate_vol_list.get(i) + "\t";
            row_str += precentral_vol_list.get(i) + "\t";
            row_str += precuneus_vol_list.get(i) + "\t";
            row_str += rostralanteriorcingulate_vol_list.get(i) + "\t";
            row_str += rostralmiddlefrontal_vol_list.get(i) + "\t";
            row_str += superiorfrontal_vol_list.get(i) + "\t";
            row_str += superiorparietal_vol_list.get(i) + "\t";
            row_str += superiortemporal_vol_list.get(i) + "\t";
            row_str += supramarginal_vol_list.get(i) + "\t";
            row_str += transversetemporal_vol_list.get(i) + "\t";
            row_str += insula_vol_list.get(i) + "\t";
            row_str += NumVert_list.get(i) + "\t";
            row_str += WhiteSurfArea_list.get(i) + "\t";
            row_str += MeanThickness_list.get(i) + "\t";
            row_str += BrainSegVol_list.get(i) + "\t";
            row_str += BrainSegVolNotVent_list.get(i) + "\t";
            row_str += BrainSegVolNotVentSurf_list.get(i) + "\t";
            row_str += CortexVol_list.get(i) + "\t";
            row_str += SupraTentorialVol_list.get(i) + "\t";
            row_str += SupraTentorialVolNotVent_list.get(i) + "\t";
            row_str += eTIV_list.get(i) + "\t";

            tm.put(SubjectNameList.get(i), row_str);
        }

        //ODS化
        CollectionOdsWriterAndReader_ver3.addToOdsfile(tm, new File(read_target_folder_path.getPath() + "/" + OutputFileName + ".ods"), "AparcRt");


    }

    public static void AparcLt() {
        String read_target_folder_path_str = "file:/C:/Users/issey/Downloads/RESULTS_20170221";
        URI read_target_folder_path = null;
        try {
            read_target_folder_path = new URI(read_target_folder_path_str);
        } catch (URISyntaxException urise) {
            JOptionPane.showMessageDialog(null, urise.getMessage());
        }


        //Dataのリストをまず生成
        AparcStatsBunch_Lt ASBR = new AparcStatsBunch_Lt(read_target_folder_path.getPath());

        ArrayList<String> SubjectNameList = ASBR.getListOf_SubjectName();

        ArrayList<Double> caudalanteriorcingulate_vol_list = ASBR.getListOf_Caudalanteriorcingulate_Vol();
        ArrayList<Double> caudalmiddlefrontal_vol_list = ASBR.getListOf_Caudalmiddlefrontal_Vol();
        ArrayList<Double> cuneus_vol_list = ASBR.getListOf_Cuneus_Vol();
        ArrayList<Double> entorhinal_vol_list = ASBR.getListOf_Entorhinal_Vol();
        ArrayList<Double> fusiform_vol_list = ASBR.getListOf_Fusiform_Vol();
        ArrayList<Double> inferiorparietal_vol_list = ASBR.getListOf_Inferiorparietal_Vol();
        ArrayList<Double> inferiortemporal_vol_list = ASBR.getListOf_Inferiortemporal_Vol();
        ArrayList<Double> isthmuscingulate_vol_list = ASBR.getListOf_Isthmuscingulate_Vol();
        ArrayList<Double> lateraloccipital_vol_list = ASBR.getListOf_Lateraloccipital_Vol();
        ArrayList<Double> lateralorbitofrontal_vol_list = ASBR.getListOf_Lateralorbitofrontal_Vol();
        ArrayList<Double> lingual_vol_list = ASBR.getListOf_Lingual_Vol();
        ArrayList<Double> medialorbitofrontal_vol_list = ASBR.getListOf_Medialorbitofrontal_Vol();
        ArrayList<Double> middletemporal_vol_list = ASBR.getListOf_Middletemporal_Vol();
        ArrayList<Double> parahippocampal_vol_list = ASBR.getListOf_Parahippocampal_Vol();
        ArrayList<Double> paracentral_vol_list = ASBR.getListOf_Paracentral_Vol();
        ArrayList<Double> parsopercularis_vol_list = ASBR.getListOf_Parsopercularis_Vol();
        ArrayList<Double> parsorbitalis_vol_list = ASBR.getListOf_Parsorbitalis_Vol();
        ArrayList<Double> parstriangularis_vol_list = ASBR.getListOf_Parstriangularis_Vol();
        ArrayList<Double> pericalcarine_vol_list = ASBR.getListOf_Pericalcarine_Vol();
        ArrayList<Double> postcentral_vol_list = ASBR.getListOf_Postcentral_Vol();
        ArrayList<Double> posteriorcingulate_vol_list = ASBR.getListOf_Posteriorcingulate_Vol();
        ArrayList<Double> precentral_vol_list = ASBR.getListOF_Precentral_Vol();
        ArrayList<Double> precuneus_vol_list = ASBR.getListOf_Precuneus_Vol();
        ArrayList<Double> rostralanteriorcingulate_vol_list = ASBR.getListOf_Rostralanteriorcingulate_Vol();
        ArrayList<Double> rostralmiddlefrontal_vol_list = ASBR.getListOf_Rostralmiddlefrontal_Vol();
        ArrayList<Double> superiorfrontal_vol_list = ASBR.getListOf_Superiorfrontal_Vol();
        ArrayList<Double> superiorparietal_vol_list = ASBR.getListOf_Superiorparietal_Vol();
        ArrayList<Double> superiortemporal_vol_list = ASBR.getListOf_Superiortemporal_Vol();
        ArrayList<Double> supramarginal_vol_list = ASBR.getListOf_Supramarginal_Vol();
        ArrayList<Double> transversetemporal_vol_list = ASBR.getListOf_Transversetemporal_Vol();
        ArrayList<Double> insula_vol_list = ASBR.getListOf_Insula_Vol();

        ArrayList<Integer> NumVert_list = ASBR.getListOf_TotalNumVert();
        ArrayList<Double> WhiteSurfArea_list = ASBR.getListOf_WhiteSurfArea();
        ArrayList<Double> MeanThickness_list = ASBR.getListOf_MeanThickness();
        ArrayList<Double> BrainSegVol_list = ASBR.getListOf_BrainSegVol();
        ArrayList<Double> BrainSegVolNotVent_list = ASBR.getListOf_BrainSegVolNotVent();
        ArrayList<Double> BrainSegVolNotVentSurf_list = ASBR.getListOf_BrainSegVolNotVentSurf();
        ArrayList<Double> CortexVol_list = ASBR.getListOf_CortexVol();
        ArrayList<Double> SupraTentorialVol_list = ASBR.getListOf_SupraTentorialVol();
        ArrayList<Double> SupraTentorialVolNotVent_list = ASBR.getListOf_SupraTentorialVolNotVent();
        ArrayList<Double> eTIV_list = ASBR.getListOf_eTIV();


        //ODS化するためのツリーマップを用意
        LinkedHashMap<String, String> tm = new LinkedHashMap();
        String the1st_row_str =
                "caudalanteriorcingulate\t" +
                        "caudalmiddlefrontal\t" +
                        "cuneus\t" +
                        "entorhinal\t" +
                        "fusiform\t" +
                        "inferiorparietal\t" +
                        "inferiortemporal\t" +
                        "isthmuscingulate\t" +
                        "lateraloccipital\t" +
                        "lateralorbitofrontal\t" +
                        "lingual\t" +
                        "medialorbitofrontal\t" +
                        "middletemporal\t" +
                        "parahippocampal\t" +
                        "paracentral\t" +
                        "parsopercularis\t" +
                        "parsorbitalis\t" +
                        "parstriangularis\t" +
                        "pericalcarine\t" +
                        "postcentral\t" +
                        "posteriorcingulate\t" +
                        "precentral\t" +
                        "precuneus\t" +
                        "rostralanteriorcingulate\t" +
                        "rostralmiddlefrontal\t" +
                        "superiorfrontal\t" +
                        "superiorparietal\t" +
                        "superiortemporal\t" +
                        "supramarginal\t" +
                        "transversetemporal\t" +
                        "insula\t" +
                        "NumVert\t" +
                        "WhiteSurfArea\t" +
                        "MeanThickness\t" +
                        "BrainSegVol\t" +
                        "BrainSegVolNotVent\t" +
                        "BrainSegVolNotVentSurf\t" +
                        "CortexVol\t" +
                        "SupraTentorialVol\t" +
                        "SupraTentorialVolNotVent\t" +
                        "eTIV";


        tm.put("Sbj", the1st_row_str);
        String row_str;
        for (int i = 0; i < ASBR.getListOf_SubjectName().size(); i++) {
            row_str = "";

            row_str += caudalanteriorcingulate_vol_list.get(i) + "\t";
            row_str += caudalmiddlefrontal_vol_list.get(i) + "\t";
            row_str += cuneus_vol_list.get(i) + "\t";
            row_str += entorhinal_vol_list.get(i) + "\t";
            row_str += fusiform_vol_list.get(i) + "\t";
            row_str += inferiorparietal_vol_list.get(i) + "\t";
            row_str += inferiortemporal_vol_list.get(i) + "\t";
            row_str += isthmuscingulate_vol_list.get(i) + "\t";
            row_str += lateraloccipital_vol_list.get(i) + "\t";
            row_str += lateralorbitofrontal_vol_list.get(i) + "\t";
            row_str += lingual_vol_list.get(i) + "\t";
            row_str += medialorbitofrontal_vol_list.get(i) + "\t";
            row_str += middletemporal_vol_list.get(i) + "\t";
            row_str += parahippocampal_vol_list.get(i) + "\t";
            row_str += paracentral_vol_list.get(i) + "\t";
            row_str += parsopercularis_vol_list.get(i) + "\t";
            row_str += parsorbitalis_vol_list.get(i) + "\t";
            row_str += parstriangularis_vol_list.get(i) + "\t";
            row_str += pericalcarine_vol_list.get(i) + "\t";
            row_str += postcentral_vol_list.get(i) + "\t";
            row_str += posteriorcingulate_vol_list.get(i) + "\t";
            row_str += precentral_vol_list.get(i) + "\t";
            row_str += precuneus_vol_list.get(i) + "\t";
            row_str += rostralanteriorcingulate_vol_list.get(i) + "\t";
            row_str += rostralmiddlefrontal_vol_list.get(i) + "\t";
            row_str += superiorfrontal_vol_list.get(i) + "\t";
            row_str += superiorparietal_vol_list.get(i) + "\t";
            row_str += superiortemporal_vol_list.get(i) + "\t";
            row_str += supramarginal_vol_list.get(i) + "\t";
            row_str += transversetemporal_vol_list.get(i) + "\t";
            row_str += insula_vol_list.get(i) + "\t";
            row_str += NumVert_list.get(i) + "\t";
            row_str += WhiteSurfArea_list.get(i) + "\t";
            row_str += MeanThickness_list.get(i) + "\t";
            row_str += BrainSegVol_list.get(i) + "\t";
            row_str += BrainSegVolNotVent_list.get(i) + "\t";
            row_str += BrainSegVolNotVentSurf_list.get(i) + "\t";
            row_str += CortexVol_list.get(i) + "\t";
            row_str += SupraTentorialVol_list.get(i) + "\t";
            row_str += SupraTentorialVolNotVent_list.get(i) + "\t";
            row_str += eTIV_list.get(i) + "\t";

            tm.put(SubjectNameList.get(i), row_str);
        }

        //ODS化
        CollectionOdsWriterAndReader_ver3.addToOdsfile(tm, new File(read_target_folder_path.getPath() + "/" + OutputFileName + ".ods"), "AparcLt");


    }

}
