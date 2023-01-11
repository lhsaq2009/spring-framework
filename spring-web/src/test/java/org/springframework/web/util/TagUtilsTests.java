package org.springframework.web.util;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * Unit tests for the {@link TagUtils} class.
 *
 * @author Alef Arendsen
 * @author Rick Evans
 */
public class TagUtilsTests {

	@Test
	public void getScopeSunnyDay() {
		assertThat(TagUtils.SCOPE_PAGE).isEqualTo("page");
		assertThat(TagUtils.SCOPE_APPLICATION).isEqualTo("application");
		assertThat(TagUtils.SCOPE_SESSION).isEqualTo("session");
		assertThat(TagUtils.SCOPE_REQUEST).isEqualTo("request");

		assertThat(TagUtils.getScope("page")).isEqualTo(PageContext.PAGE_SCOPE);
		assertThat(TagUtils.getScope("request")).isEqualTo(PageContext.REQUEST_SCOPE);
		assertThat(TagUtils.getScope("session")).isEqualTo(PageContext.SESSION_SCOPE);
		assertThat(TagUtils.getScope("application")).isEqualTo(PageContext.APPLICATION_SCOPE);

		// non-existent scope
		assertThat(TagUtils.getScope("bla")).as("TagUtils.getScope(..) with a non-existent scope argument must " +
				"just return the default scope (PageContext.PAGE_SCOPE).").isEqualTo(PageContext.PAGE_SCOPE);
	}

	@Test
	public void getScopeWithNullScopeArgument() {
		assertThatIllegalArgumentException().isThrownBy(() ->
				TagUtils.getScope(null));
	}

	@Test
	public void hasAncestorOfTypeWhereAncestorTagIsNotATagType() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				TagUtils.hasAncestorOfType(new TagSupport(), String.class));
	}

	@Test
	public void hasAncestorOfTypeWithNullTagArgument() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				TagUtils.hasAncestorOfType(null, TagSupport.class));
	}

	@Test
	public void hasAncestorOfTypeWithNullAncestorTagClassArgument() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				TagUtils.hasAncestorOfType(new TagSupport(), null));
	}

	@Test
	public void hasAncestorOfTypeTrueScenario() throws Exception {
		Tag a = new TagA();
		Tag b = new TagB();
		Tag c = new TagC();

		a.setParent(b);
		b.setParent(c);

		assertThat(TagUtils.hasAncestorOfType(a, TagC.class)).isTrue();
	}

	@Test
	public void hasAncestorOfTypeFalseScenario() throws Exception {
		Tag a = new TagA();
		Tag b = new TagB();
		Tag anotherB = new TagB();

		a.setParent(b);
		b.setParent(anotherB);

		assertThat(TagUtils.hasAncestorOfType(a, TagC.class)).isFalse();
	}

	@Test
	public void hasAncestorOfTypeWhenTagHasNoParent() throws Exception {
		assertThat(TagUtils.hasAncestorOfType(new TagA(), TagC.class)).isFalse();
	}

	@Test
	public void assertHasAncestorOfTypeWithNullTagName() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				TagUtils.assertHasAncestorOfType(new TagA(), TagC.class, null, "c"));
	}

	@Test
	public void assertHasAncestorOfTypeWithNullAncestorTagName() throws Exception {
		assertThatIllegalArgumentException().isThrownBy(() ->
				TagUtils.assertHasAncestorOfType(new TagA(), TagC.class, "a", null));
	}

	@Test
	public void assertHasAncestorOfTypeThrowsExceptionOnFail() throws Exception {
		Tag a = new TagA();
		Tag b = new TagB();
		Tag anotherB = new TagB();

		a.setParent(b);
		b.setParent(anotherB);

		assertThatIllegalStateException().isThrownBy(() ->
				TagUtils.assertHasAncestorOfType(a, TagC.class, "a", "c"));
	}

	@Test
	public void testAssertHasAncestorOfTypeDoesNotThrowExceptionOnPass() throws Exception {
		Tag a = new TagA();
		Tag b = new TagB();
		Tag c = new TagC();

		a.setParent(b);
		b.setParent(c);

		TagUtils.assertHasAncestorOfType(a, TagC.class, "a", "c");
	}

	@SuppressWarnings("serial")
	private static final class TagA extends TagSupport {

	}

	@SuppressWarnings("serial")
	private static final class TagB extends TagSupport {

	}

	@SuppressWarnings("serial")
	private static final class TagC extends TagSupport {

	}

}
