package com.frame.web.base.attachement;

import com.frame.core.http.ResponseEntity;
import com.frame.core.utils.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;

@RestController
@RequestMapping("/common/file")
public class AttachementController {

    @Value("${system.filebasepath}")
    public String filebasepath;

    @Autowired
    private AttachementService attachementService;

    private final String fileDownloadUrl = "/fileDownload/{fileId}";

    private final String fileReviewUrl = "/fileReview/{fileId}";

    @PostMapping("/{module}/fileUpload")
    public ResponseEntity fileUpload(@RequestParam("file") CommonsMultipartFile file, @PathVariable("module") String module) throws IOException {
        String dirPath = String.format("%s/%s", filebasepath, module);
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String path = String.format("%s/%s", dirPath, System.currentTimeMillis() + file.getOriginalFilename());
        File newFile = new File(path);
        Attachement attachement = new Attachement();
        attachement.setId(EncryptUtil.randomUUID());
        attachement.setName(file.getOriginalFilename());
        attachement.setPath(path);
        attachement.setSize(file.getSize());
        attachement.setType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase());
        attachement.setDownloadUrl(fileDownloadUrl.replace("{fileId}", attachement.getId()));
        attachement.setReviewUrl(fileReviewUrl.replace("{fileId}", attachement.getId()));
        file.transferTo(newFile);
        attachementService.saveAttachement(attachement);
        return ResponseEntity.ok().putData(attachement);
    }

    @GetMapping(fileDownloadUrl)
    public void fileDownload(@PathVariable("fileId") String fileId, HttpServletResponse response) throws IOException {
        Attachement attachement = attachementService.getAttachement(fileId);
        this.fileDownload(attachement.getPath(), response, false);
    }

    @GetMapping(fileReviewUrl)
    public void fileShow(@PathVariable("fileId") String fileId, HttpServletResponse response) throws IOException {
        Attachement attachement = attachementService.getAttachement(fileId);
        this.fileDownload(attachement.getPath(), response, true);
    }

    @PostMapping("/fileDelete/{fileId}")
    public ResponseEntity fileDelete(@PathVariable("fileId") String fileId) {
        Attachement attachement = attachementService.deleteAttachement(fileId);
        if (attachement == null) {
            return ResponseEntity.error("删除失败，附件不存在！！");
        }
        File file = new File(attachement.getPath());
        file.delete();
        return ResponseEntity.ok();
    }

    /**
     * 文件下载
     *
     * @param filePath 文件路径
     * @param response web返回参数
     * @param isOnLine 是否在线预览
     * @throws IOException
     */
    private void fileDownload(String filePath, HttpServletResponse response, boolean isOnLine) throws IOException {
        File f = new File(filePath);
        if (!f.exists()) {
            response.sendError(404, "File not found!");
            return;
        }
        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(f));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        //response.reset(); // 非常重要
        String fileName = URLEncoder.encode(f.getName(), "UTF-8");
        if (isOnLine) { // 在线打开方式
            URL u = new URL("file:///" + filePath);
            response.setContentType(u.openConnection().getContentType());
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
        } else { // 纯下载方式
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        }
        OutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(buffer);
        out.flush();
        out.close();
    }
}
