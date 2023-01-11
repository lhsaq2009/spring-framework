package org.springframework.jmx.export.assembler;

import javax.management.MBeanOperationInfo;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanInfo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rob Harrop
 * @author David Boden
 * @author Chris Beams
 */
public class MethodNameBasedMBeanInfoAssemblerTests extends AbstractJmxAssemblerTests {

	protected static final String OBJECT_NAME = "bean:name=testBean5";


	@Override
	protected String getObjectName() {
		return OBJECT_NAME;
	}

	@Override
	protected int getExpectedOperationCount() {
		return 5;
	}

	@Override
	protected int getExpectedAttributeCount() {
		return 2;
	}

	@Override
	protected MBeanInfoAssembler getAssembler() {
		MethodNameBasedMBeanInfoAssembler assembler = new MethodNameBasedMBeanInfoAssembler();
		assembler.setManagedMethods("add", "myOperation", "getName", "setName", "getAge");
		return assembler;
	}

	@Test
	public void testGetAgeIsReadOnly() throws Exception {
		ModelMBeanInfo info = getMBeanInfoFromAssembler();
		ModelMBeanAttributeInfo attr = info.getAttribute(AGE_ATTRIBUTE);

		assertThat(attr.isReadable()).isTrue();
		assertThat(attr.isWritable()).isFalse();
	}

	@Test
	public void testSetNameParameterIsNamed() throws Exception {
		ModelMBeanInfo info = getMBeanInfoFromAssembler();

		MBeanOperationInfo operationSetAge = info.getOperation("setName");
		assertThat(operationSetAge.getSignature()[0].getName()).isEqualTo("name");
	}

	@Override
	protected String getApplicationContextPath() {
		return "org/springframework/jmx/export/assembler/methodNameAssembler.xml";
	}

}
