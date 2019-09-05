/**
 * 
 */
package org.osgi.enroute.examples.microservice.dao;
import org.osgi.annotation.versioning.ProviderType;
import java.util.List;
/**
 * @author vivcrone
 *
 */
@ProviderType
public interface PersonDao {
	public List<PersonDao> select();
	public PersonDto findByPk(Long pk);
	public Long save(PersonDto data);
	public void update(PersonDto data);
	public void delete(Long pk);
}
