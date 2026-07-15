package com.moyuyo.service;

import com.moyuyo.common.dto.address.AddressValidateResponse;
import com.moyuyo.dao.entity.AddressEntity;

import java.util.List;

public interface AddressService {

  List<AddressEntity> listByUserId(Long userId);

  AddressEntity getById(Long id, Long userId);

  AddressEntity create(Long userId, AddressEntity address);

  AddressEntity update(Long userId, AddressEntity address);

  void delete(Long id, Long userId);

  void setDefault(Long id, Long userId);

  AddressValidateResponse validateAddress(Long addressId, Long userId);
}
