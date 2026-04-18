package com.az_qa.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.az_qa.backend.dao.ReleasesDAO;
import com.az_qa.backend.vo.ReleaseVO;

@Service
public class ReleaseService {
    @Autowired
    private ReleasesDAO releasesDAO;

    public ReleaseVO findById(long id) {
        return releasesDAO.findById(id);
    }

    public List<ReleaseVO> findAll() {
        return releasesDAO.findAll();
    }

    public ReleaseVO add(ReleaseVO releaseVO) {
        return releasesDAO.save(releaseVO);
    }
}
