package fr.trxyy.updater;

import java.io.File;
import java.net.Proxy;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.trxyy.base.Configuration;
import fr.trxyy.interfaces.utils.FileUtil;
import fr.trxyy.interfaces.utils.OSUtil;
import fr.trxyy.interfaces.utils.Wrapper;

public class GameParser {
	
	private int totalFilesInFolder = 0;
	
	  public static void getFilesToDownload() {
		  
		  	Wrapper.log("Preparation de la mise a jour.");
			Proxy proxy = Proxy.NO_PROXY;
	      try {
	          final URL resourceUrl = new URL(Configuration.getDownloadUrl());
	          final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	          final DocumentBuilder db = dbf.newDocumentBuilder();
	          final Document doc = db.parse(resourceUrl.openConnection(proxy).getInputStream());
	          final NodeList nodeLst = doc.getElementsByTagName("Contents");
	          
	          final long start = System.nanoTime();
	          for(int i = 0; i < nodeLst.getLength(); i++) {
	              final Node node = nodeLst.item(i);
	              if(node.getNodeType() == 1) {
	                  final Element element = (Element) node;
	                  final String key = element.getElementsByTagName("Key").item(0).getChildNodes().item(0).getNodeValue();
	                  String etag = element.getElementsByTagName("ETag") != null ? element.getElementsByTagName("ETag").item(0).getChildNodes().item(0).getNodeValue() : "-";
	                  final long size = Long.parseLong(element.getElementsByTagName("Size").item(0).getChildNodes().item(0).getNodeValue());
	                  
						File localFile = new File(OSUtil.getDirectory(), key);
						if (!localFile.isDirectory()) {						
	                      if(etag.length() > 1) {
	                          etag = FileUtil.getEtag(etag);
	                          if (localFile.exists()) {
	                          	 if(localFile.isFile() && localFile.length() == size) {
	                                 final String localMd5 = FileUtil.getMD5(localFile);
	                                 if(!localMd5.equals(etag)) {
	                                	 GameUpdater.filesToDownload.put(key, new LauncherFile(size, Configuration.getDownloadUrl() + key, localFile.getAbsolutePath()));
	                                 }
	                          	 }
		                          else {
			                          	if (!(Configuration.getDownloadUrl() + key).endsWith("/")) {
			                          		GameUpdater.filesToDownload.put(key, new LauncherFile(size, Configuration.getDownloadUrl() + key, localFile.getAbsolutePath()));
			                          	}
		                          }
	                          }
	                          else {
	                          	if (!(Configuration.getDownloadUrl() + key).endsWith("/")) {
	                          		GameUpdater.filesToDownload.put(key, new LauncherFile(size, Configuration.getDownloadUrl() + key, localFile.getAbsolutePath()));
	                          	}
	                          }
	                          
	                      }
						}
						else {
							localFile.mkdir();
							localFile.mkdirs();
						}
	              }
	          }
	          final long end = System.nanoTime();
	          final long delta = end - start;
	          Wrapper.log("Temps (delta) pour comparer les ressources: " + delta / 1000000L + " ms");
	      }
	      catch(final Exception ex) {
	      	Wrapper.log("Impossible de telecharger les ressources (" + ex + ")");
	      }
	  }
}
