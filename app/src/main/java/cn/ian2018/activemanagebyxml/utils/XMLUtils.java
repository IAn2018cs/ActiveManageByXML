package cn.ian2018.activemanagebyxml.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import cn.ian2018.activemanagebyxml.MyApplication;
import cn.ian2018.activemanagebyxml.model.Active;

/**
 * Created by Administrator on 2017/9/9/009.
 */

public class XMLUtils {

    // 解析
    public static List<Active> readXMLFile(List<Active> lists) {
        String path = MyApplication.getContext().getExternalFilesDir("xml").getPath() + "/" + "active.xml";
        lists.clear();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(path));
            NodeList activeList = doc.getElementsByTagName("active");
            for (int i = 0; i < activeList.getLength(); i++) {
                Node activeNode = activeList.item(i);
                if (activeNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element activeElement = (Element) activeNode;
                    Active active = new Active();
                    active.setId(Integer.valueOf(activeElement.getAttribute("id")));
                    active.setName(activeElement.getElementsByTagName("name").item(0).getTextContent());
                    active.setTime(activeElement.getElementsByTagName("time").item(0).getTextContent());
                    active.setLocation(activeElement.getElementsByTagName("location").item(0).getTextContent());
                    active.setImage(activeElement.getElementsByTagName("image").item(0).getTextContent());
                    lists.add(active);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lists;
    }

    // 增
    public static boolean addActive(Active active) {
        try {
            String path = MyApplication.getContext().getExternalFilesDir("xml").getPath() + "/" + "active.xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(path));

            // 获得根节点
            Element actives = (Element) doc.getElementsByTagName("actives").item(0);

            Element activex = doc.createElement("active");
            // 设置元素active的属性值
            activex.setAttribute("id", active.getId() + "");

            // 创建名称节点 并插入父节点中
            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode(active.getName()));
            activex.appendChild(name);

            // 创建时间节点 并插入父节点中
            Element time = doc.createElement("time");
            time.appendChild(doc.createTextNode(active.getTime()));
            activex.appendChild(time);

            // 创建地点节点 并插入父节点中
            Element location = doc.createElement("location");
            location.appendChild(doc.createTextNode(active.getLocation()));
            activex.appendChild(location);

            // 创建图片节点 并插入父节点中
            Element image = doc.createElement("image");
            image.appendChild(doc.createTextNode(active.getImage()));
            activex.appendChild(image);

            // 将新建的active节点 插入到根节点中
            actives.appendChild(activex);

            // 重新改写文件
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(path));

            transformer.transform(source, result);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删
    public static boolean deleteXMLById(int id) {
        try {
            String path = MyApplication.getContext().getExternalFilesDir("xml").getPath() + "/" + "active.xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(path));

            Element actives = (Element) doc.getElementsByTagName("actives").item(0);

            NodeList activeList = doc.getElementsByTagName("active");
            for (int i = 0; i < activeList.getLength(); i++) {
                Node activeNode = activeList.item(i);
                if (activeNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element activeElement = (Element) activeNode;
                    if (id == Integer.valueOf(activeElement.getAttribute("id"))) {
                        actives.removeChild(activeElement);

                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource source = new DOMSource(doc);
                        StreamResult result = new StreamResult(new File(path));

                        transformer.transform(source, result);

                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 改
    public static boolean changeXMLById(int id, Active active) {
        try {
            String path = MyApplication.getContext().getExternalFilesDir("xml").getPath() + "/" + "active.xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(path));

            NodeList activeList = doc.getElementsByTagName("active");
            for (int i = 0; i < activeList.getLength(); i++) {
                Node activeNode = activeList.item(i);
                if (activeNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element activeElement = (Element) activeNode;
                    if (id == Integer.valueOf(activeElement.getAttribute("id"))) {
                        activeElement.getElementsByTagName("name").item(0).setTextContent(active.getName());
                        activeElement.getElementsByTagName("time").item(0).setTextContent(active.getTime());
                        activeElement.getElementsByTagName("location").item(0).setTextContent(active.getLocation());
                        activeElement.getElementsByTagName("image").item(0).setTextContent(active.getImage());

                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource source = new DOMSource(doc);
                        StreamResult result = new StreamResult(new File(path));

                        transformer.transform(source, result);

                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 查
    public static List<Active> selectXMLByName(String name) {
        List<Active> activeList = new ArrayList<>();
        try {
            String path = MyApplication.getContext().getExternalFilesDir("xml").getPath() + "/" + "active.xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(path));

            NodeList activeListX = doc.getElementsByTagName("active");

            for (int i = 0; i < activeListX.getLength(); i++) {
                Node activeNode = activeListX.item(i);
                if (activeNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element activeElement = (Element) activeNode;
                    String nameX = activeElement.getElementsByTagName("name").item(0).getTextContent();
                    if (nameX.contains(name)) {
                        Active active = new Active();

                        active.setId(Integer.valueOf(activeElement.getAttribute("id")));
                        active.setName(activeElement.getElementsByTagName("name").item(0).getTextContent());
                        active.setTime(activeElement.getElementsByTagName("time").item(0).getTextContent());
                        active.setLocation(activeElement.getElementsByTagName("location").item(0).getTextContent());
                        active.setImage(activeElement.getElementsByTagName("image").item(0).getTextContent());

                        activeList.add(active);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return activeList;
        }
        return activeList;
    }
}
