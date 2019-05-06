package Fs6AnlRsltCnvrtr.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 参考資料
 * http://qiita.com/nwtgck/items/5dd7a3008a5a31bf964e
 * https://docs.oracle.com/cd/E26537_01/tutorial/essential/io/pathOps.html#convert
 * Created by issey on 2017/06/06.
 */
public class PathResolver_v1 {
    public static void main(String[] args) {
        args = new String[]{
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.05\\StatsData_converted\\MDDBandA\\surf+deep\\SubjectName.txt",
                "C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.05\\RCode\\MDDBandA"};


        System.out.println(getRelativePath(args[0], args[1]));
        System.out.println(getRelativePathStr(args[0], args[1]));

        System.out.println(getAbsolutePath("../../"));

    }

    /**
     * @return
     */
    static String getRelativePathStr(String target_abs_path_str, String base_abs_path_str) {
        Path target_path = new File(target_abs_path_str).toPath();
        Path base_path = new File(base_abs_path_str).toPath();
        Path base_to_target_path = base_path.relativize(target_path);
        return base_to_target_path.normalize().toString().replaceAll("\\\\", "/");
    }

    static Path getRelativePath(String target_abs_path_str, String base_abs_path_str) {
        Path target_path = new File(target_abs_path_str).toPath();
        Path base_path = new File(base_abs_path_str).toPath();
        Path base_to_target_path = base_path.relativize(target_path);
        return base_to_target_path;
    }

    static String getAbsolutePath(String relative_path) {
        try {
            return Paths.get(relative_path).toRealPath(LinkOption.NOFOLLOW_LINKS).toString();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return "";
        }
    }
}
