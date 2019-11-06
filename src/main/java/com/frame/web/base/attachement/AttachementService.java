package com.frame.web.base.attachement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttachementService {

    @Autowired
    private AttachementDao attachementDao;

    public Attachement getAttachement(String fileId){
        return attachementDao.find(fileId);
    }

    public Attachement deleteAttachement(String fileId){
        Attachement attachement = attachementDao.find(fileId);
        attachementDao.deleteById(fileId);
        return attachement;
    }

    public Attachement saveAttachement(Attachement attachement){
        return attachementDao.save(attachement);
    }
}
