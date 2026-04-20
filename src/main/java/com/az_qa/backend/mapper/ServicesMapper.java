/*
 * Tecnológico de Monterrey — Campus Chihuahua
 * Desarrollo e Implantación de Sistemas de Software
 * TC3005B GPO500 - 2026
 * Autozone QA Automation
 */

package com.az_qa.backend.mapper;

import com.az_qa.backend.entity.ServicesEntity;
import com.az_qa.backend.entity.UrlEntity;
import com.az_qa.backend.vo.ServicesVO;
import com.az_qa.backend.vo.UrlVO;
import java.util.Collections;
import java.util.List;

/**
 * Utility mapper for converting between persistence entities and API value
 * objects.
 */
public final class ServicesMapper {

  /** Utility class constructor. */
  private ServicesMapper() {}

  /**
   * Converts a {@link ServicesEntity} into a {@link ServicesVO}.
   * Returns {@code null} when the input is {@code null}.
   *
   * @param entity persistence entity
   * @return value object representation or {@code null}
   */
  public static ServicesVO toVO(ServicesEntity entity) {
    if (entity == null) {
      return null;
    }

    List<UrlVO> urls =
        entity.getUrls() == null
            ? Collections.emptyList()
            : entity.getUrls().stream().map(ServicesMapper::urlToVO).toList();

    return new ServicesVO(entity.getId(), entity.getName(), entity.getDescription(), urls);
  }

  /**
   * Converts a {@link UrlEntity} into a {@link UrlVO}.
   * Returns {@code null} when the input is {@code null}.
   *
   * @param urlEntity persistence entity
   * @return value object representation or {@code null}
   */
  public static UrlVO urlToVO(UrlEntity urlEntity) {
    if (urlEntity == null) {
      return null;
    }

    return new UrlVO(urlEntity.getIdUrl(), urlEntity.getNombre(), urlEntity.getUrl());
  }
}
