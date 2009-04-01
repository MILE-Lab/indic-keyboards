/** ********************************************************************
 * File:           XMLGenerator.java
 * Description:    File to generate the Keybaord Layout/XML builder.
 * Authors:        Akshay,Abhinava,Revati,Arun
 * Created:        Tue Mar 31 02:01:25 IST 2009
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class XMLGenerator extends org.eclipse.swt.widgets.Composite {

    private Text q;
    private Text Q;
    private Text w;
    private Text u;
    private Text Y;
    private Text y;
    private Text T;
    private Text t;
    private Text R;
    private Text r;
    private Text E;
    private Text D;
    private Text X;
    private Text z;
    private Text Z;
    private Text doubleQuote;
    private Text singleQuote;
    private Text colon;
    private Text semiColon;
    private Text L;
    private Text l;
    private Text K;
    private Text k;
    private Text J;
    private Text j;
    private Text H;
    private Text h;
    private Text G;
    private Text g;
    private Text F;
    private Text f;
    private Text d;
    private Text S;
    private Text s;
    private Text a;
    private Text A;
    private Text rightBrace;
    private Text rightArrayBracket;
    private Text rightAngularBracket;
    private Text rightBracket;
    private Text tilde;
    private Label label1;
    private Canvas canvas1;
    private Button buttonOK;
    private Text pipe;
    private Text backSlash;
    private Text plus;
    private Text equals;
    private Text underScore;
    private Text hyphen;
    private Text zero;
    private Text nine;
    private Text asteriks;
    private Text ampersand;
    private Text eight;
    private Text seven;
    private Text cap;
    private Text six;
    private Text percent;
    private Text five;
    private Text dollar;
    private Text four;
    private Text hash;
    private Text three;
    private Text AT;
    private Text two;
    private Text exclaim;
    private Text one;
    private Text backTick;
    private Text questionMark;
    private Text frontSlash;
    private Text fullStop;
    private Text leftBrace;
    private Text leftAngularBracket;
    private Text leftBracket;
    private Text leftArrayBracket;
    private Text comma;
    private Text M;
    private Text m;
    private Text N;
    private Text n;
    private Text B;
    private Text b;
    private Text V;
    private Text v;
    private Text C;
    private Text c;
    private Text x;
    private Text P;
    private Text p;
    private Text O;
    private Text o;
    private Text I;
    private Text i;
    private Text U;
    private Text e;
    private Text W;


    {
        //Register as a resource user - SWTResourceManager will
        //handle the obtaining and disposing of resources
        SWTResourceManager.registerResourceUser(this);
    }

    public XMLGenerator(Composite parent, int style) {
        super(parent, style);
        initGUI();
    }

    /**
     * Initializes the GUI.
     */
    private void initGUI() {
        try {
            this.setBackground(SWTResourceManager.getColor(117, 117, 221));
            FormLayout thisLayout = new FormLayout();
            this.setLayout(thisLayout);
            this.setSize(714, 434);
            {
                label1 = new Label(this, SWT.CENTER);
                label1.setText("Replace each of these characters with their\ncorresponding Unicodes and press OK");
                FormData label1LData = new FormData();
                label1LData.width = 590;
                label1LData.height = 59;
                label1LData.left =  new FormAttachment(0, 1000, 65);
                label1LData.top =  new FormAttachment(0, 1000, 363);
                label1.setLayoutData(label1LData);
                label1.setFont(SWTResourceManager.getFont("Tahoma", 16, 0, false, false));
                label1.setAlignment(SWT.CENTER);
                label1.setBackground(SWTResourceManager.getColor(117, 117, 221));
            }

            {
                buttonOK = new Button(this, SWT.PUSH | SWT.CENTER);
                FormData buttonOKLData = new FormData();
                buttonOKLData.width = 126;
                buttonOKLData.height = 38;
                buttonOKLData.left =  new FormAttachment(0, 1000, 296);
                buttonOKLData.top =  new FormAttachment(0, 1000, 303);
                buttonOK.setLayoutData(buttonOKLData);
                buttonOK.setText("OK");
                buttonOK.addMouseListener(new MouseAdapter() {

                    public void mouseDown(MouseEvent evt) {

                        buttonOKMouseDown(evt);
                    }
                });
            }
            {
                pipe = new Text(this, SWT.NONE);
                FormData pipeLData = new FormData();
                pipeLData.width = 35;
                pipeLData.height = 17;
                pipeLData.left = new FormAttachment(0, 1000, 668);
                pipeLData.top = new FormAttachment(0, 1000, 26);
                pipe.setLayoutData(pipeLData);
                pipe.setText("|");
            }
            {
                backSlash = new Text(this, SWT.NONE);
                FormData backSlashLData = new FormData();
                backSlashLData.width = 35;
                backSlashLData.height = 17;
                backSlashLData.left = new FormAttachment(0, 1000, 668);
                backSlashLData.top = new FormAttachment(0, 1000, 58);
                backSlash.setLayoutData(backSlashLData);
                backSlash.setText("\\");
            }
            {
                plus = new Text(this, SWT.NONE);
                FormData plusLData = new FormData();
                plusLData.width = 35;
                plusLData.height = 17;
                plusLData.left = new FormAttachment(0, 1000, 617);
                plusLData.top = new FormAttachment(0, 1000, 26);
                plus.setLayoutData(plusLData);
                plus.setText("+");
            }
            {
                equals = new Text(this, SWT.NONE);
                FormData equalsLData = new FormData();
                equalsLData.width = 35;
                equalsLData.height = 17;
                equalsLData.left = new FormAttachment(0, 1000, 617);
                equalsLData.top = new FormAttachment(0, 1000, 58);
                equals.setLayoutData(equalsLData);
                equals.setText("=");
            }
            {
                underScore = new Text(this, SWT.NONE);
                FormData underScoreLData = new FormData();
                underScoreLData.width = 35;
                underScoreLData.height = 17;
                underScoreLData.left = new FormAttachment(0, 1000, 566);
                underScoreLData.top = new FormAttachment(0, 1000, 26);
                underScore.setLayoutData(underScoreLData);
                underScore.setText("_");
            }
            {
                hyphen = new Text(this, SWT.NONE);
                FormData hyphenLData = new FormData();
                hyphenLData.width = 35;
                hyphenLData.height = 17;
                hyphenLData.left = new FormAttachment(0, 1000, 566);
                hyphenLData.top = new FormAttachment(0, 1000, 58);
                hyphen.setLayoutData(hyphenLData);
                hyphen.setText("-");
            }
            {
                rightBracket = new Text(this, SWT.NONE);
                FormData rightBracketLData = new FormData();
                rightBracketLData.width = 35;
                rightBracketLData.height = 17;
                rightBracketLData.left = new FormAttachment(0, 1000, 515);
                rightBracketLData.top = new FormAttachment(0, 1000, 26);
                rightBracket.setLayoutData(rightBracketLData);
                rightBracket.setText(")");
            }
            {
                zero = new Text(this, SWT.NONE);
                FormData zeroLData = new FormData();
                zeroLData.width = 35;
                zeroLData.height = 17;
                zeroLData.left = new FormAttachment(0, 1000, 515);
                zeroLData.top = new FormAttachment(0, 1000, 58);
                zero.setLayoutData(zeroLData);
                zero.setText("0");
            }
            {
                leftBracket = new Text(this, SWT.NONE);
                FormData leftBracketLData = new FormData();
                leftBracketLData.width = 35;
                leftBracketLData.height = 17;
                leftBracketLData.left = new FormAttachment(0, 1000, 464);
                leftBracketLData.top = new FormAttachment(0, 1000, 26);
                leftBracket.setLayoutData(leftBracketLData);
                leftBracket.setText("(");
            }
            {
                nine = new Text(this, SWT.NONE);
                FormData nineLData = new FormData();
                nineLData.width = 35;
                nineLData.height = 17;
                nineLData.left = new FormAttachment(0, 1000, 464);
                nineLData.top = new FormAttachment(0, 1000, 58);
                nine.setLayoutData(nineLData);
                nine.setText("9");
            }
            {
                asteriks = new Text(this, SWT.NONE);
                FormData asteriksLData = new FormData();
                asteriksLData.width = 35;
                asteriksLData.height = 17;
                asteriksLData.left = new FormAttachment(0, 1000, 413);
                asteriksLData.top = new FormAttachment(0, 1000, 26);
                asteriks.setLayoutData(asteriksLData);
                asteriks.setText("*");
            }
            {
                eight = new Text(this, SWT.NONE);
                FormData eightLData = new FormData();
                eightLData.width = 35;
                eightLData.height = 17;
                eightLData.left = new FormAttachment(0, 1000, 413);
                eightLData.top = new FormAttachment(0, 1000, 58);
                eight.setLayoutData(eightLData);
                eight.setText("8");
            }
            {
                ampersand = new Text(this, SWT.NONE);
                FormData ampersandLData = new FormData();
                ampersandLData.width = 35;
                ampersandLData.height = 17;
                ampersandLData.left = new FormAttachment(0, 1000, 362);
                ampersandLData.top = new FormAttachment(0, 1000, 26);
                ampersand.setLayoutData(ampersandLData);
                ampersand.setText("&");
            }

            {
                seven = new Text(this, SWT.NONE);
                FormData sevenLData = new FormData();
                sevenLData.width = 35;
                sevenLData.height = 17;
                sevenLData.left = new FormAttachment(0, 1000, 362);
                sevenLData.top = new FormAttachment(0, 1000, 58);
                seven.setLayoutData(sevenLData);
                seven.setText("7");
            }
            {
                cap = new Text(this, SWT.NONE);
                FormData capLData = new FormData();
                capLData.width = 35;
                capLData.height = 17;
                capLData.left = new FormAttachment(0, 1000, 311);
                capLData.top = new FormAttachment(0, 1000, 26);
                cap.setLayoutData(capLData);
                cap.setText("^");
            }
            {
                six = new Text(this, SWT.NONE);
                FormData sixLData = new FormData();
                sixLData.width = 35;
                sixLData.height = 17;
                sixLData.left = new FormAttachment(0, 1000, 311);
                sixLData.top = new FormAttachment(0, 1000, 58);
                six.setLayoutData(sixLData);
                six.setText("6");
            }
            {
                percent = new Text(this, SWT.NONE);
                FormData percentLData = new FormData();
                percentLData.width = 35;
                percentLData.height = 17;
                percentLData.left = new FormAttachment(0, 1000, 260);
                percentLData.top = new FormAttachment(0, 1000, 26);
                percent.setLayoutData(percentLData);
                percent.setText("%");
            }
            {
                five = new Text(this, SWT.NONE);
                FormData fiveLData = new FormData();
                fiveLData.width = 35;
                fiveLData.height = 17;
                fiveLData.left = new FormAttachment(0, 1000, 260);
                fiveLData.top = new FormAttachment(0, 1000, 58);
                five.setLayoutData(fiveLData);
                five.setText("5");
            }
            {
                dollar = new Text(this, SWT.NONE);
                FormData dollarLData = new FormData();
                dollarLData.width = 35;
                dollarLData.height = 17;
                dollarLData.left = new FormAttachment(0, 1000, 209);
                dollarLData.top = new FormAttachment(0, 1000, 26);
                dollar.setLayoutData(dollarLData);
                dollar.setText("$");
            }
            {
                four = new Text(this, SWT.NONE);
                FormData fourLData = new FormData();
                fourLData.width = 35;
                fourLData.height = 17;
                fourLData.left = new FormAttachment(0, 1000, 209);
                fourLData.top = new FormAttachment(0, 1000, 58);
                four.setLayoutData(fourLData);
                four.setText("4");
            }
            {
                hash = new Text(this, SWT.NONE);
                FormData hashLData = new FormData();
                hashLData.width = 35;
                hashLData.height = 17;
                hashLData.left = new FormAttachment(0, 1000, 158);
                hashLData.top = new FormAttachment(0, 1000, 26);
                hash.setLayoutData(hashLData);
                hash.setText("#");
            }
            {
                three = new Text(this, SWT.NONE);
                FormData threeLData = new FormData();
                threeLData.width = 35;
                threeLData.height = 17;
                threeLData.left = new FormAttachment(0, 1000, 158);
                threeLData.top = new FormAttachment(0, 1000, 58);
                three.setLayoutData(threeLData);
                three.setText("3");
            }
            {
                AT = new Text(this, SWT.NONE);
                FormData ATLData = new FormData();
                ATLData.width = 35;
                ATLData.height = 17;
                ATLData.left = new FormAttachment(0, 1000, 107);
                ATLData.top = new FormAttachment(0, 1000, 26);
                AT.setLayoutData(ATLData);
                AT.setText("@");
            }
            {
                two = new Text(this, SWT.NONE);
                FormData twoLData = new FormData();
                twoLData.width = 35;
                twoLData.height = 17;
                twoLData.left = new FormAttachment(0, 1000, 107);
                twoLData.top = new FormAttachment(0, 1000, 58);
                two.setLayoutData(twoLData);
                two.setText("2");
            }
            {
                exclaim = new Text(this, SWT.NONE);
                FormData exclaimLData = new FormData();
                exclaimLData.width = 35;
                exclaimLData.height = 17;
                exclaimLData.left = new FormAttachment(0, 1000, 56);
                exclaimLData.top = new FormAttachment(0, 1000, 26);
                exclaim.setLayoutData(exclaimLData);
                exclaim.setText("!");
            }
            {
                one = new Text(this, SWT.NONE);
                FormData oneLData = new FormData();
                oneLData.width = 35;
                oneLData.height = 17;
                oneLData.left = new FormAttachment(0, 1000, 56);
                oneLData.top = new FormAttachment(0, 1000, 58);
                one.setLayoutData(oneLData);
                one.setText("1");
            }
            {
                tilde = new Text(this, SWT.NONE);
                FormData tildeLData = new FormData();
                tildeLData.width = 35;
                tildeLData.height = 17;
                tildeLData.left = new FormAttachment(0, 1000, 5);
                tildeLData.top = new FormAttachment(0, 1000, 26);
                tilde.setLayoutData(tildeLData);
                tilde.setText("~");
            }
            {
                backTick = new Text(this, SWT.NONE);
                FormData backTickLData = new FormData();
                backTickLData.width = 35;
                backTickLData.height = 17;
                backTickLData.left = new FormAttachment(0, 1000, 5);
                backTickLData.top = new FormAttachment(0, 1000, 58);
                backTick.setLayoutData(backTickLData);
                backTick.setText("`");
            }
            {
                questionMark = new Text(this, SWT.NONE);
                FormData questionMarkLData = new FormData();
                questionMarkLData.width = 35;
                questionMarkLData.height = 17;
                questionMarkLData.left = new FormAttachment(0, 1000, 524);
                questionMarkLData.top = new FormAttachment(0, 1000, 222);
                questionMark.setLayoutData(questionMarkLData);
                questionMark.setText("?");
            }
            {
                frontSlash = new Text(this, SWT.NONE);
                FormData frontSlashLData = new FormData();
                frontSlashLData.width = 35;
                frontSlashLData.height = 17;
                frontSlashLData.left = new FormAttachment(0, 1000, 524);
                frontSlashLData.top = new FormAttachment(0, 1000, 255);
                frontSlash.setLayoutData(frontSlashLData);
                frontSlash.setText("/");
            }
            {
                rightAngularBracket = new Text(this, SWT.NONE);
                FormData rightAngularBracketLData = new FormData();
                rightAngularBracketLData.width = 35;
                rightAngularBracketLData.height = 17;
                rightAngularBracketLData.left = new FormAttachment(0, 1000, 473);
                rightAngularBracketLData.top = new FormAttachment(0, 1000, 222);
                rightAngularBracket.setLayoutData(rightAngularBracketLData);
                rightAngularBracket.setText(">");
            }
            {
                fullStop = new Text(this, SWT.NONE);
                FormData fullStopLData = new FormData();
                fullStopLData.width = 35;
                fullStopLData.height = 17;
                fullStopLData.left = new FormAttachment(0, 1000, 473);
                fullStopLData.top = new FormAttachment(0, 1000, 255);
                fullStop.setLayoutData(fullStopLData);
                fullStop.setText(".");
            }
            {
                leftAngularBracket = new Text(this, SWT.NONE);
                FormData leftAngularBracketLData = new FormData();
                leftAngularBracketLData.width = 35;
                leftAngularBracketLData.height = 17;
                leftAngularBracketLData.left = new FormAttachment(0, 1000, 422);
                leftAngularBracketLData.top = new FormAttachment(0, 1000, 222);
                leftAngularBracket.setLayoutData(leftAngularBracketLData);
                leftAngularBracket.setText("<");
            }
            {
                comma = new Text(this, SWT.NONE);
                FormData commaLData = new FormData();
                commaLData.width = 35;
                commaLData.height = 17;
                commaLData.left = new FormAttachment(0, 1000, 422);
                commaLData.top = new FormAttachment(0, 1000, 255);
                comma.setLayoutData(commaLData);
                comma.setText(",");
            }
            {
                M = new Text(this, SWT.NONE);
                FormData MLData = new FormData();
                MLData.width = 35;
                MLData.height = 17;
                MLData.left = new FormAttachment(0, 1000, 371);
                MLData.top = new FormAttachment(0, 1000, 222);
                M.setLayoutData(MLData);
                M.setText("M");
            }
            {
                m = new Text(this, SWT.NONE);
                FormData mLData = new FormData();
                mLData.width = 35;
                mLData.height = 17;
                mLData.left = new FormAttachment(0, 1000, 371);
                mLData.top = new FormAttachment(0, 1000, 255);
                m.setLayoutData(mLData);
                m.setText("m");
            }
            {
                N = new Text(this, SWT.NONE);
                FormData NLData = new FormData();
                NLData.width = 35;
                NLData.height = 17;
                NLData.left = new FormAttachment(0, 1000, 320);
                NLData.top = new FormAttachment(0, 1000, 222);
                N.setLayoutData(NLData);
                N.setText("N");
            }
            {
                n = new Text(this, SWT.NONE);
                FormData nLData = new FormData();
                nLData.width = 35;
                nLData.height = 17;
                nLData.left = new FormAttachment(0, 1000, 320);
                nLData.top = new FormAttachment(0, 1000, 255);
                n.setLayoutData(nLData);
                n.setText("n");
            }
            {
                B = new Text(this, SWT.NONE);
                FormData BLData = new FormData();
                BLData.width = 35;
                BLData.height = 17;
                BLData.left = new FormAttachment(0, 1000, 269);
                BLData.top = new FormAttachment(0, 1000, 222);
                B.setLayoutData(BLData);
                B.setText("B");
            }
            {
                b = new Text(this, SWT.NONE);
                FormData bLData = new FormData();
                bLData.width = 35;
                bLData.height = 17;
                bLData.left = new FormAttachment(0, 1000, 269);
                bLData.top = new FormAttachment(0, 1000, 255);
                b.setLayoutData(bLData);
                b.setText("b");
            }
            {
                V = new Text(this, SWT.NONE);
                FormData VLData = new FormData();
                VLData.width = 35;
                VLData.height = 17;
                VLData.left = new FormAttachment(0, 1000, 218);
                VLData.top = new FormAttachment(0, 1000, 222);
                V.setLayoutData(VLData);
                V.setText("V");
            }
            {
                v = new Text(this, SWT.NONE);
                FormData vLData = new FormData();
                vLData.width = 35;
                vLData.height = 17;
                vLData.left = new FormAttachment(0, 1000, 218);
                vLData.top = new FormAttachment(0, 1000, 255);
                v.setLayoutData(vLData);
                v.setText("v");
            }
            {
                C = new Text(this, SWT.NONE);
                FormData CLData = new FormData();
                CLData.width = 35;
                CLData.height = 17;
                CLData.left = new FormAttachment(0, 1000, 167);
                CLData.top = new FormAttachment(0, 1000, 222);
                C.setLayoutData(CLData);
                C.setText("C");
            }
            {
                c = new Text(this, SWT.NONE);
                FormData cLData = new FormData();
                cLData.width = 35;
                cLData.height = 17;
                cLData.left = new FormAttachment(0, 1000, 167);
                cLData.top = new FormAttachment(0, 1000, 255);
                c.setLayoutData(cLData);
                c.setText("c");
            }
            {
                x = new Text(this, SWT.NONE);
                FormData xLData = new FormData();
                xLData.width = 35;
                xLData.height = 17;
                xLData.left = new FormAttachment(0, 1000, 116);
                xLData.top = new FormAttachment(0, 1000, 255);
                x.setLayoutData(xLData);
                x.setText("x");
            }
            {
                X = new Text(this, SWT.NONE);
                FormData XLData = new FormData();
                XLData.width = 35;
                XLData.height = 17;
                XLData.left = new FormAttachment(0, 1000, 116);
                XLData.top = new FormAttachment(0, 1000, 222);
                X.setLayoutData(XLData);
                X.setText("X");
            }
            {
                z = new Text(this, SWT.NONE);
                FormData zLData = new FormData();
                zLData.width = 35;
                zLData.height = 17;
                zLData.left = new FormAttachment(0, 1000, 65);
                zLData.top = new FormAttachment(0, 1000, 255);
                z.setLayoutData(zLData);
                z.setText("z");
            }
            {
                Z = new Text(this, SWT.NONE);
                FormData ZLData = new FormData();
                ZLData.width = 35;
                ZLData.height = 17;
                ZLData.left = new FormAttachment(0, 1000, 65);
                ZLData.top = new FormAttachment(0, 1000, 222);
                Z.setLayoutData(ZLData);
                Z.setText("Z");
            }
            {
                doubleQuote = new Text(this, SWT.NONE);
                FormData doubleQuoteLData = new FormData();
                doubleQuoteLData.width = 35;
                doubleQuoteLData.height = 17;
                doubleQuoteLData.left = new FormAttachment(0, 1000, 563);
                doubleQuoteLData.top = new FormAttachment(0, 1000, 156);
                doubleQuote.setLayoutData(doubleQuoteLData);
                doubleQuote.setText("\"");
            }
            {
                singleQuote = new Text(this, SWT.NONE);
                FormData singleQuoteLData = new FormData();
                singleQuoteLData.width = 35;
                singleQuoteLData.height = 17;
                singleQuoteLData.left = new FormAttachment(0, 1000, 563);
                singleQuoteLData.top = new FormAttachment(0, 1000, 185);
                singleQuote.setLayoutData(singleQuoteLData);
                singleQuote.setText("'");
            }
            {
                colon = new Text(this, SWT.NONE);
                FormData colonLData = new FormData();
                colonLData.width = 35;
                colonLData.height = 17;
                colonLData.left = new FormAttachment(0, 1000, 512);
                colonLData.top = new FormAttachment(0, 1000, 156);
                colon.setLayoutData(colonLData);
                colon.setText(":");
            }
            {
                semiColon = new Text(this, SWT.NONE);
                FormData semiColonLData = new FormData();
                semiColonLData.width = 35;
                semiColonLData.height = 17;
                semiColonLData.left = new FormAttachment(0, 1000, 512);
                semiColonLData.top = new FormAttachment(0, 1000, 185);
                semiColon.setLayoutData(semiColonLData);
                semiColon.setText(";");
            }
            {
                L = new Text(this, SWT.NONE);
                FormData LLData = new FormData();
                LLData.width = 35;
                LLData.height = 17;
                LLData.left = new FormAttachment(0, 1000, 461);
                LLData.top = new FormAttachment(0, 1000, 156);
                L.setLayoutData(LLData);
                L.setText("L");
            }
            {
                l = new Text(this, SWT.NONE);
                FormData lLData = new FormData();
                lLData.width = 35;
                lLData.height = 17;
                lLData.left = new FormAttachment(0, 1000, 461);
                lLData.top = new FormAttachment(0, 1000, 189);
                l.setLayoutData(lLData);
                l.setText("l");
            }
            {
                K = new Text(this, SWT.NONE);
                FormData KLData = new FormData();
                KLData.width = 35;
                KLData.height = 17;
                KLData.left = new FormAttachment(0, 1000, 410);
                KLData.top = new FormAttachment(0, 1000, 156);
                K.setLayoutData(KLData);
                K.setText("K");
            }
            {
                k = new Text(this, SWT.NONE);
                FormData kLData = new FormData();
                kLData.width = 35;
                kLData.height = 17;
                kLData.left = new FormAttachment(0, 1000, 410);
                kLData.top = new FormAttachment(0, 1000, 189);
                k.setLayoutData(kLData);
                k.setText("k");
            }
            {
                J = new Text(this, SWT.NONE);
                FormData JLData = new FormData();
                JLData.width = 35;
                JLData.height = 17;
                JLData.left = new FormAttachment(0, 1000, 359);
                JLData.top = new FormAttachment(0, 1000, 156);
                J.setLayoutData(JLData);
                J.setText("J");
            }
            {
                j = new Text(this, SWT.NONE);
                FormData jLData = new FormData();
                jLData.width = 35;
                jLData.height = 17;
                jLData.left = new FormAttachment(0, 1000, 359);
                jLData.top = new FormAttachment(0, 1000, 189);
                j.setLayoutData(jLData);
                j.setText("j");
            }
            {
                H = new Text(this, SWT.NONE);
                FormData HLData = new FormData();
                HLData.width = 35;
                HLData.height = 17;
                HLData.left = new FormAttachment(0, 1000, 308);
                HLData.top = new FormAttachment(0, 1000, 156);
                H.setLayoutData(HLData);
                H.setText("H");
            }
            {
                h = new Text(this, SWT.NONE);
                FormData hLData = new FormData();
                hLData.width = 35;
                hLData.height = 17;
                hLData.left = new FormAttachment(0, 1000, 308);
                hLData.top = new FormAttachment(0, 1000, 189);
                h.setLayoutData(hLData);
                h.setText("h");
            }
            {
                G = new Text(this, SWT.NONE);
                FormData GLData = new FormData();
                GLData.width = 35;
                GLData.height = 17;
                GLData.left = new FormAttachment(0, 1000, 257);
                GLData.top = new FormAttachment(0, 1000, 156);
                G.setLayoutData(GLData);
                G.setText("G");
            }
            {
                g = new Text(this, SWT.NONE);
                FormData gLData = new FormData();
                gLData.width = 35;
                gLData.height = 17;
                gLData.left = new FormAttachment(0, 1000, 257);
                gLData.top = new FormAttachment(0, 1000, 189);
                g.setLayoutData(gLData);
                g.setText("g");
            }
            {
                F = new Text(this, SWT.NONE);
                FormData FLData = new FormData();
                FLData.width = 35;
                FLData.height = 17;
                FLData.left = new FormAttachment(0, 1000, 204);
                FLData.top = new FormAttachment(0, 1000, 156);
                F.setLayoutData(FLData);
                F.setText("F");
            }
            {
                f = new Text(this, SWT.NONE);
                FormData fLData = new FormData();
                fLData.width = 35;
                fLData.height = 17;
                fLData.left = new FormAttachment(0, 1000, 206);
                fLData.top = new FormAttachment(0, 1000, 189);
                f.setLayoutData(fLData);
                f.setText("f");
            }
            {
                D = new Text(this, SWT.NONE);
                FormData DLData = new FormData();
                DLData.width = 35;
                DLData.height = 17;
                DLData.left = new FormAttachment(0, 1000, 153);
                DLData.top = new FormAttachment(0, 1000, 156);
                D.setLayoutData(DLData);
                D.setText("D");
            }
            {
                d = new Text(this, SWT.NONE);
                FormData dLData = new FormData();
                dLData.width = 35;
                dLData.height = 17;
                dLData.left = new FormAttachment(0, 1000, 155);
                dLData.top = new FormAttachment(0, 1000, 189);
                d.setLayoutData(dLData);
                d.setText("d");
            }
            {
                S = new Text(this, SWT.NONE);
                FormData SLData = new FormData();
                SLData.width = 35;
                SLData.height = 17;
                SLData.left = new FormAttachment(0, 1000, 102);
                SLData.top = new FormAttachment(0, 1000, 156);
                S.setLayoutData(SLData);
                S.setText("S");
            }
            {
                s = new Text(this, SWT.NONE);
                FormData sLData = new FormData();
                sLData.width = 35;
                sLData.height = 17;
                sLData.left = new FormAttachment(0, 1000, 104);
                sLData.top = new FormAttachment(0, 1000, 189);
                s.setLayoutData(sLData);
                s.setText("s");
            }
            {
                a = new Text(this, SWT.NONE);
                FormData aLData = new FormData();
                aLData.width = 35;
                aLData.height = 17;
                aLData.left = new FormAttachment(0, 1000, 53);
                aLData.top = new FormAttachment(0, 1000, 189);
                a.setLayoutData(aLData);
                a.setText("a");
            }
            {
                A = new Text(this, SWT.NONE);
                FormData ALData = new FormData();
                ALData.width = 35;
                ALData.height = 17;
                ALData.left = new FormAttachment(0, 1000, 51);
                ALData.top = new FormAttachment(0, 1000, 156);
                A.setLayoutData(ALData);
                A.setText("A");
            }
            {
                rightBrace = new Text(this, SWT.NONE);
                FormData rightBraceLData = new FormData();
                rightBraceLData.width = 35;
                rightBraceLData.height = 17;
                rightBraceLData.left = new FormAttachment(0, 1000, 602);
                rightBraceLData.top = new FormAttachment(0, 1000, 90);
                rightBrace.setLayoutData(rightBraceLData);
                rightBrace.setText("}");
            }
            {
                rightArrayBracket = new Text(this, SWT.NONE);
                FormData rightArrayBracketLData = new FormData();
                rightArrayBracketLData.width = 35;
                rightArrayBracketLData.height = 17;
                rightArrayBracketLData.left = new FormAttachment(0, 1000, 602);
                rightArrayBracketLData.top = new FormAttachment(0, 1000, 120);
                rightArrayBracket.setLayoutData(rightArrayBracketLData);
                rightArrayBracket.setText("]");
            }
            {
                leftBrace = new Text(this, SWT.NONE);
                FormData leftBraceLData = new FormData();
                leftBraceLData.width = 35;
                leftBraceLData.height = 17;
                leftBraceLData.left = new FormAttachment(0, 1000, 551);
                leftBraceLData.top = new FormAttachment(0, 1000, 90);
                leftBrace.setLayoutData(leftBraceLData);
                leftBrace.setText("{");
            }
            {
                leftArrayBracket = new Text(this, SWT.NONE);
                FormData leftArrayBracketLData = new FormData();
                leftArrayBracketLData.width = 35;
                leftArrayBracketLData.height = 17;
                leftArrayBracketLData.left = new FormAttachment(0, 1000, 551);
                leftArrayBracketLData.top = new FormAttachment(0, 1000, 123);
                leftArrayBracket.setLayoutData(leftArrayBracketLData);
                leftArrayBracket.setText("[");
            }
            {
                P = new Text(this, SWT.NONE);
                FormData PLData = new FormData();
                PLData.width = 35;
                PLData.height = 17;
                PLData.left = new FormAttachment(0, 1000, 500);
                PLData.top = new FormAttachment(0, 1000, 90);
                P.setLayoutData(PLData);
                P.setText("P");
            }
            {
                p = new Text(this, SWT.NONE);
                FormData pLData = new FormData();
                pLData.width = 35;
                pLData.height = 17;
                pLData.left = new FormAttachment(0, 1000, 500);
                pLData.top = new FormAttachment(0, 1000, 123);
                p.setLayoutData(pLData);
                p.setText("p");
            }
            {
                O = new Text(this, SWT.NONE);
                FormData OLData = new FormData();
                OLData.width = 35;
                OLData.height = 17;
                OLData.left = new FormAttachment(0, 1000, 449);
                OLData.top = new FormAttachment(0, 1000, 90);
                O.setLayoutData(OLData);
                O.setText("O");
            }
            {
                o = new Text(this, SWT.NONE);
                FormData oLData = new FormData();
                oLData.width = 35;
                oLData.height = 17;
                oLData.left = new FormAttachment(0, 1000, 449);
                oLData.top = new FormAttachment(0, 1000, 123);
                o.setLayoutData(oLData);
                o.setText("o");
            }
            {
                I = new Text(this, SWT.NONE);
                FormData ILData = new FormData();
                ILData.width = 35;
                ILData.height = 17;
                ILData.left = new FormAttachment(0, 1000, 398);
                ILData.top = new FormAttachment(0, 1000, 90);
                I.setLayoutData(ILData);
                I.setText("I");
            }
            {
                i = new Text(this, SWT.NONE);
                FormData iLData = new FormData();
                iLData.width = 35;
                iLData.height = 17;
                iLData.left = new FormAttachment(0, 1000, 398);
                iLData.top = new FormAttachment(0, 1000, 123);
                i.setLayoutData(iLData);
                i.setText("i");
            }
            {
                U = new Text(this, SWT.NONE);
                FormData ULData = new FormData();
                ULData.width = 35;
                ULData.height = 17;
                ULData.left = new FormAttachment(0, 1000, 347);
                ULData.top = new FormAttachment(0, 1000, 90);
                U.setLayoutData(ULData);
                U.setText("U");
            }
            {
                u = new Text(this, SWT.NONE);
                FormData uLData = new FormData();
                uLData.width = 35;
                uLData.height = 17;
                uLData.left = new FormAttachment(0, 1000, 347);
                uLData.top = new FormAttachment(0, 1000, 123);
                u.setLayoutData(uLData);
                u.setText("u");
            }
            {
                Y = new Text(this, SWT.NONE);
                FormData YLData = new FormData();
                YLData.width = 35;
                YLData.height = 17;
                YLData.left = new FormAttachment(0, 1000, 296);
                YLData.top = new FormAttachment(0, 1000, 90);
                Y.setLayoutData(YLData);
                Y.setText("Y");
            }
            {
                y = new Text(this, SWT.NONE);
                FormData yLData = new FormData();
                yLData.width = 35;
                yLData.height = 17;
                yLData.left = new FormAttachment(0, 1000, 296);
                yLData.top = new FormAttachment(0, 1000, 123);
                y.setLayoutData(yLData);
                y.setText("y");
            }
            {
                T = new Text(this, SWT.NONE);
                FormData TLData = new FormData();
                TLData.width = 35;
                TLData.height = 17;
                TLData.left = new FormAttachment(0, 1000, 245);
                TLData.top = new FormAttachment(0, 1000, 90);
                T.setLayoutData(TLData);
                T.setText("T");
            }
            {
                t = new Text(this, SWT.NONE);
                FormData tLData = new FormData();
                tLData.width = 35;
                tLData.height = 17;
                tLData.left = new FormAttachment(0, 1000, 245);
                tLData.top = new FormAttachment(0, 1000, 123);
                t.setLayoutData(tLData);
                t.setText("t");
            }
            {
                R = new Text(this, SWT.NONE);
                FormData RLData = new FormData();
                RLData.width = 35;
                RLData.height = 17;
                RLData.left = new FormAttachment(0, 1000, 194);
                RLData.top = new FormAttachment(0, 1000, 90);
                R.setLayoutData(RLData);
                R.setText("R");
            }
            {
                r = new Text(this, SWT.NONE);
                FormData rLData = new FormData();
                rLData.width = 35;
                rLData.height = 17;
                rLData.left = new FormAttachment(0, 1000, 194);
                rLData.top = new FormAttachment(0, 1000, 123);
                r.setLayoutData(rLData);
                r.setText("r");
            }
            {
                E = new Text(this, SWT.NONE);
                FormData ELData = new FormData();
                ELData.width = 35;
                ELData.height = 17;
                ELData.left = new FormAttachment(0, 1000, 143);
                ELData.top = new FormAttachment(0, 1000, 90);
                E.setLayoutData(ELData);
                E.setText("E");
            }
            {
                e = new Text(this, SWT.NONE);
                FormData eLData = new FormData();
                eLData.width = 35;
                eLData.height = 17;
                eLData.left = new FormAttachment(0, 1000, 143);
                eLData.top = new FormAttachment(0, 1000, 123);
                e.setLayoutData(eLData);
                e.setText("e");
            }
            {
                W = new Text(this, SWT.NONE);
                FormData WLData = new FormData();
                WLData.width = 35;
                WLData.height = 17;
                WLData.left = new FormAttachment(0, 1000, 92);
                WLData.top = new FormAttachment(0, 1000, 90);
                W.setLayoutData(WLData);
                W.setText("W");
            }
            {
                w = new Text(this, SWT.NONE);
                FormData wLData = new FormData();
                wLData.width = 35;
                wLData.height = 17;
                wLData.left = new FormAttachment(0, 1000, 92);
                wLData.top = new FormAttachment(0, 1000, 123);
                w.setLayoutData(wLData);
                w.setText("w");
            }
            {
                Q = new Text(this, SWT.NONE);
                FormData QLData = new FormData();
                QLData.width = 35;
                QLData.height = 17;
                QLData.left = new FormAttachment(0, 1000, 41);
                QLData.top = new FormAttachment(0, 1000, 90);
                Q.setLayoutData(QLData);
                Q.setText("Q");
            }
            {
                q = new Text(this, SWT.NONE);
                FormData qLData = new FormData();
                qLData.width = 35;
                qLData.height = 17;
                qLData.left = new FormAttachment(0, 1000, 41);
                qLData.top = new FormAttachment(0, 1000, 123);
                q.setLayoutData(qLData);
                q.setText("q");
            }
            this.layout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Auto-generated method to display this
     * org.eclipse.swt.widgets.Composite inside a new Shell.
     */
    public static void GenerateXMLUI() {
        Display display = Display.getDefault();
        Shell shell = new Shell(display, SWT.DIALOG_TRIM);
        XMLGenerator inst = new XMLGenerator(shell, SWT.NULL);
        Image image = new Image(display, IndicKeyboards.class.getResourceAsStream("trayicon.ico"));
        Point size = inst.getSize();
        shell.setImage(image);
        shell.setText("XML Generator");
        shell.setToolTipText("Generates the XML file holding Unicodes");
        shell.setLayout(new FillLayout());
        shell.layout();
        if (size.x == 0 && size.y == 0) {
            inst.pack();
            shell.pack();
        } else {
            Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
            shell.setSize(shellBounds.width, shellBounds.height);
        }
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
            
            
        }
    }

    private void buttonOKMouseDown(MouseEvent evt) {
        Shell shell = new Shell(Display.getCurrent());
        shell.setFocus();
        //shell.setImage(image);
        FileDialog dialog = new FileDialog(shell,
                SWT.SAVE);

        dialog.setFilterNames(new String[]{
                    "XML Documents"});
        dialog.setFilterExtensions(new String[]{
                    "*.xml"}); // Wild
        // cards
        dialog.setFileName(".xml");
        dialog.setText("Location to save the file");
        String path = dialog.open();

        FileOutputStream out;
        try {
            out = new FileOutputStream(path);

            PrintStream newXML; // declare a print stream
            // object
            newXML = new PrintStream(out);
            // Connect print stream to the output
            // stream

            /*
             * now the convert method will simply
             * convert and the o/p is put to file
             * only after the user presses ok button
             */
            //Write header


            //Start of the XML Data

            String temp;
            int length = dialog.getFileName().length();
            temp = dialog.getFileName().substring(0, length - 4);

            newXML.println("<" + temp + ">\n");

            /*The row from A to L starts here*/

            if (a.getText().compareTo("a") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "a" + "</char>");
                newXML.println("<unicode>" + a.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (A.getText().compareTo("A") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "A" + "</char>");
                newXML.println("<unicode>" + A.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (s.getText().compareTo("s") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "s" + "</char>");
                newXML.println("<unicode>" + s.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (S.getText().compareTo("S") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "S" + "</char>");
                newXML.println("<unicode>" + S.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (d.getText().compareTo("d") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "d" + "</char>");
                newXML.println("<unicode>" + d.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (D.getText().compareTo("D") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "D" + "</char>");
                newXML.println("<unicode>" + D.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (f.getText().compareTo("f") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "f" + "</char>");
                newXML.println("<unicode>" + f.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (F.getText().compareTo("F") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "F" + "</char>");
                newXML.println("<unicode>" + F.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (g.getText().compareTo("g") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "g" + "</char>");
                newXML.println("<unicode>" + g.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (G.getText().compareTo("G") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "G" + "</char>");
                newXML.println("<unicode>" + G.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (h.getText().compareTo("h") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "h" + "</char>");
                newXML.println("<unicode>" + h.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (H.getText().compareTo("H") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "H" + "</char>");
                newXML.println("<unicode>" + H.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (j.getText().compareTo("j") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "j" + "</char>");
                newXML.println("<unicode>" + j.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (J.getText().compareTo("J") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "J" + "</char>");
                newXML.println("<unicode>" + J.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (k.getText().compareTo("k") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "k" + "</char>");
                newXML.println("<unicode>" + k.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (K.getText().compareTo("K") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "K" + "</char>");
                newXML.println("<unicode>" + K.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (l.getText().compareTo("l") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "l" + "</char>");
                newXML.println("<unicode>" + l.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (L.getText().compareTo("L") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "L" + "</char>");
                newXML.println("<unicode>" + L.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            /*The row from A to L ends here*/

            /*The row from Q to P starts here*/

            if (q.getText().compareTo("q") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "q" + "</char>");
                newXML.println("<unicode>" + q.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (Q.getText().compareTo("Q") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "Q" + "</char>");
                newXML.println("<unicode>" + Q.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (w.getText().compareTo("w") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "w" + "</char>");
                newXML.println("<unicode>" + w.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (W.getText().compareTo("W") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "W" + "</char>");
                newXML.println("<unicode>" + W.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (e.getText().compareTo("e") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "e" + "</char>");
                newXML.println("<unicode>" + e.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (E.getText().compareTo("E") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "E" + "</char>");
                newXML.println("<unicode>" + E.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (r.getText().compareTo("r") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "r" + "</char>");
                newXML.println("<unicode>" + r.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (R.getText().compareTo("R") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "R" + "</char>");
                newXML.println("<unicode>" + R.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (t.getText().compareTo("t") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "t" + "</char>");
                newXML.println("<unicode>" + t.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (T.getText().compareTo("T") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "T" + "</char>");
                newXML.println("<unicode>" + T.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (y.getText().compareTo("y") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "y" + "</char>");
                newXML.println("<unicode>" + y.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (Y.getText().compareTo("Y") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "Y" + "</char>");
                newXML.println("<unicode>" + Y.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (u.getText().compareTo("u") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "u" + "</char>");
                newXML.println("<unicode>" + u.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (U.getText().compareTo("U") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "U" + "</char>");
                newXML.println("<unicode>" + U.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (i.getText().compareTo("i") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "i" + "</char>");
                newXML.println("<unicode>" + i.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (I.getText().compareTo("I") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "I" + "</char>");
                newXML.println("<unicode>" + I.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (o.getText().compareTo("o") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "o" + "</char>");
                newXML.println("<unicode>" + o.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (O.getText().compareTo("O") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "O" + "</char>");
                newXML.println("<unicode>" + O.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (p.getText().compareTo("p") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "p" + "</char>");
                newXML.println("<unicode>" + p.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (P.getText().compareTo("P") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "P" + "</char>");
                newXML.println("<unicode>" + P.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            /*The row from Q to P ends here*/

            /*The row from Z to M start here*/


            if (z.getText().compareTo("z") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "z" + "</char>");
                newXML.println("<unicode>" + z.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (Z.getText().compareTo("Z") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "Z" + "</char>");
                newXML.println("<unicode>" + Z.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (x.getText().compareTo("x") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "x" + "</char>");
                newXML.println("<unicode>" + x.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (X.getText().compareTo("X") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "X" + "</char>");
                newXML.println("<unicode>" + X.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (c.getText().compareTo("c") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "c" + "</char>");
                newXML.println("<unicode>" + c.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (C.getText().compareTo("C") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "C" + "</char>");
                newXML.println("<unicode>" + C.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (v.getText().compareTo("v") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "v" + "</char>");
                newXML.println("<unicode>" + v.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (V.getText().compareTo("V") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "V" + "</char>");
                newXML.println("<unicode>" + V.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (b.getText().compareTo("b") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "b" + "</char>");
                newXML.println("<unicode>" + B.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (n.getText().compareTo("n") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "n" + "</char>");
                newXML.println("<unicode>" + n.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (N.getText().compareTo("N") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "N" + "</char>");
                newXML.println("<unicode>" + N.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (m.getText().compareTo("m") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "m" + "</char>");
                newXML.println("<unicode>" + m.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (M.getText().compareTo("M") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "M" + "</char>");
                newXML.println("<unicode>" + M.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            /*The row from Z to M ends here*/

            /*Numbers 0 to 9 starts here*/
            if (zero.getText().compareTo("0") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "0" + "</char>");
                newXML.println("<unicode>" + zero.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (exclaim.getText().compareTo("!") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "!" + "</char>");
                newXML.println("<unicode>" + exclaim.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (two.getText().compareTo("2") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "2" + "</char>");
                newXML.println("<unicode>" + two.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (AT.getText().compareTo("@") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "@" + "</char>");
                newXML.println("<unicode>" + AT.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (three.getText().compareTo("3") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "3" + "</char>");
                newXML.println("<unicode>" + three.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (hash.getText().compareTo("#") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "#" + "</char>");
                newXML.println("<unicode>" + hash.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (four.getText().compareTo("4") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "4" + "</char>");
                newXML.println("<unicode>" + four.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (dollar.getText().compareTo("$") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "$" + "</char>");
                newXML.println("<unicode>" + dollar.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (five.getText().compareTo("5") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "5" + "</char>");
                newXML.println("<unicode>" + five.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (percent.getText().compareTo("%") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "%" + "</char>");
                newXML.println("<unicode>" + percent.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (six.getText().compareTo("6") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "6" + "</char>");
                newXML.println("<unicode>" + six.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (cap.getText().compareTo("^") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "^" + "</char>");
                newXML.println("<unicode>" + cap.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (seven.getText().compareTo("7") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "7" + "</char>");
                newXML.println("<unicode>" + seven.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (ampersand.getText().compareTo("&") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "&" + "</char>");
                newXML.println("<unicode>" + ampersand.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (eight.getText().compareTo("8") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "8" + "</char>");
                newXML.println("<unicode>" + eight.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (asteriks.getText().compareTo("*") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "*" + "</char>");
                newXML.println("<unicode>" + asteriks.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (nine.getText().compareTo("9") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "9" + "</char>");
                newXML.println("<unicode>" + nine.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (leftBracket.getText().compareTo("(") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "(" + "</char>");
                newXML.println("<unicode>" + leftBracket.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (zero.getText().compareTo("0") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "0" + "</char>");
                newXML.println("<unicode>" + zero.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (rightBracket.getText().compareTo(")") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + ")" + "</char>");
                newXML.println("<unicode>" + rightBracket.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            /*Numbers 0 to 9 end here*/

            /*Special characters start here*/

            if (hyphen.getText().compareTo("-") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "-" + "</char>");
                newXML.println("<unicode>" + hyphen.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (underScore.getText().compareTo("_") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "_" + "</char>");
                newXML.println("<unicode>" + underScore.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (equals.getText().compareTo("=") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "=" + "</char>");
                newXML.println("<unicode>" + equals.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (backSlash.getText().compareTo("\\") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "\\" + "</char>");
                newXML.println("<unicode>" + backSlash.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (pipe.getText().compareTo("|") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "|" + "</char>");
                newXML.println("<unicode>" + pipe.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (leftArrayBracket.getText().compareTo("[") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "[" + "</char>");
                newXML.println("<unicode>" + leftArrayBracket.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (rightArrayBracket.getText().compareTo("]") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "]" + "</char>");
                newXML.println("<unicode>" + rightArrayBracket.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (leftBrace.getText().compareTo("{") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "{" + "</char>");
                newXML.println("<unicode>" + leftBrace.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (rightBrace.getText().compareTo("}") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "}" + "</char>");
                newXML.println("<unicode>" + rightBrace.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (semiColon.getText().compareTo(";") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + ";" + "</char>");
                newXML.println("<unicode>" + semiColon.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (colon.getText().compareTo(":") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + ":" + "</char>");
                newXML.println("<unicode>" + colon.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (doubleQuote.getText().compareTo("\"") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "\"" + "</char>");
                newXML.println("<unicode>" + doubleQuote.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (singleQuote.getText().compareTo("'") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "'" + "</char>");
                newXML.println("<unicode>" + singleQuote.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (comma.getText().compareTo(",") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "," + "</char>");
                newXML.println("<unicode>" + comma.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (fullStop.getText().compareTo(".") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "." + "</char>");
                newXML.println("<unicode>" + fullStop.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (leftAngularBracket.getText().compareTo("<") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "<" + "</char>");
                newXML.println("<unicode>" + leftAngularBracket.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (rightAngularBracket.getText().compareTo(">") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + ">" + "</char>");
                newXML.println("<unicode>" + rightAngularBracket.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (questionMark.getText().compareTo("?") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "?" + "</char>");
                newXML.println("<unicode>" + questionMark.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (frontSlash.getText().compareTo("/") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "/" + "</char>");
                newXML.println("<unicode>" + frontSlash.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            if (tilde.getText().compareTo("~") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "~" + "</char>");
                newXML.println("<unicode>" + tilde.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }


            if (backTick.getText().compareTo("`") == 0) {
                //don't add to the XML file
            } else {
                newXML.println("<pattern>");
                newXML.println("<char>" + "`" + "</char>");
                newXML.println("<unicode>" + backTick.getText() + "</unicode>");
                newXML.println("</pattern>\n");
            }

            newXML.print("</" + temp + ">");

            newXML.close();
            //shell.close();
            //shell.dispose();


        } catch (FileNotFoundException ex) {
            Logger.getLogger(XMLGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            // Cancel button of the file save dialog
            // do nothing
        } finally {
            shell.close();
            shell.dispose();
        }
    }
}
