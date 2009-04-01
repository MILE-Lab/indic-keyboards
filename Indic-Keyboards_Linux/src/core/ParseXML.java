/** ********************************************************************
 * File:           ParseXML.java
 * Description:    File to generate the DOM document to parse the XML.
 * Authors:        Akshay,Abhinava,Revati,Arun
 * Created:        Fri Mar 20 18:01:25 IST 2009
 *
 * (C) Copyright 2008, MILE Lab, IISc
 ** Licensed under the Apache License, Version 2.0 (the "License");
 ** you may not use this file except in compliance with the License.
 ** You may obtain a copy of the License at
 ** http://www.apache.org/licenses/LICENSE-2.0
 ** Unless required by applicable law or agreed to in writing, software
 ** distributed under the License is distributed on an "AS IS" BASIS,
 ** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ** See the License for the specific language governing permissions and
 ** limitations under the License.
 *
 **********************************************************************/

package core;

import java.io.File;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 

public class ParseXML{
	public static String keyboardlayoutname;
    public void getPattern(String pattern){
    try {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File(System.getProperty("user.dir"),"/kblayouts/"+keyboardlayoutname));

            // normalize text representation
            doc.getDocumentElement ().normalize ();
            NodeList listOfPatterns = doc.getElementsByTagName("pattern");
           // int totalPatterns = listOfPatterns.getLength();

            for(int s=0; s<listOfPatterns.getLength() ; s++){


                Node firstPatternNode = listOfPatterns.item(s);
                if(firstPatternNode.getNodeType() == Node.ELEMENT_NODE){


                    Element firstPatternElement = (Element)firstPatternNode;

                    //-------
                    NodeList charNameList = firstPatternElement.getElementsByTagName("char");
                    Element charElement = (Element)charNameList.item(0);

                    NodeList textFNList = charElement.getChildNodes();
                   
                   // System.out.println("The inputChar param =" + inputChar);
                    if(pattern.equals(textFNList.item(0).getNodeValue().trim())){
                    System.out.println("Char : " + 
                           ((Node)textFNList.item(0)).getNodeValue().trim());
                    
                    //-------
                    NodeList lastNameList = firstPatternElement.getElementsByTagName("unicode");
                    Element lastNameElement = (Element)lastNameList.item(0);

                    NodeList textLNList = lastNameElement.getChildNodes();
                    System.out.println("Unicode : " + ((Node)textLNList.item(0)).getNodeValue().trim());
                    core.OutputUCode.outputCode(((Node)textLNList.item(0)).getNodeValue().trim());
                    }
                }//end of if clause


            }//end of for loop with s var


        }catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " 
             + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());

        }catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();

        }catch (Throwable t) {
        t.printStackTrace ();
        }
        //System.exit (0);

    }//End of getPattern
    public static void setlang(String name){
    	keyboardlayoutname = name;
    }

}