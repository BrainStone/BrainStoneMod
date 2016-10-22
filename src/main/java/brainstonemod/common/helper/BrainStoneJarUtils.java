package brainstonemod.common.helper;

import lombok.Cleanup;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipException;

/**
 * A universal static helper class.<br>
 * This class provides a lot functionality around working with the current jar
 * file.
 */
public final class BrainStoneJarUtils {
	public final static boolean RUNNING_FROM_JAR = isRunningFromJar();
	public final static boolean SIGNED_JAR = isJarSigned();

	/**
	 * Extracts ONE certificate chain from the specified certificate array which
	 * may contain multiple certificate chains, starting from index
	 * 'startIndex'.
	 */
	private static X509Certificate[] getAChain(Certificate[] certs, int startIndex) {
		if (startIndex > (certs.length - 1))
			return null;

		int i;
		// Keep going until the next certificate is not the
		// issuer of this certificate.
		for (i = startIndex; i < (certs.length - 1); i++) {
			if (!((X509Certificate) certs[i + 1]).getSubjectDN().equals(((X509Certificate) certs[i]).getIssuerDN())) {
				break;
			}
		}
		// Construct and return the found certificate chain.
		final int certChainSize = (i - startIndex) + 1;
		final X509Certificate[] ret = new X509Certificate[certChainSize];
		for (int j = 0; j < certChainSize; j++) {
			ret[j] = (X509Certificate) certs[startIndex + j];
		}
		return ret;
	}

	public static JarFile getRunningJar() throws IOException {
		return getRunningJar(false);
	}

