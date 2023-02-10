package com.tonnie.ipl.xpto.tracking.telemetry.model;

import java.io.Serializable;

public interface IEntity<T extends Serializable> {
  
  public T getId();

}
