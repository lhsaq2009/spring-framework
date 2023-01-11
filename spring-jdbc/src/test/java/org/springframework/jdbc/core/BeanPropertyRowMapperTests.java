package org.springframework.jdbc.core;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.test.ConcretePerson;
import org.springframework.jdbc.core.test.DatePerson;
import org.springframework.jdbc.core.test.ExtendedPerson;
import org.springframework.jdbc.core.test.Person;
import org.springframework.jdbc.core.test.SpacePerson;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Thomas Risberg
 * @author Juergen Hoeller
 */
public class BeanPropertyRowMapperTests extends AbstractRowMapperTests {

	@Test
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void testOverridingDifferentClassDefinedForMapping() {
		BeanPropertyRowMapper mapper = new BeanPropertyRowMapper(Person.class);
		assertThatExceptionOfType(InvalidDataAccessApiUsageException.class).isThrownBy(() ->
				mapper.setMappedClass(Long.class));
	}

	@Test
	public void testOverridingSameClassDefinedForMapping() {
		BeanPropertyRowMapper<Person> mapper = new BeanPropertyRowMapper<>(Person.class);
		mapper.setMappedClass(Person.class);
	}

	@Test
	public void testStaticQueryWithRowMapper() throws Exception {
		Mock mock = new Mock();
		List<Person> result = mock.getJdbcTemplate().query(
				"select name, age, birth_date, balance from people",
				new BeanPropertyRowMapper<>(Person.class));
		assertThat(result.size()).isEqualTo(1);
		verifyPerson(result.get(0));
		mock.verifyClosed();
	}

	@Test
	public void testMappingWithInheritance() throws Exception {
		Mock mock = new Mock();
		List<ConcretePerson> result = mock.getJdbcTemplate().query(
				"select name, age, birth_date, balance from people",
				new BeanPropertyRowMapper<>(ConcretePerson.class));
		assertThat(result.size()).isEqualTo(1);
		verifyPerson(result.get(0));
		mock.verifyClosed();
	}

	@Test
	public void testMappingWithNoUnpopulatedFieldsFound() throws Exception {
		Mock mock = new Mock();
		List<ConcretePerson> result = mock.getJdbcTemplate().query(
				"select name, age, birth_date, balance from people",
				new BeanPropertyRowMapper<>(ConcretePerson.class, true));
		assertThat(result.size()).isEqualTo(1);
		verifyPerson(result.get(0));
		mock.verifyClosed();
	}

	@Test
	public void testMappingWithUnpopulatedFieldsNotChecked() throws Exception {
		Mock mock = new Mock();
		List<ExtendedPerson> result = mock.getJdbcTemplate().query(
				"select name, age, birth_date, balance from people",
				new BeanPropertyRowMapper<>(ExtendedPerson.class));
		assertThat(result.size()).isEqualTo(1);
		ExtendedPerson bean = result.get(0);
		verifyPerson(bean);
		mock.verifyClosed();
	}

	@Test
	public void testMappingWithUnpopulatedFieldsNotAccepted() throws Exception {
		Mock mock = new Mock();
		assertThatExceptionOfType(InvalidDataAccessApiUsageException.class).isThrownBy(() ->
				mock.getJdbcTemplate().query("select name, age, birth_date, balance from people",
						new BeanPropertyRowMapper<>(ExtendedPerson.class, true)));
	}

	@Test
	public void testMappingNullValue() throws Exception {
		BeanPropertyRowMapper<Person> mapper = new BeanPropertyRowMapper<>(Person.class);
		Mock mock = new Mock(MockType.TWO);
		assertThatExceptionOfType(TypeMismatchException.class).isThrownBy(() ->
				mock.getJdbcTemplate().query("select name, null as age, birth_date, balance from people", mapper));
	}

	@Test
	public void testQueryWithSpaceInColumnNameAndLocalDateTime() throws Exception {
		Mock mock = new Mock(MockType.THREE);
		List<SpacePerson> result = mock.getJdbcTemplate().query(
				"select last_name as \"Last Name\", age, birth_date, balance from people",
				new BeanPropertyRowMapper<>(SpacePerson.class));
		assertThat(result.size()).isEqualTo(1);
		verifyPerson(result.get(0));
		mock.verifyClosed();
	}

	@Test
	public void testQueryWithSpaceInColumnNameAndLocalDate() throws Exception {
		Mock mock = new Mock(MockType.THREE);
		List<DatePerson> result = mock.getJdbcTemplate().query(
				"select last_name as \"Last Name\", age, birth_date, balance from people",
				new BeanPropertyRowMapper<>(DatePerson.class));
		assertThat(result.size()).isEqualTo(1);
		verifyPerson(result.get(0));
		mock.verifyClosed();
	}

}
