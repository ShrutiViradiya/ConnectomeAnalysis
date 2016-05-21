package BrainMapper_ver4.utils;

import org.apache.commons.jxpath.*;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http://d.hatena.ne.jp/tacohachi/20090129/p1
 * Created by issey on 2016/04/10.
 */
public class JXPathWriteHelper_ver1 {


    public static void main(String[] args) {

        /**
         * 生成
         */
        JXPathWriteHelper_ver1 jxphelper = new JXPathWriteHelper_ver1();

        /**
         * DomDocumentの生成
         */
        Document domDocument = JXPathWriteHelper_ver1.createNewDomDocument("output");


        /**
         * 値の追加
         */
        //ルール
        //存在しない属性値を指定したノードの追加はfalse
        //存在しないまたは存在しても属性値の一致しないノードを経由しているとfalse
        //属性値を指定しないノードの追加は上書き
        //属性値を指定しないと最も最初に出現した（もっとも最後に追加された）ノードを意味する
        jxphelper.appendJXPath(domDocument, "/output/area/a/x", "存在しないノードを経由して新規ノード作成。");
        jxphelper.appendJXPath(domDocument, "/output/area/a/x/@id", "属性の設定");
        //jxphelper.appendJXPath(domDocument, "/output/area/a/x", "上書き");//上書き

        jxphelper.appendJXPath(domDocument, "/output/area/a/x[@id='1']", "ノードとしては存在するが属性の異なるノードへの書き込み");//上書き
        //jxphelper.appendJXPath(domDocument, "/output/area/a/x[@id='1']", "");//上書き

        jxphelper.appendJXPath(domDocument, "/output/area[@id='2']/x", "ノードとしては存在するが属性が異なるノードを経由した場合の、ノードの新規生成。");
        jxphelper.appendJXPath(domDocument, "/output/area[@id='3']/@c", "ノードとしては存在するが属性が異なるノードを経由した場合の、属性の新規生成。");

        jxphelper.appendJXPath(domDocument, "/output/area[@id='4']", "属性値の囲み文字（クォーテーション）に関する実験");
        //jxphelper.appendJXPath(domDocument, "/output/area[@id=4]", "属性値指定にクォーテーションがない場合");
        jxphelper.appendJXPath(domDocument, "/output/area[@id='ひらがな']", "属性名がひらがなの場合");
        jxphelper.appendJXPath(domDocument, "/output/area[@id=\"4\"]", "属性値の囲み文字がダブルクォーテーションの場合");
        jxphelper.appendJXPath(domDocument, "/output/area[@id=ひらがな]/@id", "経由ノードの属性値指定にクォーテーションがない場合");

        /**
         * 書き込み
         */
        jxphelper.outputDomDocumentToFile(domDocument, new File("src/BrainMapper/utils/20160411.xml"));

    }

