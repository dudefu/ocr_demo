package com.xinyi;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

/**
 * Created by WSK on 2017/1/6.
 */
public class OCR {
    /**
     *
     * @param srImage 图片路径
     * @param ZH_CN 是否使用中文训练库,true-是
     * @return 识别结果
     */
    public static String FindOCR(String srImage, boolean ZH_CN) {
        try {
            System.out.println("start");
            double start=System.currentTimeMillis();
            File imageFile = new File(srImage);
            if (!imageFile.exists()) {
                return "图片不存在";
            }
            BufferedImage textImage = ImageIO.read(imageFile);
            Tesseract instance=new Tesseract();
            instance.setDatapath("/Volumes/WORKS/OCR/tessdata-4.1.0/");//设置训练库
            if (ZH_CN)
                instance.setLanguage("chi_sim");//中文识别
            String result = null;
            result = instance.doOCR(textImage);
            double end=System.currentTimeMillis();
            System.out.println("耗时"+(end-start)/1000+" s");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "发生未知错误";
        }
    }
    public static void main(String[] args) throws Exception {
//        String result=FindOCR("/Users/dudefu/Downloads/test.png",true);
//        System.out.println(result);
        Tesseract tesseract=new Tesseract();
        tesseract.setDatapath("/Volumes/WORKS/OCR/tessdata-4.1.0/");//设置训练库
        tesseract.setLanguage("chi_sim");//中文识别
        File imageFile = new File("/Volumes/WORKS/OCR/ocr_demo/src/main/resources/data/pai2.png");
        BufferedImage image = ImageIO.read(imageFile);
//        BufferedImage image = ImageIO.read(new URL("http://static8.ziroom.com/phoenix/pc/images/price/aacd14fbc53a106c7f0f0d667535683as.png"));
        String ocr = tesseract.doOCR(image);
        System.out.println("ocr result : " + ocr);
    }

    /**
     * 图片识别
     * @author wangy
     * @date 2019-08-26
     * @param parameter
     */
    public static  String  ocrResult(WebElement element ) throws Exception {

        FileSystemView fsv = FileSystemView.getFileSystemView();
        File com=fsv.getHomeDirectory();    //这便是读取桌面路径的方法了
        String url = "";
        String os = System.getProperty("os.name");
        //识别系统，找不同的语言包路径
        if (os.indexOf("Windows") == -1) {
            url = "/opt/google/";
        } else {
            url = com.getPath();
        }
        //获取元素截图的路径
        String path[]=Screenshot.elementscreenShot(element);
        //获取未处理的截图路径
        String imgpath=path[0];
        String result = null;
        File imageFile = new File(imgpath);
        //要对图片处理
        CleanElementImage.handlImage(imageFile,path[1]);
        ITesseract instance = new Tesseract();
        //读取语言包的路径地址
        instance.setDatapath(url + File.separator + "test" + File.separator
                + "tessdata");
        // 默认是英文（识别字母和数字），如果要识别中文(数字 + 中文），需要制定语言包，这里是数字，所以没用语言包
        // instance.setLanguage("chi_sim");
        //为了防止没截完图片就识别，做了一个简单的循环
        try{
            String ocrResult=instance.doOCR(imageFile);
            if(imageFile.exists()&&ocrResult!=""){
                result=ocrResult;
            }else {
                while(true){
                    Thread.sleep(1000);
                    if(imageFile.exists()&&ocrResult!=""){
                        result=ocrResult;
                        break;
                    }
                }
            }

        }catch(TesseractException e){
            System.out.println(e.getMessage());
        }
        return result;
    }

}

