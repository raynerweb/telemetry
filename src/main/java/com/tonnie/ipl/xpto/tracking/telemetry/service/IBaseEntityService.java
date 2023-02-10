package com.tonnie.ipl.xpto.tracking.telemetry.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import com.tonnie.ipl.xpto.tracking.telemetry.model.IEntity;

public interface IBaseEntityService<ID_CLASS extends Serializable, ENTITY extends IEntity<ID_CLASS>> {

  Optional<ENTITY> findById(ID_CLASS id);
  
  Optional<ENTITY> findById(ENTITY filter);

  List<ENTITY> findAll();

  ENTITY save(ENTITY entity);
  
  ENTITY update(ENTITY entity);

  void delete(ID_CLASS id);

  void delete(ENTITY obj);

}
