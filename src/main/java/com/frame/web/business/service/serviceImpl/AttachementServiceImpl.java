package com.frame.web.business.service.serviceImpl;

import com.logistics.web.dao.AttachementDao;
import com.logistics.web.entity.system.Attachement;
import com.logistics.web.service.AttachementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttachementServiceImpl implements AttachementService {

    @Autowired
    private AttachementDao attachementDao;

    @Override
    public Attachement getAttachement(String fileId){
        return attachementDao.find(fileId);
    }

    @Override
    public Attachement deleteAttachement(String fileId){
        Attachement attachement = attachementDao.find(fileId);
        attachementDao.deleteById(fileId);
        return attachement;
    }

    @Override
    public Attachement saveAttachement(Attachement attachement){
        return attachementDao.save(attachement);
    }
}
