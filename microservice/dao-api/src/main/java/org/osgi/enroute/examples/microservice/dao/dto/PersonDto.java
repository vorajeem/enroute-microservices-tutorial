/**
 * 
 */
package org.osgi.enroute.examples.microservice.dao.dto;

import java.util.List;
import java.util.ArrayList;
import org.osgi.enroute.examples.microservice.dao.dto.AddressDto;
/**
 * @author vivcrone
 *
 */
public class PersonDto {

	public long personId;
	public String firstName;
	public String lastName;
	public List<AddressDto> addresses = new ArrayList<>();
}
