package org.springframework.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Juergen Hoeller
 */
class ResourceUtilsTests {

	@Test
	void isJarURL() throws Exception {
		assertThat(ResourceUtils.isJarURL(new URL("jar:file:myjar.jar!/mypath"))).isTrue();
		assertThat(ResourceUtils.isJarURL(new URL(null, "zip:file:myjar.jar!/mypath", new DummyURLStreamHandler()))).isTrue();
		assertThat(ResourceUtils.isJarURL(new URL(null, "wsjar:file:myjar.jar!/mypath", new DummyURLStreamHandler()))).isTrue();
		assertThat(ResourceUtils.isJarURL(new URL(null, "jar:war:file:mywar.war*/myjar.jar!/mypath", new DummyURLStreamHandler()))).isTrue();
		assertThat(ResourceUtils.isJarURL(new URL("file:myjar.jar"))).isFalse();
		assertThat(ResourceUtils.isJarURL(new URL("http:myserver/myjar.jar"))).isFalse();
	}

	@Test
	void extractJarFileURL() throws Exception {
		assertThat(ResourceUtils.extractJarFileURL(new URL("jar:file:myjar.jar!/mypath"))).isEqualTo(new URL("file:myjar.jar"));
		assertThat(ResourceUtils.extractJarFileURL(new URL(null, "jar:myjar.jar!/mypath", new DummyURLStreamHandler()))).isEqualTo(new URL("file:/myjar.jar"));
		assertThat(ResourceUtils.extractJarFileURL(new URL(null, "zip:file:myjar.jar!/mypath", new DummyURLStreamHandler()))).isEqualTo(new URL("file:myjar.jar"));
		assertThat(ResourceUtils.extractJarFileURL(new URL(null, "wsjar:file:myjar.jar!/mypath", new DummyURLStreamHandler()))).isEqualTo(new URL("file:myjar.jar"));

		assertThat(ResourceUtils.extractJarFileURL(new URL("file:myjar.jar"))).isEqualTo(new URL("file:myjar.jar"));
		assertThat(ResourceUtils.extractJarFileURL(new URL("jar:file:myjar.jar!/"))).isEqualTo(new URL("file:myjar.jar"));
		assertThat(ResourceUtils.extractJarFileURL(new URL(null, "zip:file:myjar.jar!/", new DummyURLStreamHandler()))).isEqualTo(new URL("file:myjar.jar"));
		assertThat(ResourceUtils.extractJarFileURL(new URL(null, "wsjar:file:myjar.jar!/", new DummyURLStreamHandler()))).isEqualTo(new URL("file:myjar.jar"));
	}

	@Test
	void extractArchiveURL() throws Exception {
		assertThat(ResourceUtils.extractArchiveURL(new URL("jar:file:myjar.jar!/mypath"))).isEqualTo(new URL("file:myjar.jar"));
		assertThat(ResourceUtils.extractArchiveURL(new URL(null, "jar:myjar.jar!/mypath", new DummyURLStreamHandler()))).isEqualTo(new URL("file:/myjar.jar"));
		assertThat(ResourceUtils.extractArchiveURL(new URL(null, "zip:file:myjar.jar!/mypath", new DummyURLStreamHandler()))).isEqualTo(new URL("file:myjar.jar"));
		assertThat(ResourceUtils.extractArchiveURL(new URL(null, "wsjar:file:myjar.jar!/mypath", new DummyURLStreamHandler()))).isEqualTo(new URL("file:myjar.jar"));
		assertThat(ResourceUtils.extractArchiveURL(new URL(null, "jar:war:file:mywar.war*/myjar.jar!/mypath", new DummyURLStreamHandler()))).isEqualTo(new URL("file:mywar.war"));

		assertThat(ResourceUtils.extractArchiveURL(new URL("file:myjar.jar"))).isEqualTo(new URL("file:myjar.jar"));
		assertThat(ResourceUtils.extractArchiveURL(new URL("jar:file:myjar.jar!/"))).isEqualTo(new URL("file:myjar.jar"));
		assertThat(ResourceUtils.extractArchiveURL(new URL(null, "zip:file:myjar.jar!/", new DummyURLStreamHandler()))).isEqualTo(new URL("file:myjar.jar"));
		assertThat(ResourceUtils.extractArchiveURL(new URL(null, "wsjar:file:myjar.jar!/", new DummyURLStreamHandler()))).isEqualTo(new URL("file:myjar.jar"));
		assertThat(ResourceUtils.extractArchiveURL(new URL(null, "jar:war:file:mywar.war*/myjar.jar!/", new DummyURLStreamHandler()))).isEqualTo(new URL("file:mywar.war"));
	}


	/**
	 * Dummy URLStreamHandler that's just specified to suppress the standard
	 * {@code java.net.URL} URLStreamHandler lookup, to be able to
	 * use the standard URL class for parsing "rmi:..." URLs.
	 */
	private static class DummyURLStreamHandler extends URLStreamHandler {

		@Override
		protected URLConnection openConnection(URL url) throws IOException {
			throw new UnsupportedOperationException();
		}
	}

}
