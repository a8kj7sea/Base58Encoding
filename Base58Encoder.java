import java.math.BigInteger;

public class Base58Encoder {
    private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";

    public static String encode(byte[] data) {
        BigInteger value = new BigInteger(1, data);
        StringBuilder result = new StringBuilder();

        while (value.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] quotientAndRemainder = value.divideAndRemainder(BigInteger.valueOf(58));
            value = quotientAndRemainder[0];
            int remainder = quotientAndRemainder[1].intValue();
            result.insert(0, ALPHABET.charAt(remainder));
        }

        // Pad leading zeros
        for (byte b : data) {
            if (b == 0x00) {
                result.insert(0, ALPHABET.charAt(0));
            } else {
                break;
            }
        }

        return result.toString();
    }

    public static byte[] decode(String encoded) {
        BigInteger value = BigInteger.ZERO;
        for (int i = 0; i < encoded.length(); i++) {
            int digit = ALPHABET.indexOf(encoded.charAt(i));
            if (digit == -1) {
                throw new IllegalArgumentException("Invalid Base58 character: " + encoded.charAt(i));
            }
            value = value.multiply(BigInteger.valueOf(58)).add(BigInteger.valueOf(digit));
        }

        byte[] bytes = value.toByteArray();
        if (bytes.length > 1 && bytes[0] == 0x00) {
            // Remove leading zero if present
            byte[] tmp = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, tmp, 0, tmp.length);
            return tmp;
        } else {
            return bytes;
        }
    }
}
