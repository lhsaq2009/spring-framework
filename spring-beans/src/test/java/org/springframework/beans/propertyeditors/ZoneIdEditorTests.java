package org.springframework.beans.propertyeditors;

import java.time.ZoneId;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Nicholas Williams
 */
public class ZoneIdEditorTests {

	private final ZoneIdEditor editor = new ZoneIdEditor();

	@Test
	public void americaChicago() {
		editor.setAsText("America/Chicago");

		ZoneId zoneId = (ZoneId) editor.getValue();
		assertThat(zoneId).as("The zone ID should not be null.").isNotNull();
		assertThat(zoneId).as("The zone ID is not correct.").isEqualTo(ZoneId.of("America/Chicago"));

		assertThat(editor.getAsText()).as("The text version is not correct.").isEqualTo("America/Chicago");
	}

	@Test
	public void americaLosAngeles() {
		editor.setAsText("America/Los_Angeles");

		ZoneId zoneId = (ZoneId) editor.getValue();
		assertThat(zoneId).as("The zone ID should not be null.").isNotNull();
		assertThat(zoneId).as("The zone ID is not correct.").isEqualTo(ZoneId.of("America/Los_Angeles"));

		assertThat(editor.getAsText()).as("The text version is not correct.").isEqualTo("America/Los_Angeles");
	}

	@Test
	public void getNullAsText() {
		assertThat(editor.getAsText()).as("The returned value is not correct.").isEqualTo("");
	}

	@Test
	public void getValueAsText() {
		editor.setValue(ZoneId.of("America/New_York"));
		assertThat(editor.getAsText()).as("The text version is not correct.").isEqualTo("America/New_York");
	}

}
