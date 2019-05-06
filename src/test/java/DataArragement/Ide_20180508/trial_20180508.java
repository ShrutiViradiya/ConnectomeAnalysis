/**
 * 2018.05.08に井手先生からの依頼で
 * dcm2niiでdicom⇒nii.gz変換したデータを
 * 集約し、リネームした。
 */
package DataArragement.Ide_20180508;

import FileFilter.FilterConstructor.AlmightyFileFilterConstructor_Extension;
import FileFilter.FilterConstructor.IoFileFilterConstructor_OnlyDir;
import IuFileManagement.FileCopyTools_ver2;
import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;

import java.io.File;

/**
 * Created by issey on 2018/05/09.
 */
public class trial_20180508 {

    static String TARGET_ROOT_FOLDER_PATH = "H:\\20180509\\20180509_ide\\";
    static String GROUPING = "HS";
    static String DESTINATION = "H:\\20180509\\Ide_20180509_SLE_and_HS\\";

    public static void main(String[] args) {
        File[] sub_folders = new File(TARGET_ROOT_FOLDER_PATH + GROUPING + "\\").listFiles(IoFileFilterConstructor_OnlyDir.getOnlyDirFilter());

        String id = "";
        String name_date = "";
        String date = "";
        File t1w = null;
        File my = null;
        for (File sub_folder : sub_folders) {
            id = sub_folder.getName();

            name_date = getNameAndDateString(id);

            System.out.println("■" + id + " " + name_date);

            try {
                t1w = getT1wNiiFile(id);
                System.out.println("t1w=" + t1w.getName());
                copyFolder(t1w.getAbsolutePath(), DESTINATION + "\\" + GROUPING + id + "_T1" + ".nii.gz");

                my = getMyNiiFile(id);
                System.out.println("my=" + my.getName());
                copyFolder(my.getAbsolutePath(), DESTINATION + "\\" + GROUPING + id + "_MY" + ".nii.gz");

            } catch (NullPointerException npe) {
                npe.printStackTrace();

            }
        }

    }

    public static boolean copyFolder(String src_folder_path, String dst_folder_path) {
        String rslt_str = FileCopyTools_ver2.copy(src_folder_path, dst_folder_path);
        System.out.println(rslt_str);
        return false;
    }

    public static File getT1wNiiFile(String id) throws NullPointerException {
        File rslt_file = null;

        String folder_path = TARGET_ROOT_FOLDER_PATH + GROUPING + "\\" + id + "\\";
        //System.out.println(folder_path);
        String t1wi_folder_path = folder_path + "T1W_nii" + "\\";
        System.out.println("t1wi_folder_path=" + t1wi_folder_path);
        //File[] dcm_files = (new File(t1wi_folder_path)).listFiles(AlmightyFileFilterConstructor_Extension.getIoFileNameFilter("nii.gz"));
        File[] dcm_files = (new File(t1wi_folder_path)).listFiles();
        //System.out.println(dcm_files.length);
        //System.out.println(dcm_files[0].getAbsolutePath());
        rslt_file = dcm_files[0];

        return rslt_file;
    }


    public static File getMyNiiFile(String id) throws NullPointerException {
        File rslt_file = null;

        String folder_path = TARGET_ROOT_FOLDER_PATH + GROUPING + "\\" + id + "\\";
        //System.out.println(folder_path);
        String my_folder_path = folder_path + "MY_nii" + "\\";
        System.out.println("my_folder_path=" + my_folder_path);
        //File[] dcm_files = (new File(my_folder_path)).listFiles(AlmightyFileFilterConstructor_Extension.getIoFileNameFilter("nii.gz"));
        File[] dcm_files = (new File(my_folder_path)).listFiles();
        //System.out.println(dcm_files.length);
        //System.out.println(dcm_files[0].getAbsolutePath());
        rslt_file = dcm_files[0];

        return rslt_file;
    }

    public static String getNameAndDateString(String id) {
        String rslt_str = "";
        String folder_path = TARGET_ROOT_FOLDER_PATH + GROUPING + "\\" + id + "\\";
        folder_path = folder_path.replaceAll("\\\\", "/");

        File[] sub_folders = new File(folder_path).listFiles(IoFileFilterConstructor_OnlyDir.getOnlyDirFilter());
        String t1wi_fld_name = "";
        String target_dcm_folder_path = "";
        File target_dcm_folder = null;
        File[] dcm_files = null;
        String target_dcm_file_path = "";
        for (File sub_folder : sub_folders) {

            t1wi_fld_name = sub_folder.getName();
            //System.out.println(t1wi_fld_name);

            if (t1wi_fld_name.contains("_T1W")) {
                //System.out.println("T1WIフォルダを特定しました。");

                target_dcm_folder_path = folder_path + "/" + t1wi_fld_name + "/";
                //System.out.println(target_dcm_folder_path);
                target_dcm_folder = new File(target_dcm_folder_path);
                //if(target_dcm_folder!=null) break;
                dcm_files = (new File(target_dcm_folder_path)).listFiles(AlmightyFileFilterConstructor_Extension.getIoFileNameFilter("dcm"));
                //System.out.println(dcm_files.length);
                //System.out.println(dcm_files[0].getAbsolutePath());
                target_dcm_file_path = dcm_files[0].getAbsolutePath();
                if (target_dcm_file_path != "") break;
            }
        }
        //System.out.println(target_dcm_file_path);
        //System.out.println(target_dcm_folder_path);
        //System.out.println(getPtName(new File(target_dcm_file_path)));

        rslt_str = getPtName(new File(target_dcm_file_path));
        rslt_str = rslt_str.replaceAll(" ", "_");

        rslt_str = rslt_str + " " + getDate(new File(target_dcm_file_path));

        return (rslt_str);


    }

    public static String getPtName(File target_dcm_file) {
        String rslt_str = "";
        DicomObject dcmObj = new BasicDicomObject();
        DicomInputStream din = null;
        try {
            din = new DicomInputStream(target_dcm_file);
            din.readDicomObject(dcmObj, -1);
            rslt_str = dcmObj.getString(hex2int("00100010"));
            din.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rslt_str;
    }

    public static String getDate(File target_dcm_file) {
        String rslt_str = "";
        DicomObject dcmObj = new BasicDicomObject();
        DicomInputStream din = null;
        try {
            din = new DicomInputStream(target_dcm_file);
            din.readDicomObject(dcmObj, -1);
            rslt_str = dcmObj.getString(hex2int("00080022"));
            din.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rslt_str;
    }


    /**
     * 16進文字8文字（FFFFFFFF,01234567 等）で表現された数値をINTに変換
     * <p/>
     *
     * @param str <p/>
     * @return
     */
    public static int hex2int(String str) {
        if (str.length() != 8) {
            System.out.println("16進数変換に失敗");
            System.exit(0);
            return 0;
        } else {
            int k = (int) Long.parseLong(str, 16);
            return k;
        }
    }


}
