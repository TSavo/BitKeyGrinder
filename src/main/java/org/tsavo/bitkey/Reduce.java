package org.tsavo.bitkey;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.crypto.digests.RIPEMD160Digest;
import org.spongycastle.math.ec.ECPoint;

public class Reduce extends Reducer<Text, Text, Text, Text> {
	@Override
	protected void reduce(Text key, Iterable<Text> value, Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException {
		for (Text t : value) {
			context.write(t, new Text(computeHashForKey(t.toString())));
		}
	}

	public static byte[] sha256hash160(byte[] input) {
		try {
			byte[] sha256 = MessageDigest.getInstance("SHA-256").digest(input);
			RIPEMD160Digest digest = new RIPEMD160Digest();
			digest.update(sha256, 0, sha256.length);
			byte[] out = new byte[20];
			digest.doFinal(out, 0);
			return out;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e); // Cannot happen.
		}
	}

	public static String bytesToHex(byte[] bytes) {
		final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	static ECPoint point = SECNamedCurves.getByName("secp256k1").getG();

	public static String computeHashForKey(String string) {
		try {
			return bytesToHex(sha256hash160(point.multiply(new BigInteger(1, MessageDigest.getInstance("SHA-256").digest(string.getBytes()))).getEncoded()));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
