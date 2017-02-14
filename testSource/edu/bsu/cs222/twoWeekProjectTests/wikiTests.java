package edu.bsu.cs222.twoWeekProjectTests;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import edu.bsu.cs222.brattonharrison.WikiURL;
import edu.bsu.cs222.brattonharrison.XMLParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class wikiTests {
    private Document document;
    public wikiTests() {
    }

    @Before
    public void setUp() throws IOException, SAXException, ParserConfigurationException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("api.php.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        this.document = builder.parse(inputStream);
    }

    @Test
    public void testStringBuilder() {
        WikiURL urlStringBuilder = new WikiURL("soup");
        String t = urlStringBuilder.toString();
        Assert.assertEquals("https://en.wikipedia.org/w/api.php?action=query&format=xml&prop=revisions&titles=" +
                "soup&rvprop=timestamp|comment|user&rvlimit=30&redirects", t);
    }

    @Test
    public void testWikiParser() {
        NodeList children = this.document.getChildNodes();
        Element apiElement = (Element)children.item(0);
        System.out.println(apiElement.toString());
        System.out.println(children.toString());
        Assert.assertEquals("api", apiElement.getNodeName());
    }

    @Test
    public void testWikiParserQuery() {
        NodeList list = this.document.getElementsByTagName("rev");
        Node nNode = list.item(0);
        Element nodeElement = (Element)nNode;
        Assert.assertEquals("Northamerica1000", nodeElement.getAttribute("user"));
    }

    @Test
    public void testNoConnection(){
        WikiURL connectionTest = new WikiURL("soup");
        Assert.assertEquals(true,connectionTest.isValid());
    }
    @Test
    public void testURLEncoder(){
        WikiURL connectionTest = new WikiURL("Barack Obama");
        Assert.assertEquals("https://en.wikipedia.org/w/api.php?action=query&format=xml&prop=revisions&titles="
                +"Barack+Obama&rvprop=timestamp|comment|user&rvlimit=30&redirects" ,connectionTest.toString());
    }

    @Test
    public void testRedirects() throws MalformedURLException {
        XMLParser parser = new XMLParser();
        parser.createDocument(new URL("https://en.wikipedia.org/w/api.php?action=query&format=xml&prop=revisions&titles=+Barack+obama&rvprop=timestamp|comment|user&rvlimit=30&redirects"));

        Assert.assertEquals("Redirected From: Barack obama To: Barack Obama", parser.checkForRedirects() );
    }
}