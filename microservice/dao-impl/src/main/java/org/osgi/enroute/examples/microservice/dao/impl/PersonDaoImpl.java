package org.osgi.enroute.examples.microservice.dao.impl;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;// had to add manually
import org.slf4j.LoggerFactory;


import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static org.osgi.enroute.examples.microservice.dao.impl.PersonTable.INIT;
import static org.osgi.enroute.examples.microservice.dao.impl.PersonTable.FIRST_NAME;
import static org.osgi.enroute.examples.microservice.dao.impl.PersonTable.LAST_NAME;
import static org.osgi.enroute.examples.microservice.dao.impl.PersonTable.PERSON_ID;
import static org.osgi.enroute.examples.microservice.dao.impl.PersonTable.SQL_DELETE_PERSON_BY_PK;
import static org.osgi.enroute.examples.microservice.dao.impl.PersonTable.SQL_INSERT_PERSON;
import static org.osgi.enroute.examples.microservice.dao.impl.PersonTable.SQL_SELECT_PERSON_BY_PK;
import static org.osgi.enroute.examples.microservice.dao.impl.PersonTable.SQL_SELECT_ALL_PERSONS;
import static org.osgi.enroute.examples.microservice.dao.impl.PersonTable.SQL_UPDATE_PERSON_BY_PK;



import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.osgi.service.component.annotations.Reference;
import org.osgi.enroute.examples.microservice.dao.PersonDao;
import org.osgi.enroute.examples.microservice.dao.dto.PersonDto;
import org.osgi.service.transaction.control.TransactionControl;
import org.osgi.service.transaction.control.jdbc.JDBCConnectionProvider;
import org.osgi.enroute.examples.microservice.dao.AddressDao;
import org.osgi.service.component.annotations.Activate;



import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;


@Component
public class PersonDaoImpl implements PersonDao{

	private static final Logger logger = LoggerFactory.getLogger(PersonDaoImpl.class);
	
	@Reference
	TransactionControl transactionControl;
	
	@Reference(name= "provider")
	JDBCConnectionProvider jdbcConnectionProvider;
	
	@Reference
	AddressDao addressDao;
	
	Connection connection;
	
	@Activate
	void start(Map<String, Object> props) throws SQLException {
		connection = jdbcConnectionProvider.getResource(transactionControl);
		transactionControl.supports(()-> connection.prepareStatement(INIT).execute());
	}
	
	@Override
	public List<PersonDto> select() {
		return transactionControl.notSupported(() -> {

            List<PersonDto> dbResults = new ArrayList<>();

            ResultSet rs = connection.createStatement().executeQuery(SQL_SELECT_ALL_PERSONS);

            while (rs.next()) {
                PersonDto personDto = mapRecordToPerson(rs);
                personDto.addresses = addressDao.select(personDto.personId);
                dbResults.add(personDto);
            }

            return dbResults;
        });
	}

	@Override
	public PersonDto findByPk(Long pk) {
	       return transactionControl.supports(() -> {

	            PersonDto personDTO = null;

	            PreparedStatement pst = connection.prepareStatement(SQL_SELECT_PERSON_BY_PK);
	            pst.setLong(1, pk);

	            ResultSet rs = pst.executeQuery();

	            if (rs.next()) {
	                personDTO = mapRecordToPerson(rs);
	                personDTO.addresses = addressDao.select(pk);
	            }

	            return personDTO;
	        });
	}

	@Override
	public Long save(PersonDto data) {
		  return transactionControl.required(() -> {

	            PreparedStatement pst = connection.prepareStatement(SQL_INSERT_PERSON, RETURN_GENERATED_KEYS);

	            pst.setString(1, data.firstName);
	            pst.setString(2, data.lastName);

	            pst.executeUpdate();

	            AtomicLong genPersonId = new AtomicLong(data.personId);

	            if (genPersonId.get() <= 0) {
	                ResultSet genKeys = pst.getGeneratedKeys();

	                if (genKeys.next()) {
	                    genPersonId.set(genKeys.getLong(1));
	                }
	            }

	            logger.info("Saved Person with ID : {}", genPersonId.get());

	            if (genPersonId.get() > 0) {
	                data.addresses.stream().forEach(address -> {
	                    address.personId = genPersonId.get();
	                    addressDao.save(genPersonId.get(), address);
	                });
	            }

	            return genPersonId.get();
	        });
	}

	@Override
	public void update(PersonDto data) {
		// TODO Auto-generated method stub

        transactionControl.required(() -> {

            PreparedStatement pst = connection.prepareStatement(SQL_UPDATE_PERSON_BY_PK);
            pst.setString(1, data.firstName);
            pst.setString(2, data.lastName);
            pst.setLong(3, data.personId);
            pst.executeUpdate();

            logger.info("Updated person : {}", data);

            data.addresses.stream().forEach(address -> addressDao.update(data.personId, address));
            
            return null;
        });
		
	}

	@Override
	public void delete(Long primaryKey) {
		// TODO Auto-generated method stub
		

        transactionControl.required(() -> {
            PreparedStatement pst = connection.prepareStatement(SQL_DELETE_PERSON_BY_PK);
            pst.setLong(1, primaryKey);
            pst.executeUpdate();
            addressDao.delete(primaryKey);
            logger.info("Deleted Person with ID : {}", primaryKey);
            return null;
        });
		
	}
    
	protected PersonDto mapRecordToPerson(ResultSet rs)  throws SQLException{
		PersonDto personDto = new PersonDto();
		personDto.personId = rs.getLong(PERSON_ID);
		personDto.firstName = rs.getString(FIRST_NAME);
		personDto.lastName = rs.getString(LAST_NAME);
		// TODO Auto-generated method stub
		return personDto;
	}

	
    //TODO add an implementation
    
}