    /**
     * //存在しない属性値を指定したノードの追加はfalse
     * //存在しないまたは存在しても属性値の一致しないノードを経由しているとfalse
     * //属性値を指定しないノードの追加は上書き.最初に出現した（もっとも最後に追加された）ノードを上書き。
     *
     * @param domDocument
     * @param aXPath
     * @param value
     * @return
     */
    public boolean appendJXPath(Document domDocument, String aXPath, Object value) {
        System.out.println("------ JXPathHelper#appendJXPath() ------");

        //aXPathの整合性を確認。ノードや属性の名前は数字で始まってはいけない。
        String regex1 = "^[@]*[0-9]+.*";
        Matcher m1 = null;
        for (String sub_str : aXPath.split("/")) {
            System.out.print("/");
            System.out.print(sub_str);
            m1 = Pattern.compile(regex1).matcher(sub_str);
            if (m1.find()) {
                System.out.print("←×");
                System.out.println("XPathの記述を見なおしてください。NodeやAttributeの名前の先頭文字は数字以外にする必要あり。");
                return false;
            } else {
            }
        }
        System.out.println();

        // JXPathContextの準備
        JXPathContext context = JXPathContext.newContext(domDocument);
        context.setFactory(new OriginalFactory());


        //context.createPathAndSetValue(aXPath, value);
        //アドレスが指し示すノードが存在するか段階的に確認。
        //なければ随時作ってゆく。
        //アドレスの最後の部分が指し示すノードについては、「上書き」か「兄弟生成」かを切り替える。
        Node node = null;
        String path = "";
        String[] sub_path_strings = aXPath.split("/");
        for (int i = 1; i <= sub_path_strings.length; i++) {
            //System.out.println("  sub_path_strings[i-1]: " + sub_path_strings[i - 1]);
            path = path + "/" + sub_path_strings[i - 1];
            System.out.print(" ├ sub part of the path「" + path + "」");

            String path_attr_removed = path.replaceFirst("\\[@[a-zA-Z0-9_-]+=['\"]+.+?['\"]+\\]$", "");
            //System.out.println("  path_attr_removed: " + path_attr_removed);
            System.out.print("...exist?");

            //「path」で示されているノードの存在確認をする。
            // もしそもそもノードなければ「JXPathNotFoundException」が投げられkる。
            // ノードがあっても属性名が一致しなければ「NullPointerException」が投げられkる。
            boolean attr_identity = true;
            boolean node_existence = true;
            try {
                node = (Node) context.selectSingleNode(path);
                node.getNodeName();
            } catch (JXPathNotFoundException jxpnfe) {
                System.out.println("  ノードがない可能性");
                node_existence = false;
                attr_identity = false;

            } catch (NullPointerException npe) {
                System.out.println("  ノードがあっても属性値が一致しない可能性");
                node_existence = true;
                attr_identity = false;
            }
            if (node_existence) {
                if (attr_identity) {
                    //NodeがあってAttrbute情報も一致する場合。
                    System.out.println(" --> Yes");
                } else {
                    //NodeがあってもAttrbute情報が異なる場合。
                    System.out.println(" --> No");

                    /**
                     * 属性情報が不一致なそのNodeと同階層に、
                     * 指定されたAttribute情報を持つ新たなNodeを作る
                     */
                    System.out.println("path_attr_removed=" + path_attr_removed);
                    try {
                        //同階層に属性なしの同名ノードが存在する場合
                        node = (Node) context.selectSingleNode(path_attr_removed);
                    } catch (JXPathNotFoundException jxpnfe) {
                        //同階層に属性なしの同名ノードが存在しない場合
                        context.createPathAndSetValue(path_attr_removed, "_temp_");
                        node = (Node) context.selectSingleNode(path_attr_removed);
                    }
                    System.out.println("  そのNodeと同階層に、指定されたAttribute情報を持つ新たなNodeを作る");
                    //System.out.println("   path:" + path);
                    String regex0 = "([a-zA-Z0-9_-]+)\\[@([a-zA-Z0-9_-]+)=['\"]+(.+?)['\"]+\\]$";
                    Matcher matcher1 = Pattern.compile(regex0).matcher(path);
                    if (matcher1.find()) {
                        String node_name = matcher1.group(1);
                        String attr_name = matcher1.group(2);
                        String attr_value = matcher1.group(3);
                        Document document = (Document) context.getContextBean();
                        Element brother = document.createElement(node_name);
                        //brother.setTextContent((String) value);
                        brother.setAttribute(attr_name, attr_value);
                        node.getParentNode().insertBefore(brother, node);
                        if (node.getTextContent().equals("_temp_")) {
                            //同階層に属性なしの同名ノードが存在しない時に一時的に作ったノードを削除する
                            node.getParentNode().removeChild(node);
                        }
                        node = (Node) brother;//新たに作られたノードを選択
                        attr_identity = true;//属性情報が一致したことにする。
                    }
                }
            } else {
                //Nodeがない場合。
                System.out.println(" --> No");

            }


            //XPathアドレスの最後の部分を解釈して、状況に応じて切り替える。
            if (i == sub_path_strings.length) {
                String regex2 = "([a-zA-Z0-9_-]+)\\[@([a-zA-Z0-9_-]+)=['\"]+(.+?)['\"]+\\]$";
                Matcher m2 = Pattern.compile(regex2).matcher(path);
                String regex3 = "@([a-zA-Z0-9_-]+)$";
                Matcher m3 = Pattern.compile(regex3).matcher(path);
                String regex4 = "/([a-zA-Z0-9_-]+)$";
                Matcher m4 = Pattern.compile(regex4).matcher(path);


                if (m2.find()) {
                    System.out.println("  属性指定があるノード追加");

                    String node_name = m2.group(1);
                    String attr_name = m2.group(2);
                    String attr_value = m2.group(3);
                    Document document = (Document) context.getContextBean();
                    Element brother = document.createElement(node_name);
                    brother.setTextContent((String) value);
                    brother.setAttribute(attr_name, attr_value);
                    if (attr_identity) {//同一属性なら上書き
                        System.out.println("  書き換え");
                        node.getParentNode().appendChild(brother);
                        node.getParentNode().removeChild(node);
                    } else {
                        System.out.println("    兄弟生成");
                        node.getParentNode().insertBefore(brother, node);
                    }
                    return true;
                } else if (m3.find()) {//属性の設定
                    System.out.println("  属性値の設定");
                    String attr_name = m3.group(1);
                    try {
                        context.createPathAndSetValue(path, value);//既存ノードを上書き
                    } catch (JXPathException e) {
                        System.err.println("    XPath適用エラー");
                        System.err.println("    存在しないノードを経由しようとしている可能性。");
                        System.err.println("    属性値の指定の際に「'」「\"」を用いていない可能性。");
                        //e.printStackTrace();
                        return false;
                    }
                    return true;
                } else if (m4.find()) {//属性指定のないノード指定
                    System.out.println("  属性指定のないノード生成");
                    String node_name = m4.group(1);
                    try {
                        context.createPathAndSetValue(path, value);//既存ノードを上書き

                    } catch (JXPathException e) {
                        System.err.println("    XPath適用エラー");
                        System.err.println("    存在しないノードを経由しようとしている可能性。");
                        System.err.println("    属性値の指定の際に「'」「\"」を用いていない可能性。");
                        //e.printStackTrace();
                        return false;
                    }
                    return true;
                } else {
                    System.err.println("  XPath適用エラー");
                    System.err.println("    XPathの記述が間違っている可能性あり。Node名やAttribute名を確認してください。");
                    System.err.println("    属性値の指定の際に「'」「\"」を用いていない可能性。");
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 新たなDomDocumentを生成するメソッド
     *
     * @param root_tag_name
     * @return
     */

    static public Document createNewDomDocument(String root_tag_name) {
        Document domDocument = null;
        DocumentBuilderFactory factory = null;
        try {
            factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            domDocument = builder.newDocument();
            Element root = (Element) domDocument.createElement(root_tag_name);
            domDocument.appendChild(root);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return domDocument;
    }

    /**
     * 既存のXMLファイルからDomDocumentを作るメソッド
     *
     * @param xml_file_path
     * @return
     */
    static public Document getDomDocument(String xml_file_path) {
        Document domDocument = null;
        DocumentBuilderFactory factory = null;
        try {
            factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            domDocument = builder.parse(new File(xml_file_path));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException saxe) {
            saxe.printStackTrace();
        }
        return domDocument;
    }


    static public boolean outputDomDocumentToFile(Document domDocument, File outputfile) {
        if (!outputfile.exists()) {
            outputfile.getParentFile().mkdirs();
            try {
                outputfile.createNewFile();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            }
        }

        try {
            Writer writer = null;
            TransformerFactory tfactory = TransformerFactory.newInstance();
            Transformer transformer = tfactory.newTransformer();

            DOMSource dom_source = new DOMSource(domDocument);

            writer = new FileWriter(outputfile);
            StreamResult sr = new StreamResult(writer);

            transformer.transform(dom_source, sr);

            writer.flush();
            writer.close();


            //transformer.transform(new DOMSource(domDocument), new StreamResult(outputfile));
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            return false;
        } catch (TransformerException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return true;
    }


    /**
     * Jakarata-Commons-JXPathの abstractクラスである
     * AbstractFactory の実装例
     * http://www.jxpath.com/dom/jxpath/tips3.htmlより
     *
     * @author www.jxpath.com
     */
    class OriginalFactory extends AbstractFactory {


        /**
         * 実装必須メソッド
         *
         * @param context JXPathContextです。Documentインスタンスの取得に使う
         * @param pointer 追加するものがノードか属性か判定する
         * @param parent  追加するノードの直属の親を指す
         * @param name
         * @param index   同名のXPathの何番目に追加するか示す(サンプルでは未使用)
         */
        public boolean createObject(JXPathContext context,
                                    Pointer pointer,
                                    Object parent,
                                    String name,
                                    int index) {
            System.out.println("    <" + name + ">というノード生成(@OriginalFactory#createObject())");

            // ノードか属性か判定する
            boolean isAttribute = ((NodePointer) pointer).isAttribute();

            Element element = (Element) parent;
            if (isAttribute) {
                // 属性の時
                element.setAttribute(name, "");
            } else {
                // ノードの時
                Document document = (Document) context.getContextBean();
                Element child = document.createElement(name);


                element.appendChild(child);


            }

            // 追加成功
            return true;
        }

        /**
         * 実装必須メソッドなのですが、とりあえず放置
         */
        public boolean declareVariable(JXPathContext context,
                                       String name) {
            return false;
        }
    }
}
