package me.darcade.minecraftlottery;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class UpdateChecker {

	public String checkversion() {

		try {
			URL Projectfiles = new URL(
					"http://dev.bukkit.org/bukkit-plugins/minecraft-lottery/files.rss");
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(Projectfiles.openStream());
			doc.getDocumentElement().normalize();
			
			Element element = (Element) doc.getElementsByTagName("item").item(0);
			return element.getElementsByTagName("title").item(0).getTextContent().replaceAll("[A-Za-z ]", "").toString();
			
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public URL getDownload() {

		try {
			URL Projectfiles = new URL(
					"http://dev.bukkit.org/bukkit-plugins/minecraft-lottery/files.rss");
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(Projectfiles.openStream());
			doc.getDocumentElement().normalize();
			
			Element element = (Element) doc.getElementsByTagName("item").item(0);
			return new URL(element.getElementsByTagName("link").item(0).getTextContent().toString());
			
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
