package ru.notivent.configuration;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

@MappedTypes({UUID.class})
public class UuidTypeHandler implements TypeHandler<UUID> {
  @Override
  public void setParameter(PreparedStatement ps, int i, UUID parameter, JdbcType jdbcType)
      throws SQLException {
    if (parameter == null) {
      ps.setObject(i, null, Types.OTHER);
    } else {
      ps.setObject(i, parameter.toString(), Types.OTHER);
    }
  }

  @Override
  public UUID getResult(ResultSet rs, String columnName) throws SQLException {
    return toUUID(rs.getString(columnName));
  }

  @Override
  public UUID getResult(ResultSet rs, int columnIndex) throws SQLException {
    return toUUID(rs.getString(columnIndex));
  }

  @Override
  public UUID getResult(CallableStatement cs, int columnIndex) throws SQLException {
    return toUUID(cs.getString(columnIndex));
  }

  private static UUID toUUID(String val) {
    if (!val.isBlank()) {
      try {
        return UUID.fromString(val);
      } catch (IllegalArgumentException ex) {
        // not allowed
      }
    }
    return null;
  }
}
