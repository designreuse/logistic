package com.frame.web.business.service;

import com.logistics.web.entity.system.Attachement;

public interface AttachementService {
    Attachement getAttachement(String fileId);

    Attachement deleteAttachement(String fileId);

    Attachement saveAttachement(Attachement attachement);
}
