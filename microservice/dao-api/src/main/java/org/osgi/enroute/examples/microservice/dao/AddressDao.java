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
public interface AddressDao {
	public List<AddressDto> select(Long personId);
	public AddressDto findByPk(String emailAddress);
	public void save(Long personId, AddressDto data);
	public void update(Long personId, AddressDto data);
	public void delete(Long personId);

}
