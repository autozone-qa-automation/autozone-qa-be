package com.az_qa.backend.service;

import com.az_qa.backend.dto.request.FeaturesRequest;
import com.az_qa.backend.dto.response.FeaturesResponse;
import com.az_qa.backend.exception.ResourceNotFoundException;
import java.util.List;

public interface FeaturesService {

  /**
   * Retrieves a user by their ID.
   * @param id the user's ID
   * @return the matching user
   * @throws ResourceNotFoundException if no user exists with the given ID
   */
  FeaturesResponse getFeatureById(Long id);

  /**
   * Retrieves all registered users.
   * @return a list of all users, or an empty list if none exist
   */
  List<FeaturesResponse> getAllFeatures();

  FeaturesResponse create(FeaturesRequest request);
}
