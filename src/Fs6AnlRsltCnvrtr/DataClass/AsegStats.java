package Fs6AnlRsltCnvrtr.DataClass;

import java.io.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * aseg.stasファイルに対応したクラス
 * Created by issey on 2016/11/15.
 */
public class AsegStats {

    public static void main(String[] args) {

        String read_target_file_path = "C:/Users/issey/Documents/Dropbox/docroot/20161124_FS6_ANL_RSLT/AD001_aseg.stats";
        AsegStats aseg_stats = new AsegStats(read_target_file_path);
        System.out.println(aseg_stats.getSengemtInfoMap().get("WM-hypointensities").getVolume_mm3());
    }


    final String AsegStatsFileName;
    final String SubjectName;

    //# Measure BrainSeg, BrainSegVol, Brain Segmentation Volume, 1095187.000000, mm^3
    Pattern pttrn_BrainSegVol = Pattern.compile("^# Measure BrainSeg, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_BrainSegVol;
    double BrainSegVol = 0.0;
    //# Measure BrainSegNotVent, BrainSegVolNotVent, Brain Segmentation Volume Without Ventricles, 1042807.000000, mm^3
    Pattern pttrn_BrainSegVolNotVent = Pattern.compile("^# Measure BrainSegNotVent, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_BrainSegVolNotVent;
    double BrainSegVolNotVent = 0.0;
    //# Measure BrainSegNotVentSurf, BrainSegVolNotVentSurf, Brain Segmentation Volume Without Ventricles from Surf, 1044156.736979, mm^3
    Pattern pttrn_BrainSegVolNotVentSurf = Pattern.compile("# Measure BrainSegNotVentSurf, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_BrainSegVolNotVentSurf;
    double BrainSegVolNotVentSurf = 0.0;
    //# Measure VentricleChoroidVol, VentricleChoroidVol, Volume of ventricles and choroid plexus, 46809.000000, mm^3
    Pattern pttrn_VentricleChoroidVol = Pattern.compile("# Measure VentricleChoroidVol, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_VentricleChoroidVol;
    double VentricleChoroidVol = 0.0;
    //# Measure lhCortex, lhCortexVol, Left hemisphere cortical gray matter volume, 179829.788477, mm^3
    Pattern pttrn_lhCortexVol = Pattern.compile("# Measure lhCortex, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_lhCortexVol;
    double lhCortexVol = 0.0;
    //# Measure rhCortex, rhCortexVol, Right hemisphere cortical gray matter volume, 183916.761702, mm^3
    Pattern pttrn_rhCortexVol = Pattern.compile("# Measure rhCortex, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_rhCortexVol;
    double rhCortexVol = 0.0;
    //# Measure Cortex, CortexVol, Total cortical gray matter volume, 363746.550179, mm^3
    Pattern pttrn_CortexVol = Pattern.compile("# Measure Cortex, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_CortexVol;
    double CortexVol = 0.0;
    //# Measure lhCerebralWhiteMatter, lhCerebralWhiteMatterVol, Left hemisphere cerebral white matter volume, 261004.604645, mm^3
    Pattern pttrn_lhCerebralWhiteMatterVol = Pattern.compile("# Measure lhCerebralWhiteMatter, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_lhCerebralWhiteMatterVol;
    double lhCerebralWhiteMatterVol = 0.0;
    //# Measure rhCerebralWhiteMatter, rhCerebralWhiteMatterVol, Right hemisphere cerebral white matter volume, 254419.582155, mm^3
    Pattern pttrn_rhCerebralWhiteMatterVol = Pattern.compile("# Measure rhCerebralWhiteMatter, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_rhCerebralWhiteMatterVol;
    double rhCerebralWhiteMatterVol = 0.0;
    //# Measure CerebralWhiteMatter, CerebralWhiteMatterVol, Total cerebral white matter volume, 515424.186800, mm^3
    Pattern pttrn_CerebralWhiteMatterVol = Pattern.compile("# Measure CerebralWhiteMatter, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_CerebralWhiteMatterVol;
    double CerebralWhiteMatterVol = 0.0;
    //# Measure SubCortGray, SubCortGrayVol, Subcortical gray matter volume, 44587.000000, mm^3
    Pattern pttrn_SubCortGrayVol = Pattern.compile("# Measure SubCortGray, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_SubCortGrayVol;
    double SubCortGrayVol = 0.0;
    //# Measure TotalGray, TotalGrayVol, Total gray matter volume, 507066.550179, mm^3
    Pattern pttrn_TotalGrayVol = Pattern.compile("# Measure TotalGray, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_TotalGrayVol;
    double TotalGrayVol = 0.0;
    //# Measure SupraTentorial, SupraTentorialVol, Supratentorial volume, 973138.736979, mm^3
    Pattern pttrn_SupraTentorialVol = Pattern.compile("# Measure SupraTentorial, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_SupraTentorialVol;
    double SupraTentorialVol = 0.0;
    //# Measure SupraTentorialNotVent, SupraTentorialVolNotVent, Supratentorial volume, 926329.736979, mm^3
    Pattern pttrn_SupraTentorialVolNotVent = Pattern.compile("# Measure SupraTentorialNotVent, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_SupraTentorialVolNotVent;
    double SupraTentorialVolNotVent = 0.0;
    //# Measure SupraTentorialNotVentVox, SupraTentorialVolNotVentVox, Supratentorial volume voxel count, 922153.000000, mm^3
    Pattern pttrn_SupraTentorialVolNotVentVox = Pattern.compile("# Measure SupraTentorialNotVentVox, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_SupraTentorialVolNotVentVox;
    double SupraTentorialVolNotVentVox = 0.0;
    //# Measure Mask, MaskVol, Mask Volume, 1518608.000000, mm^3
    Pattern pttrn_MaskVol = Pattern.compile("# Measure Mask, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_MaskVol;
    double MaskVol = 0.0;
    //# Measure BrainSegVol-to-eTIV, BrainSegVol-to-eTIV, Ratio of BrainSegVol to eTIV, 0.730466, unitless
    Pattern pttrn_BrainSegVol_to_eTIV = Pattern.compile("# Measure BrainSegVol-to-eTIV, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_BrainSegVol_to_eTIV;
    double BrainSegVol_to_eTIV = 0.0;
    //# Measure MaskVol-to-eTIV, MaskVol-to-eTIV, Ratio of MaskVol to eTIV, 1.012878, unitless
    Pattern pttrn_MaskVol_to_eTIV = Pattern.compile("# Measure MaskVol-to-eTIV, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_MaskVol_to_eTIV;
    double MaskVol_to_eTIV = 0.0;
    //# Measure lhSurfaceHoles, lhSurfaceHoles, Number of defect holes in lh surfaces prior to fixing, 94, unitless
    Pattern pttrn_lhSurfaceHoles = Pattern.compile("# Measure lhSurfaceHoles, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_lhSurfaceHoles;
    int lhSurfaceHoles = 0;
    //# Measure rhSurfaceHoles, rhSurfaceHoles, Number of defect holes in rh surfaces prior to fixing, 131, unitless
    Pattern pttrn_rhSurfaceHoles = Pattern.compile("# Measure rhSurfaceHoles, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_rhSurfaceHoles;
    int rhSurfaceHoles = 0;
    //# Measure SurfaceHoles, SurfaceHoles, Total number of defect holes in surfaces prior to fixing, 225, unitless
    Pattern pttrn_SurfaceHoles = Pattern.compile("# Measure SurfaceHoles, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_SurfaceHoles;
    int SurfaceHoles = 0;
    //# Measure EstimatedTotalIntraCranialVol, eTIV, Estimated Total Intracranial Volume, 1499299.436566, mm^3
    Pattern pttrn_eTIV = Pattern.compile("# Measure EstimatedTotalIntraCranialVol, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_eTIV;
    double eTIV = 0.0;


    HashMap<String, SegmentInfo> SegmentMap = new HashMap();

    public AsegStats(String aseg_stats_file_path) {
        aseg_stats_file_path = aseg_stats_file_path.replaceAll("\\\\", "/");
        AsegStatsFileName = aseg_stats_file_path.substring(aseg_stats_file_path.lastIndexOf("/") + 1);
        SubjectName = AsegStatsFileName.substring(0, AsegStatsFileName.indexOf("_"));

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(aseg_stats_file_path), "UTF-8"));

            String line;
            String[] line_words;
            int Index;
            int SegId;
            int NVoxels;
            double Volume_mm3;
            String StructName;
            double normMean;
            double normStdDev;
            double normMin;
            double normMax;
            double normRange;
            SegmentInfo segmentInfo;
            while ((line = br.readLine()) != null) {

                //Matcherの用意
                mtchr_BrainSegVol = pttrn_BrainSegVol.matcher(line);
                mtchr_BrainSegVolNotVent = pttrn_BrainSegVolNotVent.matcher(line);
                mtchr_BrainSegVolNotVentSurf = pttrn_BrainSegVolNotVentSurf.matcher(line);
                mtchr_VentricleChoroidVol = pttrn_VentricleChoroidVol.matcher(line);
                mtchr_lhCortexVol = pttrn_lhCortexVol.matcher(line);
                mtchr_rhCortexVol = pttrn_rhCortexVol.matcher(line);
                mtchr_CortexVol = pttrn_CortexVol.matcher(line);
                mtchr_lhCerebralWhiteMatterVol = pttrn_lhCerebralWhiteMatterVol.matcher(line);
                mtchr_rhCerebralWhiteMatterVol = pttrn_rhCerebralWhiteMatterVol.matcher(line);
                mtchr_CerebralWhiteMatterVol = pttrn_CerebralWhiteMatterVol.matcher(line);
                mtchr_SubCortGrayVol = pttrn_SubCortGrayVol.matcher(line);
                mtchr_TotalGrayVol = pttrn_TotalGrayVol.matcher(line);
                mtchr_SupraTentorialVol = pttrn_SupraTentorialVol.matcher(line);
                mtchr_SupraTentorialVolNotVent = pttrn_SupraTentorialVolNotVent.matcher(line);
                mtchr_SupraTentorialVolNotVentVox = pttrn_SupraTentorialVolNotVentVox.matcher(line);
                mtchr_MaskVol = pttrn_MaskVol.matcher(line);
                mtchr_BrainSegVol_to_eTIV = pttrn_BrainSegVol_to_eTIV.matcher(line);
                mtchr_MaskVol_to_eTIV = pttrn_MaskVol_to_eTIV.matcher(line);
                mtchr_lhSurfaceHoles = pttrn_lhSurfaceHoles.matcher(line);
                mtchr_rhSurfaceHoles = pttrn_rhSurfaceHoles.matcher(line);
                mtchr_SurfaceHoles = pttrn_SurfaceHoles.matcher(line);
                mtchr_eTIV = pttrn_eTIV.matcher(line);

                //System.out.println(line);
                if (line.startsWith("#")) {
                    if (mtchr_BrainSegVol.find()) {
                        BrainSegVol = Double.parseDouble(mtchr_BrainSegVol.group(3));
                    } else if (mtchr_BrainSegVolNotVent.find()) {
                        BrainSegVolNotVent = Double.parseDouble(mtchr_BrainSegVolNotVent.group(3));
                    } else if (mtchr_BrainSegVolNotVentSurf.find()) {
                        BrainSegVolNotVentSurf = Double.parseDouble(mtchr_BrainSegVolNotVentSurf.group(3));
                    } else if (mtchr_VentricleChoroidVol.find()) {
                        VentricleChoroidVol = Double.parseDouble(mtchr_VentricleChoroidVol.group(3));
                    } else if (mtchr_lhCortexVol.find()) {
                        lhCortexVol = Double.parseDouble(mtchr_lhCortexVol.group(3));
                    } else if (mtchr_rhCortexVol.find()) {
                        rhCortexVol = Double.parseDouble(mtchr_rhCortexVol.group(3));
                    } else if (mtchr_CortexVol.find()) {
                        CortexVol = Double.parseDouble(mtchr_CortexVol.group(3));
                    } else if (mtchr_lhCerebralWhiteMatterVol.find()) {
                        lhCerebralWhiteMatterVol = Double.parseDouble(mtchr_lhCerebralWhiteMatterVol.group(3));
                    } else if (mtchr_rhCerebralWhiteMatterVol.find()) {
                        rhCerebralWhiteMatterVol = Double.parseDouble(mtchr_rhCerebralWhiteMatterVol.group(3));
                    } else if (mtchr_CerebralWhiteMatterVol.find()) {
                        CerebralWhiteMatterVol = Double.parseDouble(mtchr_CerebralWhiteMatterVol.group(3));
                    } else if (mtchr_SubCortGrayVol.find()) {
                        SubCortGrayVol = Double.parseDouble(mtchr_SubCortGrayVol.group(3));
                    } else if (mtchr_TotalGrayVol.find()) {
                        TotalGrayVol = Double.parseDouble(mtchr_TotalGrayVol.group(3));
                    } else if (mtchr_SupraTentorialVol.find()) {
                        SupraTentorialVol = Double.parseDouble(mtchr_SupraTentorialVol.group(3));
                    } else if (mtchr_SupraTentorialVolNotVent.find()) {
                        SupraTentorialVolNotVent = Double.parseDouble(mtchr_SupraTentorialVolNotVent.group(3));
                    } else if (mtchr_SupraTentorialVolNotVentVox.find()) {
                        SupraTentorialVolNotVentVox = Double.parseDouble(mtchr_SupraTentorialVolNotVentVox.group(3));
                    } else if (mtchr_MaskVol.find()) {
                        MaskVol = Double.parseDouble(mtchr_MaskVol.group(3));
                    } else if (mtchr_BrainSegVol_to_eTIV.find()) {
                        BrainSegVol_to_eTIV = Double.parseDouble(mtchr_BrainSegVol_to_eTIV.group(3));
                    } else if (mtchr_MaskVol_to_eTIV.find()) {
                        MaskVol_to_eTIV = Double.parseDouble(mtchr_MaskVol_to_eTIV.group(3));
                    } else if (mtchr_lhSurfaceHoles.find()) {
                        lhSurfaceHoles = Integer.parseInt(mtchr_lhSurfaceHoles.group(3));
                    } else if (mtchr_rhSurfaceHoles.find()) {
                        rhSurfaceHoles = Integer.parseInt(mtchr_rhSurfaceHoles.group(3));
                    } else if (mtchr_SurfaceHoles.find()) {
                        SurfaceHoles = Integer.parseInt(mtchr_SurfaceHoles.group(3));
                    } else if (mtchr_eTIV.find()) {
                        eTIV = Double.parseDouble(mtchr_eTIV.group(3));
                    }
                } else {
                    line_words = line.split("\\s+");
                    for (String line_word : line_words) {
                        //System.out.println(line_word);
                    }
                    Index = Integer.parseInt(line_words[1]);
                    SegId = Integer.parseInt(line_words[2]);
                    NVoxels = Integer.parseInt(line_words[3]);
                    Volume_mm3 = Double.parseDouble(line_words[4]);
                    StructName = line_words[5];
                    normMean = Double.parseDouble(line_words[6]);
                    normStdDev = Double.parseDouble(line_words[7]);
                    normMin = Double.parseDouble(line_words[8]);
                    normMax = Double.parseDouble(line_words[9]);
                    normRange = Double.parseDouble(line_words[10]);

                    segmentInfo = new SegmentInfo(Index, SegId, NVoxels, Volume_mm3, StructName, normMean, normStdDev, normMin, normMax, normRange);
                    SegmentMap.put(segmentInfo.getStructName(), segmentInfo);
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
    }

    public HashMap<String, SegmentInfo> getSengemtInfoMap() {
        return SegmentMap;
    }

    public String getFileName() {
        return AsegStatsFileName;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    /* ----------------------------------------------------- */

    public double getBrainSegVol() {
        return BrainSegVol;
    }

    public double getBrainSegVol_to_eTIV() {
        return BrainSegVol_to_eTIV;
    }

    public double getBrainSegVolNotVent() {
        return BrainSegVolNotVent;
    }

    public double getBrainSegVolNotVentSurf() {
        return BrainSegVolNotVentSurf;
    }

    public double getCerebralWhiteMatterVol() {
        return CerebralWhiteMatterVol;
    }

    public double getCortexVol() {
        return CortexVol;
    }

    public double geteTIV() {
        return eTIV;
    }

    public double getLhCerebralWhiteMatterVol() {
        return lhCerebralWhiteMatterVol;
    }

    public double getLhCortexVol() {
        return lhCortexVol;
    }

    public int getLhSurfaceHoles() {
        return lhSurfaceHoles;
    }

    public double getMaskVol() {
        return MaskVol;
    }

    public double getMaskVol_to_eTIV() {
        return MaskVol_to_eTIV;
    }

    public double getSubCortGrayVol() {
        return SubCortGrayVol;
    }

    public double getSupraTentorialVol() {
        return SupraTentorialVol;
    }

    public double getSupraTentorialVolNotVent() {
        return SupraTentorialVolNotVent;
    }

    public double getSupraTentorialVolNotVentVox() {
        return SupraTentorialVolNotVentVox;
    }

    public int getSurfaceHoles() {
        return SurfaceHoles;
    }

    public double getTotalGrayVol() {
        return TotalGrayVol;
    }

    public double getVentricleChoroidVol() {
        return VentricleChoroidVol;
    }

    public int getRhSurfaceHoles() {
        return rhSurfaceHoles;
    }

    public double getRhCortexVol() {
        return rhCortexVol;
    }

    public double getRhCerebralWhiteMatterVol() {
        return rhCerebralWhiteMatterVol;
    }

    /* ----------------------------------------------------- */

    class SegmentInfo {
        int Index;
        int SegId;
        int NVoxels;
        double Volume_mm3;
        String StructName;
        double normMean;
        double normStdDev;
        double normMin;
        double normMax;
        double normRange;


        public SegmentInfo(int index, int segId, int NVoxels, double volume_mm3,
                           String structName,
                           double normMean, double normStdDev,
                           double normMix, double normMax, double normRange) {
            this.Index = index;
            this.SegId = segId;
            this.NVoxels = NVoxels;
            this.Volume_mm3 = volume_mm3;
            this.StructName = structName;
            this.normMean = normMean;
            this.normStdDev = normStdDev;
            this.normMin = normMin;
            this.normMax = normMax;
            this.normRange = normRange;
        }

        public double getNormMax() {
            return normMax;
        }

        public double getNormMin() {
            return normMin;
        }


        public double getNormRange() {
            return normRange;
        }


        public double getNormStdDev() {
            return normStdDev;
        }


        public int getNVoxels() {
            return NVoxels;
        }


        public int getSegId() {
            return SegId;
        }


        public String getStructName() {
            return StructName;
        }


        public double getVolume_mm3() {
            return Volume_mm3;
        }
    }
}
