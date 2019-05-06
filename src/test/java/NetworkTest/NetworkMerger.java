/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NetworkTest;

import IuXmlSupport.graphml.GraphmlReader_ver5;
import iMtx.IllegalMatrixCalcException;
import iMtx.iMtx_v4;
import org.gephi.graph.api.*;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.Exporter;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

import java.io.*;
import java.util.ArrayList;

/**
 * 平均的なNetworkファイルを生成するために書いたプログラム。
 * しかし結局平均的なネットワークは統計には使えないと思われ、FilteredNetworkを作ることにしたんじゃなかったけなぁ。
 * <p>
 * @author issey
 */
public class NetworkMerger extends GraphmlReader_ver5 {

    static String DestiNationFolderPath;

    static String[] data_type = {"NMLwithGG", "NMLwithGAorAA", "MDDwithGG", "MDDwithGAorAA", "NML", "MDD"};

    public static void main(String[] args) {
        NetworkMerger magf = new NetworkMerger();
        //magf.makeSummary(data_type[0]);
        //magf.makeSummary(data_type[1]);
        //magf.makeSummary(data_type[2]);
        //magf.makeSummary(data_type[3]);
        //magf.makeSummary(data_type[4]);
        magf.doOperation(data_type[5]);
    }

    /**
     *
     * 複数のGraphmlファイルからEdgeの値の代表値（平均値等）を導き
     * １つのGraphmlファイルへ集約する。
     * <p>
     * @param group_type
     */
    public void doOperation(String group_type) {

        System.out.println("##### 複数のGraphmlファイルからEdgeの値の代表値を導き１つのGraphmlファイルへ集約する #####");

        /**
         * 解析対象となるGraphmlFileのリストを作る
         */
        //★     static String[]  data_type = {"NMLwithGG", "NMLwithGAorAA", "MDDwithGG", "MDDwithGAorAA", "NML", "MDD" };
        Object[] reading_targets = null;
        ReadTargetListMaker_BDNF_ver2 rtlmBDNF = new ReadTargetListMaker_BDNF_ver2();
        if (group_type.equals("NMLwithGG")) {
            reading_targets = rtlmBDNF.getGraphmlfilePathes_of_NMLwithGG().toArray();
        } else if (group_type.equals("NMLwithGAorAA")) {
            reading_targets = rtlmBDNF.getGraphmlfilePathes_of_NMLwithGAorAA().toArray();
        } else if (group_type.equals("MDDwithGG")) {
            reading_targets = rtlmBDNF.getGraphmlfilePathes_of_MDDwithGG().toArray();
        } else if (group_type.equals("MDDwithGAorAA")) {
            reading_targets = rtlmBDNF.getGraphmlfilePathes_of_MDDwithGAorAA().toArray();
        } else if (group_type.equals("NML")) {
            reading_targets = rtlmBDNF.getGraphmlfilePathes_of_NML().toArray();
        } else if (group_type.equals("MDD")) {
            reading_targets = rtlmBDNF.getGraphmlfilePathes_of_MDD().toArray();
        } else {
            System.out.println("error");
            System.exit(0);
        }

        System.out.println("解析対象ファイル数：" + reading_targets.length);
        System.out.println("今回解析対象とするGraphmlFile：");
        for (Object obj : reading_targets) {
            String TargetPath = (String) obj;
            System.out.println(TargetPath);
        }
        System.out.println("");

        /**
         * 出力先の確認
         */
        DestiNationFolderPath = "C:\\Temp\\AverageConnectivities";

        System.out.println("平均値関連ファイル出力先: " + DestiNationFolderPath);
        System.out.println("中間ファイル出力先: " + DestiNationFolderPath + "\\tempfiles_of_" + group_type);
        File dest_root = new File(DestiNationFolderPath);
        File dest_temp = new File(DestiNationFolderPath + "\\tempfiles_of_" + group_type);
        boolean skipflag = false;
        if (dest_temp.exists()) {
            //System.out.println("既に「" + DestiNationFolderPath + "\\temp" + "」が存在します。上書き回避ため、matrixファイルへの変換はスキップします。");
            //skipflag = true;
            //System.out.println("既に「" +  DestiNationFolderPath + "\\temp" + "」が存在します。上書き回避ため、プログラムを終了します。");
            //System.exit(0);
        } else {
            dest_temp.mkdirs();
        }

        /**
         * matrixファイルの作成
         */
        NetworkMerger MAGF = new NetworkMerger();
        if (skipflag != true) {
            System.out.println("#######   Step1: 各Graphmlファイルをmatrixファイルへ変換します。 #####");
            String OutputFilePath = "";

            String OutputFileNameBase = "matrix_of_number_of_fibers";
            for (int i = 0; i < reading_targets.length; i++) {
                System.out.println("■Reading " + reading_targets[i] + "...");
                System.out.println("　#####" + getMemoryInfo() + "#####");
                OutputFilePath = dest_temp.getAbsolutePath() + "\\" + OutputFileNameBase + "_" + String.format("%03d", i) + ".txt";
                MAGF.makeMatrixFiles((String) reading_targets[i], OutputFilePath, "number_of_fibers");
            }

        }

        /**
         *
         */
        System.out.println("#######   Step2: 各matrixファイルの値から代表値を算出して新たなmatrixファイルを生成します。 #####");
        System.out.println(dest_temp.getAbsolutePath());
        System.out.println(dest_root.getAbsolutePath());
        MAGF.makeAverageMatrix(dest_temp.getAbsolutePath(), dest_root.getAbsolutePath() + "\\AverageMatrix_of_" + group_type + ".txt");

        /**
         *
         */
        System.out.println("#######   Step3: 続いてmatrixファイルをGraphmlファイルに変換します。 #####");
        String MatrixFilePath = dest_root.getAbsolutePath() + "\\AverageMatrix_of_" + group_type + ".txt";
        String TargetGraphmlFilePath = reading_targets[0].toString();
        String DataType = "number_of_fibers";
        //Graphmlのロード
        GraphModel graphmodel;

        /**
         * Prepare WorkSpace
         */
        project_controller = Lookup.getDefault().lookup(ProjectController.class);
        project_controller.newProject();
        workspace = project_controller.getCurrentWorkspace();

        /**
         * Prepare ImportController
         */
        importController = Lookup.getDefault().lookup(ImportController.class);

        /**
         * Prepare Container
         */
        try {
            System.out.println("    TargetGraphmlFilePath（書き換え対象Graphml）: " + TargetGraphmlFilePath);
            GephiFile = new File(TargetGraphmlFilePath);
            container = importController.importFile(GephiFile);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        /**
         * Append container to workspace with ImportController
         * <p>
         * これを繰り返し呼び出すとメモリーリークを起こす。
         * 使用後にprocessor=nullしてもダメ。
         * <p>
         */
        if (processor == null) {
            processor = new DefaultProcessor();
        }
        importController.process(container, processor, workspace);

        /**
         * Get a graph model - it exists because we have a workspace
         */
        graphmodel = Lookup.getDefault().lookup(GraphController.class).getModel();
        //int TotalNodeCount = graphmodel.getGraph().getNodeCount();
        //System.out.println("◆NodeCount:" + TotalNodeCount);
        Graph loadedGraph = graphmodel.getGraph();

        //Matrixのロード
        System.out.println("    MatrixFilePath（書き換えるためのデータ）: " + MatrixFilePath);
        File MatrixFile = new File(MatrixFilePath);
        MatrixFile.getParentFile().mkdirs();
        try {
            MatrixFile.createNewFile();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        iMtx_v4 TempMatrix = new iMtx_v4(MatrixFile);
        loadedGraph.clearEdges();
        Edge newEdge = null;
        float weight = 1.0f;
        for (int row = 1; row <= TempMatrix.getRowLength(); row++) {
            for (int column = 1; column <= TempMatrix.getColLength(); column++) {
                if (row <= column) {
                    double value = TempMatrix.getElementAt(row - 1, column - 1);
                    if (value < 200) {

                    } else {
                        int fiber_number = (int) (value);
                        System.out.println("fiber_number: " + fiber_number);
                        Node sourceNode = null;
                        Node targetNode = null;
                        sourceNode = graphmodel.getGraph().getNode(String.valueOf(row));
                        targetNode = graphmodel.getGraph().getNode(String.valueOf(column));
                        newEdge = graphmodel.factory().newEdge(sourceNode, targetNode, weight, false);
                        newEdge.getAttributes().setValue(DataType, String.valueOf(fiber_number));
                        loadedGraph.addEdge(newEdge);
                    }

                }
            }
        }//End of for

        //Graphmlの書き出し
        String OutputFilePath = DestiNationFolderPath + "\\Average_of_" + group_type + ".graphml";
        System.out.println("    OutputFilePath: " + OutputFilePath);
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        Exporter exporterGraphML = ec.getExporter("graphml");     //Get GraphML exporter
        if (workspace == null) {
            System.out.println("workspaceがnullです。");
        }
        exporterGraphML.setWorkspace(workspace);
        try {
            ec.exportFile(new File(OutputFilePath), exporterGraphML);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        /**
         * ロードしたGraphmlファイルの後片付け
         */
        project_controller.closeCurrentWorkspace();
        project_controller.closeCurrentProject();
        project_controller.deleteWorkspace(workspace);
        project_controller = null;
        container = null;
        importController = null;
        workspace = null;
        processor = null;

    }

    /**
     * DataTypeには「number_of_fibers」「fiber_length_mean」「fiber_length_std」のいずれかを入れる。
     *
     * @return
     */
    //public String[][] makeAverageMatrix(String MatrixFilesParentPath, String OutputFilePath, String DataType) {
    public void makeAverageMatrix(String MatrixFilesParentPath, String OutputFilePath) {

        String[][] resultMatrix;

        //要約対象となるmatrixファイルの一覧を作る。
        ArrayList ArrayOfReadTarget = new ArrayList();
        FilenameFilter fnf = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                int x = name.lastIndexOf('.');
                String extent = name.substring(x + 1).toLowerCase();
                if (extent.equals("txt")) {
                    return true;
                }
                return false;
            }
        };
        System.out.println("MatrixFilesParentPath: " + MatrixFilesParentPath);
        File MtrixFilesParentFolder = new File(MatrixFilesParentPath);
        //System.out.println(ParentFolder.getAbsolutePath());
        if (!MtrixFilesParentFolder.exists()) {
            System.out.println("PatheOfParentFolder「" + MatrixFilesParentPath + "」が存在しません。");
            System.exit(0);
        }
        for (File ChildFile : MtrixFilesParentFolder.listFiles(fnf)) {
            //System.out.println(ChildFile.getName());
            ArrayOfReadTarget.add(ChildFile.getAbsolutePath());
        }

        //各matrixファイルを読み、double[][]型の配列に変換し、行列計算をする。
        BufferedReader br = null;
        iMtx_v4 AverageMatrix = null;
        iMtx_v4 TempMatrix = null;

        for (Object ReadTarget : ArrayOfReadTarget) {
            System.out.println("ReadTarget: " + ReadTarget.toString());

            TempMatrix = new iMtx_v4(new File(ReadTarget.toString()));
            //AverageMatrix に TempMatrix を 加えてゆく
            if (AverageMatrix == null) {
                AverageMatrix = new iMtx_v4(TempMatrix.getRowLength(), TempMatrix.getColLength());
            }
            try {
                AverageMatrix = iMtx_v4.add(AverageMatrix, TempMatrix);
            } catch (IllegalMatrixCalcException e) {
                e.printStackTrace();
            }
        }

        //System.out.println("*********総和*************");
        //cMatrix_ver2.printMatrix(AverageMatrix);
        //System.out.println("**********************");
        //TempMatrixを加えた回数で割って平均値を出す。
        AverageMatrix.scaleWith(1.0 / ArrayOfReadTarget.size());
        //System.out.println("**********平均************");
        //cMatrix_ver2.printMatrix(AverageMatrix);
        //System.out.println("**********************");

        //四捨五入して小数値を整数値にする
        //AverageMatrix.RoundOff();
        //System.out.println("**********平均（四捨五入後）************");
        //cMatrix_ver2.printMatrix(AverageMatrix);
        //System.out.println("**********************");

        //AverageMatrixを出力してみる
        iMtx_v4.exportMatrixAsTextfile(AverageMatrix, OutputFilePath);

        System.out.println("#######   終了   #####");

    }

}