	public static JarFile getRunningJar(boolean verify) throws IOException {
		if (!RUNNING_FROM_JAR)
			return null;

		try {
			String path = URLDecoder.decode(
					BrainStoneJarUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");

			return new JarFile(path.substring(5, path.lastIndexOf('!')), verify);
		} catch (ZipException e) {
			BSP.warnException_noAddon(e);

			return null;
		}
	}

	/**
	 * Checks whether the jar is signed
	 * 
	 * @return <code>true</code> if the jar is signed
	 */
	private static boolean isJarSigned() {
		if (!RUNNING_FROM_JAR)
			return true;

		try {
            @Cleanup
            final JarFile jarFile = getRunningJar();
            if (jarFile == null)
                return false;

            // Ensure the jar file is signed.
            final Manifest man = jarFile.getManifest();
            return man != null && !man.getEntries().isEmpty();

        } catch (final Throwable t) {
			BSP.warnException_noAddon(t);

			return false;
		}
	}

	private static boolean isRunningFromJar() {
		String className = BrainStoneJarUtils.class.getName().replace('.', '/');
		String classJar = BrainStoneJarUtils.class.getResource("/" + className + ".class").toString();

		return classJar.startsWith("jar:");
	}

	private static void verify(X509Certificate targetCert) throws IOException {
		// Sanity checking
		if (targetCert == null)
			throw new SecurityException("Provider certificate is invalid");

		@Cleanup
		final JarFile jarFile = getRunningJar(true);

		final Vector<JarEntry> entriesVec = new Vector<>();

		// Ensure the jar file is signed.
		final Manifest man = jarFile.getManifest();
		if (man == null)
			throw new SecurityException("The provider is not signed");

		// Ensure all the entries' signatures verify correctly
		final byte[] buffer = new byte[8192];
		final Enumeration<JarEntry> entries = jarFile.entries();

		while (entries.hasMoreElements()) {
			final JarEntry je = entries.nextElement();

			// Skip directories.
			if (je.isDirectory()) {
				continue;
			}

			entriesVec.addElement(je);
			@Cleanup
			final InputStream is = jarFile.getInputStream(je);

			// Read in each jar entry. A security exception will
			// be thrown if a signature/digest check fails.
			while (is.read(buffer, 0, buffer.length) != -1) {
			}
		}

		// Get the list of signer certificates
		final Enumeration<JarEntry> e = entriesVec.elements();

		while (e.hasMoreElements()) {
			final JarEntry je = e.nextElement();

			// Every file must be signed except files in META-INF.
			final Certificate[] certs = je.getCertificates();
			if ((certs == null) || (certs.length == 0)) {
				if (!je.getName().startsWith("META-INF"))
					throw new SecurityException("The provider has unsigned class files.");
			} else {
				// Check whether the file is signed by the expected
				// signer. The jar may be signed by multiple signers.
				// See if one of the signers is 'targetCert'.
				int startIndex = 0;
				X509Certificate[] certChain;
				boolean signedAsExpected = false;

				while ((certChain = getAChain(certs, startIndex)) != null) {
					if (certChain[0].equals(targetCert)) {
						// Stop since one trusted signer is found.
						signedAsExpected = true;
						break;
					}

					// Proceed to the next chain.
					startIndex += certChain.length;
				}

				if (!signedAsExpected)
					throw new SecurityException("The provider is not signed by a trusted signer");
			}
		}
	}

	/**
	 * Verifies the signature of the jar. Will do nothing if the jar is not
	 * signed
	 *
	 * @throws SecurityException
	 *             This method throws an SecurityException if for any reason the
	 *             signature is invalid.<br>
	 *             <br>
	 *             Possible reasons:
	 *             <ul>
	 *             <li>There are unsigned files in the jar
	 *             <li>The Hash of a file doesn't match (meaning the file has
	 *             been altered)
	 *             <li>A invalid certificate has been used to sign this jar
	 *             <li>The jar is not signed
	 *             </ul>
	 */
	public static void verifyJar() throws SecurityException {
		if (!SIGNED_JAR)
			return;

		final String certStr = "MIIFsTCCA5mgAwIBAgIECyArtTANBgkqhkiG9w0BAQsFADCBiDELMAkGA1UEBhMCREUxHDAaBgNV"
				+ "BAgME0JhZGVuLVfvv71ydHRlbWJlcmcxEjAQBgNVBAcTCUthcmxzcnVoZTEQMA4GA1UEChMHVW5r"
				+ "bm93bjEbMBkGA1UECxMSQ3JhenlCbG9jay1OZXR3b3JrMRgwFgYDVQQDEw9ZYW5uaWNrIFNjaGlu"
				+ "a28wHhcNMTYwNzAzMjIwODI2WhcNMTYxMDAxMjIwODI2WjCBiDELMAkGA1UEBhMCREUxHDAaBgNV"
				+ "BAgME0JhZGVuLVfvv71ydHRlbWJlcmcxEjAQBgNVBAcTCUthcmxzcnVoZTEQMA4GA1UEChMHVW5r"
				+ "bm93bjEbMBkGA1UECxMSQ3JhenlCbG9jay1OZXR3b3JrMRgwFgYDVQQDEw9ZYW5uaWNrIFNjaGlu"
				+ "a28wggIiMA0GCSqGSIb3DQEBAQUAA4ICDwAwggIKAoICAQCLwIixRYesAkgmAZTR9JMrK5AC8cE6"
				+ "oAXCAhXa0sQvIIbkcDvDyeFELUQ83pZSLSwvs48bQNVwlHTEFCoxpwZElM5v5yU0zDoxluEO1SWc"
				+ "vqtc2hQ/qbhrNYpjxRYpAEDTWaQbT3lH2hO/d1XzS7P0fjy+uXw8+o6AFAYKxBfk7YyjzsROX6s4"
				+ "jiHUps/f5VkY8fND3kSDewncjkPbC80c52PJ7KHH74YObi7da4SRUl2l8/OrIocmA5XhvMGZ3q2E"
				+ "oWZjE+bFnxD5CHVeLl2ZWX/tRPTVlMQUPr9rDW6SfA1afVtEjvRCeT7Qyzud4e44/248TKgPi81a"
				+ "UubjwySY6FH+Lzg+3z8ii+KbjIFFikqJDlbW5lRHX4OxPXz6tTX6Nj+s7+o8MlBUphdXrl31JCkC"
				+ "K9nVJesDT102velbcGjmFQEr3cZfQu4zhW+ohCvGRUzeeGNb10r/jbYSSmq1sb5ZqTHDpYLi8UEj"
				+ "JROdrOeZnU3Ou9VXiFA6zjpqh7OTLlJ136FQ1YJYIU0inv90NtF95685JfeiOM33Crik9R7yzkiL"
				+ "UyRisR/pdZd0kw3Yhx7q3+HTDQZ4ZUakvhGE7CESWlb1m8yq5iRAGk+PBjVB8vGho6gAMYm43/2B"
				+ "fzNMcYcWG5HTF7ThKDReYjbOHptVUUigTnKUP9uA1bo3TQIDAQABoyEwHzAdBgNVHQ4EFgQU23Jm"
				+ "/WSttEy0+IbH/wWuW1NoTXIwDQYJKoZIhvcNAQELBQADggIBADuQQEUB4mV9HfEzdR2ZHVFDURht"
				+ "6sETyDmSbiYW6GgQq3VaYCIpvCa2ioC6iEbSTljtc8h6Jy6O5QBbf6pnl8tz+pGizgRB/k/xZOtH"
				+ "d9tS1ZRoYMJp2XLZKqUdDcJRJMSKNHoC/ad68IHN/siJBzGpN/3Q/BOi/bOgdyb7M9VKxkgX3aWD"
				+ "Ci82j3FWsDF93HD2D2uuKXwfnaUDMp040wauXlCnDFj25964xUlgkD39iAsFSMwhVvqYSzKkh0Eg"
				+ "GAU1W71ZXqR/NFzJebyXNY1zEIDhzcYgwf+hETRwY/0LOOzfdNtDRHMoOerg2fcjbFLk/7Nb57mG"
				+ "ehqvos6bGNjXDFzqM9z1AsyckJYp5X2l5+AU5m+n6fiPcag1TKnJznB96sTuPsZsub7Zt4hzRWrq"
				+ "inZtlIdPdyveBSvZZdxk6G+Vi8fPCVOgXz4jv9P4i1eDtgmypJ0M6kyoROBLZ8UlSmeKvpRzG28h"
				+ "tAEF5MTB5liIJU5qx/jZAlSupugU3ANGMXBFHTIr/gQI/QqpI3dfqz6HIprTblOnfdegA1no5reE"
				+ "JxTwbr+Ygitrvvms8RIu9uUikQ+rp9HrydhzdyfAhkdt9JfpWpCPc83WKKjmVKy7g6Hsc8SGO6Bz"
				+ "xjjyGVieFI8Y3u+bdXVqrOn1W0OfM0tnUTHoZ5pdQdoE8ILt";

		final byte[] certBytes = Base64.getDecoder().decode(certStr);

		try {
			final CertificateFactory cf = CertificateFactory.getInstance("X.509");
			@Cleanup
			final ByteArrayInputStream inStream = new ByteArrayInputStream(certBytes);
			final X509Certificate cert = (X509Certificate) cf.generateCertificate(inStream);

			verify(cert);
		} catch (IOException e) {
			BSP.warnException_noAddon(e);
		} catch (CertificateException e) {
			BSP.warnException_noAddon(e);
		}
	}
}
