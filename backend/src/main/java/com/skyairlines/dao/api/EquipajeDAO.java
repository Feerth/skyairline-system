package com.skyairlines.dao.api;

import com.skyairlines.model.entity.Equipaje;
import com.skyairlines.model.enums.EstadoEquipaje;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface EquipajeDAO extends GenericDAO<Equipaje, Integer> {
    Optional<Equipaje> findByCodigoEtiqueta(String codigo) throws SQLException;
    List<Equipaje> findByBoleto(Integer idBoleto) throws SQLException;
    List<Equipaje> findByVuelo(Integer idVuelo) throws SQLException;
    boolean actualizarEstado(Integer id, EstadoEquipaje nuevoEstado) throws SQLException;
    List<Equipaje> findByEstado(EstadoEquipaje estado) throws SQLException;
}