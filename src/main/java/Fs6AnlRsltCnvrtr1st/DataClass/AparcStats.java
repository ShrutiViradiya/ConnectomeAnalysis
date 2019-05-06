package Fs6AnlRsltCnvrtr1st.DataClass;

import java.io.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by issey on 2016/11/16.
 */
public class AparcStats {

    public static void main(String[] args) {

        String read_target_file_path = "C:/Users/issey/Documents/Dropbox/docroot/20161124_FS6_ANL_RSLT/AD001_lh.aparc.DKTatlas.stats";
        new AparcStats(read_target_file_path);
    }

    final String AparcStatsLtFileName;
    final String SubjectName;

    //# Measure Cortex, NumVert, Number of Vertices, 139169, unitless
    Pattern pttrn_TotalNumVert = Pattern.compile("^# Measure Cortex, NumVert, (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_TotalNumVert;
    int TotalNumVert = 0;
    //# Measure Cortex, WhiteSurfArea, White Surface Total Area, 90971.7, mm^2
    Pattern pttrn_WhiteSurfArea = Pattern.compile("^# Measure Cortex, WhiteSurfArea, (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_WhiteSurfArea;
    double WhiteSurfArea = 0.0;
    //# Measure Cortex, MeanThickness, Mean Thickness, 2.01255, mm
    Pattern pttrn_MeanThickness = Pattern.compile("^# Measure Cortex, MeanThickness, (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_MeanThickness;
    double MeanThickness = 0.0;
    //# Measure BrainSeg, BrainSegVol, Brain Segmentation Volume, 1095187.000000, mm^3
    Pattern pttrn_BrainSegVol = Pattern.compile("^# Measure BrainSeg, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_BrainSegVol;
    double BrainSegVol = 0.0;
    //# Measure BrainSegNotVent, BrainSegVolNotVent, Brain Segmentation Volume Without Ventricles, 1042807.000000, mm^3
    Pattern pttrn_BrainSegVolNotVent = Pattern.compile("^# Measure BrainSegNotVent, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_BrainSegVolNotVent;
    double BrainSegVolNotVent = 0.0;
    //# Measure BrainSegNotVentSurf, BrainSegVolNotVentSurf, Brain Segmentation Volume Without Ventricles from Surf, 1044156.736979, mm^
    Pattern pttrn_BrainSegVolNotVentSurf = Pattern.compile("^# Measure BrainSegNotVentSurf, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_BrainSegVolNotVentSurf;
    double BrainSegVolNotVentSurf = 0.0;
    //# Measure Cortex, CortexVol Total cortical gray matter volume, 363746.550179, mm^3
    Pattern pttrn_CortexVol = Pattern.compile("^# Measure Cortex, CortexVol (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_CortexVol;
    double CortexVol = 0.0;
    //# Measure SupraTentorial, SupraTentorialVol, Supratentorial volume, 973138.736979, mm^3
    Pattern pttrn_SupraTentorialVol = Pattern.compile("^# Measure SupraTentorial, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_SupraTentorialVol;
    double SupraTentorialVol = 0.0;
    //# Measure SupraTentorialNotVent, SupraTentorialVolNotVent, Supratentorial volume, 926329.736979, mm^3
    Pattern pttrn_SupraTentorialVolNotVent = Pattern.compile("^# Measure SupraTentorialNotVent, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_SupraTentorialVolNotVent;
    double SupraTentorialVolNotVent = 0.0;
    //# Measure EstimatedTotalIntraCranialVol, eTIV, Estimated Total Intracranial Volume, 1499299.436566, mm^3
    Pattern pttrn_eTIV = Pattern.compile("^# Measure EstimatedTotalIntraCranialVol, (.+), (.+), ([0-9.]+), (.+)$");
    Matcher mtchr_eTIV;
    double eTIV = 0.0;


    HashMap<String, AreaInfo> AreaInfoMap = new HashMap();

    /* -----------------------------------------------------------------------------*/

    public AparcStats(String aparc_stats_file_path) {
        aparc_stats_file_path = aparc_stats_file_path.replaceAll("\\\\", "/");
        AparcStatsLtFileName = aparc_stats_file_path.substring(aparc_stats_file_path.lastIndexOf("/") + 1);
        SubjectName = AparcStatsLtFileName.substring(0, AparcStatsLtFileName.indexOf("_"));

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(aparc_stats_file_path), "UTF-8"));

            String line;
            String[] line_words;
            String StructName;
            int NumVert;
            int SurfArea;
            int GrayVol;
            double ThickAvg;
            double ThickStd;
            double MeanCurv;
            double GausCurv;
            int FoldInd;
            double CurvInd;

            AreaInfo area_info;
            while ((line = br.readLine()) != null) {

                //Matcherの用意
                mtchr_TotalNumVert = pttrn_TotalNumVert.matcher(line);
                mtchr_WhiteSurfArea = pttrn_WhiteSurfArea.matcher(line);
                mtchr_MeanThickness = pttrn_MeanThickness.matcher(line);
                mtchr_BrainSegVol = pttrn_BrainSegVol.matcher(line);
                mtchr_BrainSegVolNotVent = pttrn_BrainSegVolNotVent.matcher(line);
                mtchr_BrainSegVolNotVentSurf = pttrn_BrainSegVolNotVentSurf.matcher(line);
                mtchr_CortexVol = pttrn_CortexVol.matcher(line);
                mtchr_SupraTentorialVol = pttrn_SupraTentorialVol.matcher(line);
                mtchr_SupraTentorialVolNotVent = pttrn_SupraTentorialVolNotVent.matcher(line);
                mtchr_eTIV = pttrn_eTIV.matcher(line);

                if (line.startsWith("#")) {
                    //System.out.println(line);
                    if (mtchr_TotalNumVert.find()) {
                        TotalNumVert = Integer.parseInt(mtchr_TotalNumVert.group(2));
                        System.out.println("TotalNumVert=" + TotalNumVert);
                    } else if (mtchr_WhiteSurfArea.find()) {
                        WhiteSurfArea = Double.parseDouble(mtchr_WhiteSurfArea.group(2));
                    } else if (mtchr_MeanThickness.find()) {
                        MeanThickness = Double.parseDouble(mtchr_MeanThickness.group(2));
                    } else if (mtchr_BrainSegVol.find()) {
                        BrainSegVol = Double.parseDouble(mtchr_BrainSegVol.group(3));
                    } else if (mtchr_BrainSegVolNotVent.find()) {
                        BrainSegVolNotVent = Double.parseDouble(mtchr_BrainSegVolNotVent.group(3));
                    } else if (mtchr_BrainSegVolNotVentSurf.find()) {
                        BrainSegVolNotVentSurf = Double.parseDouble(mtchr_BrainSegVolNotVentSurf.group(3));
                    } else if (mtchr_CortexVol.find()) {
                        CortexVol = Double.parseDouble(mtchr_CortexVol.group(2));
                    } else if (mtchr_SupraTentorialVol.find()) {
                        SupraTentorialVol = Double.parseDouble(mtchr_SupraTentorialVol.group(3));
                    } else if (mtchr_SupraTentorialVolNotVent.find()) {
                        SupraTentorialVolNotVent = Double.parseDouble(mtchr_SupraTentorialVolNotVent.group(3));
                    } else if (mtchr_eTIV.find()) {
                        eTIV = Double.parseDouble(mtchr_eTIV.group(3));
                    }
                } else {
                    line_words = line.split("\\s+");
                    for (String line_word : line_words) {
                        //System.out.println(line_word);
                    }
                    StructName = line_words[0];
                    NumVert = Integer.parseInt(line_words[1]);
                    SurfArea = Integer.parseInt(line_words[2]);
                    GrayVol = Integer.parseInt(line_words[3]);
                    ThickAvg = Double.parseDouble(line_words[4]);
                    ThickStd = Double.parseDouble(line_words[5]);
                    MeanCurv = Double.parseDouble(line_words[6]);
                    GausCurv = Double.parseDouble(line_words[7]);
                    FoldInd = Integer.parseInt(line_words[8]);
                    CurvInd = Double.parseDouble(line_words[9]);

                    area_info = new AreaInfo(StructName, NumVert, SurfArea, GrayVol, ThickAvg, ThickStd, MeanCurv, GausCurv, FoldInd, CurvInd);
                    AreaInfoMap.put(area_info.getStructName(), area_info);
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

    /* -----------------------------------------------------------------------------*/
    public HashMap<String, AreaInfo> getAreaInfoMap() {
        return AreaInfoMap;
    }

    public String getFileName() {
        return AparcStatsLtFileName;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    /* ----------------------------------------------------------------------------- */

    public double geteTIV() {
        return eTIV;
    }

    public double getCortexVol() {
        return CortexVol;
    }

    public double getBrainSegVolNotVentSurf() {
        return BrainSegVolNotVentSurf;
    }

    public double getBrainSegVolNotVent() {
        return BrainSegVolNotVent;
    }

    public double getBrainSegVol() {
        return BrainSegVol;
    }

    public int getTotalNumVert() {
        return TotalNumVert;
    }

    public double getSupraTentorialVol() {
        return SupraTentorialVol;
    }

    public double getSupraTentorialVolNotVent() {
        return SupraTentorialVolNotVent;
    }

    public double getWhiteSurfArea() {
        return WhiteSurfArea;
    }

    public double getMeanThickness() {
        return MeanThickness;
    }
    /* ----------------------------------------------------------------------------- */

    class AreaInfo {
        String StructName;
        int NumVert;
        int SurfArea;
        double GrayVol;
        double ThickAvg;
        double ThickStd;
        double MeanCurv;
        double GausCurv;
        double FoldInd;
        double CurvInd;


        public AreaInfo(String struct_name, int num_vert, int surf_area, double gray_vol,
                        double thick_avg, double thick_std, double mean_curv, double gaus_curv, int fold_ind, double curv_ind) {
            this.StructName = struct_name;
            this.NumVert = num_vert;
            this.SurfArea = surf_area;
            this.GrayVol = gray_vol;
            this.ThickAvg = thick_avg;
            this.ThickStd = thick_std;
            this.MeanCurv = mean_curv;
            this.GausCurv = gaus_curv;
            this.FoldInd = fold_ind;
            this.CurvInd = curv_ind;
        }

        public double getCurvInd() {
            return CurvInd;
        }

        public double getFoldInd() {
            return FoldInd;
        }

        public double getGausCurv() {
            return GausCurv;
        }

        public double getGrayVol() {
            return GrayVol;
        }

        public double getMeanCurv() {
            return MeanCurv;
        }

        public int getNumVert() {
            return NumVert;
        }

        public String getStructName() {
            return StructName;
        }

        public int getSurfArea() {
            return SurfArea;
        }

        public double getThickAvg() {
            return ThickAvg;
        }

        public double getThickStd() {
            return ThickStd;
        }

    }
}
