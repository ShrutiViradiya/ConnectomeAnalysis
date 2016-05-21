package BrainMapper_ver4.utils;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by issey on 2016/04/10.
 */
public class JXPathReadHelper_ver1 {

    JXPathContext context;

    public JXPathReadHelper_ver1(String XML_FILE_Path) throws FileNotFoundException {

        File xmlFile = new File(XML_FILE_Path);
        if (xmlFile.exists()) {
            Document xmlDocument = null;
            // XMLファイルをDocumentとして読み込む。
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                xmlDocument = builder.parse(xmlFile);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (org.xml.sax.SAXException e) {
                e.printStackTrace();
            }

            // 読み込んだDocumnetを元にJXPathContextを生成する。
            JXPathContext context = JXPathContext.newContext(xmlDocument);

            this.context = context;
        } else {
            throw new FileNotFoundException();
        }
    }

    public static void main(String[] args) throws IOException {
        JXPathReadHelper_ver1 JXPRH = new JXPathReadHelper_ver1("src/xml_jxpath_test/countries.xml");

        //属性名を指定し要素値を取得（最初に見つかったものだけ取得できる）
        String value1 = (String) JXPRH.getContext().getValue("/countries/country/name");
        System.out.println(value1);
        Double value3 = (Double) JXPRH.getContext().getValue("/countries/country/size", Double.TYPE);
        System.out.println(value3);

        //属性値の取得
        String data2 = (String) JXPRH.getContext().getValue("/countries/country/@area");
        System.out.println(data2);
        Object data4 = JXPRH.getContext().getValue("/countries/country[@area]");
        System.out.println("data4: " + data4);

        //属性名を指定し要素値を取得
        Object data3 = JXPRH.getContext().getValue("/countries/country[@area='North America']/name");
        System.out.println(data3);

        //網羅的にデータを参照するにはイテレーターを使う
        Iterator it = JXPRH.getContext().iterate("/countries/country");
        String country_name = "";
        while (it.hasNext()) {
            country_name = (String) it.next();
            System.out.println(country_name);
        }

        //丁寧にノードにアクセスする実験
        System.out.println("---------- Pointerに関する実験 ----------");
        Pointer p = JXPRH.getContext().getPointer("/countries/country[@area=\"Europe\"]");
        System.out.println(p);
        //p = JXPRH.getContext().getPointerByKey("/countries/country/name", "Germany");
        //System.out.println(p);

        System.out.println("---------- selectNoesを使って丁寧にアクセスする実験 ----------");
        System.out.println(JXPRH.getListOfTargetElementValue("/countries/country/name"));

    }

    public JXPathContext getContext() {
        return context;
    }

    /**
     * @param XPath
     * @return
     */
    public ArrayList<String> getListOfTargetElementValue(String XPath) {
        System.out.println("---------- Start: getListOfTargetElementValue ----------");
        ArrayList<String> result_list = new ArrayList<>();

        Pattern pttrn1 = Pattern.compile("(.+)/(.+)$");//ノード用
        Matcher mtchr1 = pttrn1.matcher(XPath);//ノード用
        Pattern pttrn2 = Pattern.compile("(.+)/@(.+)$");//属性用
        Matcher mtchr2 = pttrn2.matcher(XPath);//属性用

        if (mtchr2.find()) {
            String search_target_level = mtchr2.group(1);//検索先階層として使う
            String target_attr_name = mtchr2.group(2);//検索対象属性名として使う
            ArrayList<Node> nodelist = (ArrayList<Node>) context.selectNodes(search_target_level);
            for (int i = 0; i < nodelist.size(); i++) {//検索先階層にある全てのノードに対して
                NamedNodeMap terminal_attrs = nodelist.get(i).getAttributes();
                for (int j = 0; j < terminal_attrs.getLength(); j++) {//検索対象属性があるか調べ
                    if (terminal_attrs.item(j).getNodeName().equals(target_attr_name)) {
                        result_list.add(terminal_attrs.item(j).getTextContent());//あれば戻り値のリストに追加
                        break;
                    }
                }
            }
            System.out.println("---------- End: getListOfTargetElementValue ----------");
            return result_list;
        } else if (mtchr1.find()) {
            String search_target_level = mtchr1.group(1);//検索先階層として使う
            String target_node_name = mtchr1.group(2);//検索対象ノード名として使う
            ArrayList<Node> nodelist = (ArrayList<Node>) context.selectNodes(search_target_level);
            for (int i = 0; i < nodelist.size(); i++) {//検索先階層にある全てのノードに対して
                NodeList terminal_nodes = nodelist.get(i).getChildNodes();
                for (int j = 0; j < terminal_nodes.getLength(); j++) {//検索対象ノードがあるか調べ
                    if (terminal_nodes.item(j).getNodeName().equals(target_node_name)) {
                        result_list.add(terminal_nodes.item(j).getTextContent());//あれば戻り値のリストに追加
                        break;
                    }
                }
            }
            System.out.println("---------- End: getListOfTargetElementValue ----------");
            return result_list;
        } else {
            System.err.println("getListOfTargetElementValue()メソッドでエラー。引数を確認してください。");
            System.out.println("---------- End: getListOfTargetElementValue ----------");
            return null;
        }

    }

}
