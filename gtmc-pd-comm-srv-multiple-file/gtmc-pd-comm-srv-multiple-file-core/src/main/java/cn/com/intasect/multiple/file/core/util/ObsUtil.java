package cn.com.intasect.multiple.file.core.util;

import com.obs.services.ObsClient;
import com.obs.services.model.ObsObject;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Component
public class ObsUtil {
    private String ak="8A96QLS1IB2L5S06YRFJ";

    private String sk="u79oXUK4I5qfALWE1WlOZCPQUcXE9RFz8AtgcLvc";

    private String endPoint="https://obs.cn-north-4.myhuaweicloud.com";

    private String bucketName= "lyq-bucket-test";
    //上传文件，multipartFile就是你要的文件，
    //objectKey就是文件名，如果桶中有文件夹的话，如往test文件上传test.txt文件，那么objectKey就是test/test.txt
    public void uploadFile(MultipartFile multipartFile, String objectKey) throws Exception{

        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        System.out.println("hehe");
        objectKey = "hed/hweiobs.txt";
        InputStream inputStream = new FileInputStream(new File("D:/Test/hweiobs.txt"));
        System.out.println("putObject11111");
        obsClient.putObject(bucketName, objectKey, inputStream);
        System.out.println("putObject2222");
        inputStream.close();
        obsClient.close();

        System.out.println("obsUtil:hello");
        System.out.println(obsClient);
    }
    public void download() throws Exception{

        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        String objectKey = "sup/lifecycle/info/1666712982813044738.xlsx";
        ObsObject obsObject = obsClient.getObject(bucketName, objectKey);
        // 读取对象内容
        System.out.println("Object content:");
        InputStream input = obsObject.getObjectContent();
        byte[] b = new byte[1024];
        //ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStream bos = new FileOutputStream(new File("D:/Test/hweiobs"+ System.currentTimeMillis() +".xlsx"));
        int len;
        while ((len=input.read(b)) != -1){
            bos.write(b, 0, len);
        }

        //System.out.println(new String(bos.toByteArray()));
        bos.flush();
        bos.close();
        input.close();
    }
    public void deleteFile(String objectKey) throws Exception{
        objectKey = "lyq/lyqtest.txt";
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        obsClient.deleteObject(bucketName, objectKey);
        obsClient.close();
    }

    public static void main(String[] args) {
        ObsUtil util = new ObsUtil();
        try {
//            util.uploadFile(null, null);
            util.download();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
