
package no.nmdc.oaipmh.provider.dao.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import no.nmdc.oaipmh.provider.domain.SetType;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author kjetilf
 */
public class SetRowMapper implements RowMapper<SetType> {

    @Override
    public SetType mapRow(ResultSet rs, int rowNum) throws SQLException {
        SetType setType = new SetType();
        setType.setSetName(rs.getString("originating_center"));
        setType.setSetSpec(rs.getString("originating_center"));
        return setType;
    }    

}
