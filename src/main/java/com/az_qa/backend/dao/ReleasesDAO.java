package com.az_qa.backend.dao;

import java.lang.StackWalker.Option;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.az_qa.backend.mapper.ReleaseMapper;
import com.az_qa.backend.repository.ReleaseRepository;
import com.az_qa.backend.vo.ReleaseVO;

@Repository
public class ReleasesDAO {
    @Autowired
    private ReleaseRepository releaseRepository;

    public ReleaseVO findById(Long id) {
        Optional<ReleaseVO> releaseVO = releaseRepository.findById(id).map(ReleaseMapper::toVO);

    }
}
