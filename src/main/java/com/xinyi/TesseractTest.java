package com.xinyi;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.junit.Before;

public class TesseractTest {

        private ITesseract tesseract;

        @Before
        public void init() {
            tesseract = new Tesseract();
            System.out.println("tesseract init done...");
        }

}
