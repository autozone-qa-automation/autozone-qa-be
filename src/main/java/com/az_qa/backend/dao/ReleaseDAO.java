package com.az_qa.backend.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.az_qa.backend.entity.ReleaseEntity;
import com.az_qa.backend.mapper.ReleaseMapper;
import com.az_qa.backend.repository.ReleaseRepository;
import com.az_qa.backend.vo.ReleaseVO;
import com.az_qa.backend.exception.ItemNotFoundException;;

@Repository
public class ReleaseDAO {
    /**
     * Repository dependency used for movie persistence operations.
     */
    @Autowired
    private ReleaseRepository releaseRepository;

    public ReleaseVO findById(Long id) {
        Optional<ReleaseVO> releaseVO = releaseRepository.findById(id).map(ReleaseMapper::toVO);

        if (ReleaseVO.isEmpty()) {
            throw new ItemNotFoundException("Release with id " + id + " not found");
        }

        return ReleaseVO.get();
    }

    public List<ReleaseVO> findAll() {
        return releaseRepository.findAll().stream().map(ReleaseMapper::toVO).toList();
    }

    public ReleaseVO save(ReleaseVO releaseVO) {
        ReleaseEntity releaseEntity = ReleaseMapper.toEntity(releaseVO);
        if (releaseEntity == null) {
            return null;
        }
        if (releaseEntity.getReleaseId() == null) {
            releaseEntity.setNew(true);
        }
        return ReleaseMapper.toVO(releaseRepository.save(releaseEntity));
    }

    public void deleteById(Long id) {
        releaseRepository.deleteById(id);
    }
}