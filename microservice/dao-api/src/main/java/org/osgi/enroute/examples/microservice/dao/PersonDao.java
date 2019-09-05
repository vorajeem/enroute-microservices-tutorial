/**
 * 
 */
package org.osgi.enroute.examples.microservice.dao;
import org.osgi.annotation.versioning.ProviderType;
import java.util.List;
import org.osgi.enroute.examples.microservice.dao.dto.PersonDto;
/**
 * @author vivcrone
 *
 */
@ProviderType
public interface PersonDao {
	public List<PersonDto> select();
	public PersonDto findByPk(Long pk);
	public Long save(PersonDto data);
	public void update(PersonDto data);
	public void delete(Long pk);
}
