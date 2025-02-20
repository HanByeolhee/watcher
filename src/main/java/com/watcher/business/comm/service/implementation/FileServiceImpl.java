package com.watcher.business.comm.service.implementation;

import com.amazonaws.AmazonServiceException;
import com.watcher.business.comm.mapper.FileMapper;
import com.watcher.business.comm.param.FileParam;
import com.watcher.business.comm.service.FileService;
import com.watcher.config.WatcherConfig;
import com.watcher.util.AwsS3Util;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;
import java.util.List;

@Service
@Log4j2
public class FileServiceImpl implements FileService {

    // 이미지 크기 결정
    private int scaledWidth = 1200;

    private int scaledHeight = 630;

    @Autowired
    FileMapper fileMapper;

    @Value("${upload.root}")
    String fileUploadRootPath;

    @Value("${upload.path}")
    String fileUploadPath;

    @Value("${aws.separator}")
    String awsSeparator;


    @Override
    public String upload(String base64Image, String savePath) throws Exception {
        // base64 데이터에서 이미지 바이트 배열로 디코딩

        // base64 데이터를 디코딩하여 이미지 바이트 배열로 변환
        byte[] imageBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);

        // 바이트 배열을 ByteArrayInputStream으로 변환하여 InputStream을 반환합니다.
        ByteArrayInputStream imgInputStream = new ByteArrayInputStream(imageBytes);

        try {
            AwsS3Util.putImage(savePath, imgInputStream);
        } catch (AmazonServiceException e) {
            throw new Exception("4001");
        }

        return AwsS3Util.getBucketUrl() + savePath;
    }


    @Transactional
    @Override
    public List<Integer> uploadAfterSavePath(MultipartFile[] uploadFiles, String savePath, FileParam fileParam) throws Exception {
        List<Integer> result = new ArrayList<>();

        long millis = System.currentTimeMillis();

        for (MultipartFile file : uploadFiles) {
            if (file.isEmpty()) {
                continue;
            }

            String original_filename = file.getOriginalFilename();
            String server_filename = File.separator + String.valueOf(millis) + original_filename.substring(original_filename.lastIndexOf("."));
            String upload_full_path = fileUploadPath + savePath + File.separator + fileParam.getContentsId();
            String extension = server_filename.substring(server_filename.lastIndexOf(".") + 1);

            original_filename = changeFileSeparator(original_filename);
            server_filename = changeFileSeparator(server_filename);
            upload_full_path = changeFileSeparator(upload_full_path);

            BufferedImage inputImage = ImageIO.read(file.getInputStream());

            // 스케일링을 위한 BufferedImage 생성
            BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);

            // Graphics2D를 이용한 스케일링
            Graphics2D g2d = outputImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
            g2d.dispose();

            // BufferedImage를 바이트 배열로 변환합니다.
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(outputImage, extension, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            // 바이트 배열을 ByteArrayInputStream으로 변환하여 InputStream을 반환합니다.
            ByteArrayInputStream imgInputStream = new ByteArrayInputStream(bytes);

            try {
                AwsS3Util.putImage(upload_full_path + server_filename, imgInputStream);
            } catch (AmazonServiceException e) {
                throw new Exception("4001");
            }

            fileParam.setRealFileName(original_filename);
            fileParam.setSavePath(AwsS3Util.getBucketUrl() + changeFileSeparatorAws(upload_full_path));
            fileParam.setServerFileName(changeFileSeparatorAws(server_filename));
            fileParam.setPathSeparator(File.separator);

            fileMapper.insert(fileParam);
            result.add(Integer.valueOf(fileParam.getId()));
        }

        return result;
    }


    @Transactional
    @Override
    public int uploadAfterSavePath(MultipartFile uploadfile, String savePath, FileParam fileParam) throws Exception {
        return uploadAfterSavePath(new MultipartFile[]{uploadfile}, savePath, fileParam).get(0);
    }


    @Transactional
    @Override
    public Map<String, String> download(FileParam fileParam) throws Exception {
        LinkedHashMap result = new LinkedHashMap();
        return result;
    }


    private String changeFileSeparator(String path) {
        if ("\\".equals(WatcherConfig.file_separator)) {
            return path.replaceAll("/", WatcherConfig.file_separator + WatcherConfig.file_separator);
        }

        return path.replaceAll("/", WatcherConfig.file_separator);
    }


    private String changeFileSeparatorAws(String path) {
        String tempPath = path;

        tempPath = tempPath.replaceAll(WatcherConfig.file_separator + WatcherConfig.file_separator, awsSeparator);

        if (tempPath.indexOf(awsSeparator) == -1) {
            tempPath = tempPath.replaceAll(WatcherConfig.file_separator, awsSeparator);
        }

        return tempPath;
    }


}
