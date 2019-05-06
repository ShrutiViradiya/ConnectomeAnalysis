package Fs6AnlRsltCnvrtr1st.Converter;

import IuFileManagement.FileCopyTools_ver2;

import java.io.File;

/**
 * Created by issey on 2017/06/03.
 */
public class Prep5 {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        //コピー先
        String RCODE_FLD_PATH = args[0];//"C:\\Users\\issey\\Documents\\Dropbox\\docroot\\CorMapper_v1.05\\RCode";

        //コピーするファイル
        String COR_MAP_GENERATER_PATH = System.getProperty("user.dir") + "\\src\\NotUseButNotTrash.RemakeData.Fs6AnlRsltCnvrtr\\CorMap\\RCode\\1_CorMapGenerator.R";
        String COR_MAP_GENERATER_SUB_PATH = System.getProperty("user.dir") + "\\src\\NotUseButNotTrash.RemakeData.Fs6AnlRsltCnvrtr\\CorMap\\RCode\\2_CorMapGeneratorSub.R";
        String UTILS_PATH = System.getProperty("user.dir") + "\\src\\NotUseButNotTrash.RemakeData.Fs6AnlRsltCnvrtr\\CorMap\\RCode\\utils.R";
        String CALC_MES_PATH = System.getProperty("user.dir") + "\\src\\NotUseButNotTrash.RemakeData.Fs6AnlRsltCnvrtr\\CorMap\\RCode\\4-1_CalcMesOfBinaryAndUndirectedNet_v6.R";
        String COUNT_EXC_DIFF_PATH = System.getProperty("user.dir") + "\\src\\NotUseButNotTrash.RemakeData.Fs6AnlRsltCnvrtr\\CorMap\\RCode\\CountExceedingDifferences_v3.R";
        String EDGE_SWAP_RAND_PATH = System.getProperty("user.dir") + "\\src\\NotUseButNotTrash.RemakeData.Fs6AnlRsltCnvrtr\\CorMap\\RCode\\edge_swapping_randomization.R";
        String NODE_INFO_PATH = System.getProperty("user.dir") + "\\src\\NotUseButNotTrash.RemakeData.Fs6AnlRsltCnvrtr\\CorMap\\RCode\\NodeInfo_Base_v1.txt";
        String REMAKE_NET_PATH = System.getProperty("user.dir") + "\\src\\NotUseButNotTrash.RemakeData.Fs6AnlRsltCnvrtr\\CorMap\\RCode\\remake_network_with_optimal_threshold.R";
        String VOL_MEAN_PATH = System.getProperty("user.dir") + "\\src\\NotUseButNotTrash.RemakeData.Fs6AnlRsltCnvrtr\\CorMap\\RCode\\5_MakeEachAreaVolSummary.R";
        String VOL_MEAN_SUB_PATH = System.getProperty("user.dir") + "\\src\\NotUseButNotTrash.RemakeData.Fs6AnlRsltCnvrtr\\CorMap\\RCode\\VolMeanSummarizerSub.R";
        String AGE_SEX_EFFECT_RMOVER_PATH = System.getProperty("user.dir") + "\\src\\NotUseButNotTrash.RemakeData.Fs6AnlRsltCnvrtr\\CorMap\\RCode\\AgeSexEffectRemover.R";
        String SHOW_NET_PATH = System.getProperty("user.dir") + "\\src\\NotUseButNotTrash.RemakeData.Fs6AnlRsltCnvrtr\\CorMap\\RCode\\showNetwork.R";
        String SHOW_NET_2_PATH = System.getProperty("user.dir") + "\\src\\NotUseButNotTrash.RemakeData.Fs6AnlRsltCnvrtr\\CorMap\\RCode\\showNetwork2.R";

        //コピー実行
        System.out.println(FileCopyTools_ver2.copy_with_overwriting(COR_MAP_GENERATER_PATH, RCODE_FLD_PATH +"\\"+ new File(COR_MAP_GENERATER_PATH).getName()));
        System.out.println(FileCopyTools_ver2.copy_with_overwriting(COR_MAP_GENERATER_SUB_PATH, RCODE_FLD_PATH +"\\"+ new File(COR_MAP_GENERATER_SUB_PATH).getName()));
        System.out.println(FileCopyTools_ver2.copy_with_overwriting(UTILS_PATH, RCODE_FLD_PATH +"\\"+ new File(UTILS_PATH).getName()));
        System.out.println(FileCopyTools_ver2.copy_with_overwriting(CALC_MES_PATH, RCODE_FLD_PATH +"\\"+ new File(CALC_MES_PATH).getName()));
        System.out.println(FileCopyTools_ver2.copy_with_overwriting(COUNT_EXC_DIFF_PATH, RCODE_FLD_PATH +"\\"+ new File(COUNT_EXC_DIFF_PATH).getName()));
        System.out.println(FileCopyTools_ver2.copy_with_overwriting(EDGE_SWAP_RAND_PATH, RCODE_FLD_PATH +"\\"+ new File(EDGE_SWAP_RAND_PATH).getName()));
        System.out.println(FileCopyTools_ver2.copy_with_overwriting(NODE_INFO_PATH, RCODE_FLD_PATH +"\\"+ new File(NODE_INFO_PATH).getName()));
        System.out.println(FileCopyTools_ver2.copy_with_overwriting(REMAKE_NET_PATH, RCODE_FLD_PATH +"\\"+ new File(REMAKE_NET_PATH).getName()));
        System.out.println(FileCopyTools_ver2.copy_with_overwriting(VOL_MEAN_PATH, RCODE_FLD_PATH +"\\"+ new File(VOL_MEAN_PATH).getName()));
        System.out.println(FileCopyTools_ver2.copy_with_overwriting(VOL_MEAN_SUB_PATH, RCODE_FLD_PATH +"\\"+ new File(VOL_MEAN_SUB_PATH).getName()));
        System.out.println(FileCopyTools_ver2.copy_with_overwriting(AGE_SEX_EFFECT_RMOVER_PATH, RCODE_FLD_PATH +"\\"+ new File(AGE_SEX_EFFECT_RMOVER_PATH).getName()));
        System.out.println(FileCopyTools_ver2.copy_with_overwriting(SHOW_NET_2_PATH, RCODE_FLD_PATH +"\\"+ new File(SHOW_NET_2_PATH).getName()));

    }
}
