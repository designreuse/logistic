package com.frame.web.business.dao;

import com.frame.web.base.baseRepository.BaseRepository;
import com.frame.web.business.entity.orgainzation.Dept;
import org.springframework.stereotype.Repository;

@Repository
public interface DeptDao extends BaseRepository<Dept,String>{
    Dept findByIdAndRevision(String id, Integer revision);
}
