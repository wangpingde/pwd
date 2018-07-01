package com.cn.pwd.controller.file;

import com.cn.pwd.service.IFileService;
import com.mongodb.gridfs.GridFSDBFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/file")
public class FileResource {

    @Resource
    private IFileService iFileService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> upload(HttpServletRequest request, MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String fileId = iFileService.saveFile(file.getInputStream(), fileName, mediaType(fileName).toString());
            Map<String, String> map = new HashMap<String, String>();
            map.put("fileId", fileId);
            map.put("fileName", fileName);
            return new ResponseEntity(map,HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<?> downloadFile(HttpServletRequest request, @PathVariable String fileId) {
        try {
            GridFSDBFile file = iFileService.queryFileById(fileId);
            HttpHeaders headers = new HttpHeaders();
            MediaType mediaType = MediaType.parseMediaType(file.getContentType());
            headers.setContentType(mediaType);
            return new ResponseEntity<byte[]>(deSerialize(file.getInputStream()),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFile(HttpServletRequest request, @PathVariable String fileId) {
        try {
            iFileService.deleteFile(fileId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 文件类型确定传输http
     *
     * @param name
     * @return
     */
    private MediaType mediaType(String name) {
        String sufix = name.substring(name.lastIndexOf(".") + 1);
        if (!StringUtils.isEmpty(sufix)) {
            if ("GIF".equals(sufix.toUpperCase())) {
                return MediaType.IMAGE_GIF;
            }
            if ("PNG".equals(sufix.toUpperCase())) {
                return MediaType.IMAGE_PNG;
            }
            if ("JPEG".equals(sufix.toUpperCase()) || "JPG".equals(sufix.toUpperCase()) || "IMG".equals(sufix.toUpperCase())) {
                return MediaType.IMAGE_JPEG;
            }
            if ("PDF".equals(sufix.toUpperCase())) {
                return MediaType.parseMediaType("application/json");
            }
        }
        return MediaType.APPLICATION_OCTET_STREAM;

    }


    /**
     * @param inputStream
     * @return
     * @throws IOException
     */
    private byte[] deSerialize(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(buf)) != -1)
                bos.write(buf, 0, len);
            return bos.toByteArray();
        } catch (IOException e) {

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (bos != null) {
                bos.flush();
                bos.close();
            }
        }
        return null;
    }


}
