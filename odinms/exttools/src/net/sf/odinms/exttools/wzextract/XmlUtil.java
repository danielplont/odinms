package net.sf.odinms.exttools.wzextract;

public class XmlUtil {
	private static char[] specialCharacters = { '"', '\'', '&', '<', '>' };
	private static String[] replacementStrings = { "&quot;", "&apos;", "&amp;", "&lt;", "&gt;" };

	public static String sanitizeText(String text) {
		StringBuffer buffer = new StringBuffer(text);

		for (int i = 0; i < buffer.length(); i++) {
			for (int k = 0; k < specialCharacters.length; k++) {
				if (buffer.charAt(i) == specialCharacters[k]) {
					buffer.replace(i, i + 1, replacementStrings[k]);
					i += replacementStrings[k].length() - 1;
				} else if ((int) buffer.charAt(i) > 128) {
					String replacement = "&#" + (int) buffer.charAt(i) + ";";
					buffer.replace(i, i + 1, replacement);
				}
			}
		}
		return buffer.toString();
	}
}