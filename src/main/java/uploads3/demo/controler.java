package uploads3.demo;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
@Controller
public class controler {
    @RequestMapping(value = "/upload",method = RequestMethod.GET)
    public String get(Model model){
        return "upload";
    }
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public String post(Model model,HttpServletRequest request,MultipartFile file) {
        doUpload(request,model,file);
        ////
        String filename="";
        filename=file.getOriginalFilename();
        String awsId="AKIAIDUU337NCHGSTJXA";
        String awsKey="PNzscwCH0wNh/5PkGMNz9WWwL0x/E3C21gIXhIgl";
        String bucketName="bucketdemouploadfile";
        String urlReturn="";
        AWSCredentials credentials = new BasicAWSCredentials(
                awsId,
                awsKey);
        AmazonS3 s3client = new AmazonS3Client(credentials);
        try {
            File fileupload = new File("src/main/webapp/upload/"+file.getOriginalFilename());
            s3client.putObject(new PutObjectRequest(bucketName, filename, fileupload).withCannedAcl(CannedAccessControlList.PublicRead));
            System.out.println("upload thanh cong");
//            urlReturn=s3client.getObject(bucketName,filename).getRedirectLocation();
//            System.out.println(urlReturn);
        } catch (AmazonServiceException e) {
            System.out.println("AmazonServiceException: "+ e.getMessage());
        } catch (AmazonClientException ace) {
            System.out.println("AmazonClientException: "+ ace.getMessage());
        }
        return "upload";
    }
    private String doUpload(HttpServletRequest request, Model model,
                            MultipartFile file) {
        String uploadRootPath = request.getServletContext().getRealPath("upload");
        System.out.println("uploadRootPath=" + uploadRootPath);

        File uploadRootDir = new File(uploadRootPath);
        // Tạo thư mục gốc upload nếu nó không tồn tại.
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
            // Tên file gốc tại Client.
            String name = file.getOriginalFilename();
            System.out.println("Client File Name = " + name);

            if (name != null && name.length() > 0) {
                try {
                    // Tạo file tại Server.
                    File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);

                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(file.getBytes());
                    stream.close();
                    System.out.println("Write file: " + serverFile);
                } catch (Exception e) {
                    System.out.println("Error Write file: " + e.getMessage());
                }
            }
        return "null";
    }
}
