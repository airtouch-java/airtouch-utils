package airtouch.utils;

/** Utils for converting byte[] to and from scalar */
public class ByteUtil {
    private ByteUtil() {}

        public static final long toLong(byte[] b) {
            return toLong(b,0,b.length);
        }
        
        /**
         * The <code>to{Long|Int|Short|Byte}</code> family of methods
         * convert a byte array to a scalar
         *
         * @param	b	a byte array
         * @param	offset	where in the byte array to start
         * @param	size	how many bytes
         * @return		{a long|an int|a short|a byte}
         *
         */
        public static final long toLong(byte[] b,int offset,int size) {
            long l = 0;
            for(int i=0;i<size;++i) l |= ((long)b[offset+i]&0xff)<<((size-i-1)<<3);
            return l;
        }
        public static final int toInt(byte[] b,int offset,int size) {
            int j = 0;
            for(int i=0;i<size;++i) j |= ((long)b[offset+i]&0xff)<<((size-i-1)<<3);
            return j;
        }
        public static final int toInt(byte[] b,int offset) {
            return b[offset+0]<<24 | (b[offset+1]&0xff)<<16 | (b[offset+2]&0xff)<<8 | (b[offset+3]&0xff);
        }
        public static final int toInt(byte[] b) {
            return b[0]<<24 | (b[1]&0xff)<<16 | (b[2]&0xff)<<8 | (b[3]&0xff);
        }
        public static final short toShort(byte[] b,int offset) {
            return (short)((b[offset+0]&0xff)<<8 | (b[offset+1]&0xff));
        }
        public static final short toShort(byte[] b) {
            return (short)((b[0]&0xff)<<8 | (b[1]&0xff));
        }
        public static final int toInt(byte bh, byte bl) {
            return (bh&0xff) << 8 | (bl&0xff);
        }
        
        public static final int toInt(byte bh ,byte bmh, byte bml, byte bl) {
            return (bh&0xff)<<24 | (bmh&0xff)<<16 | (bml&0xff)<<8 | (bl&0xff);
        }

        /*
         * The <code>getBytes</code> family of methods
         * convert a scalar to a byte array
         *
         * @param	i	the scalar to be converted
         * @param	offset	where in the byte array to start
         * @param	size	how many bytes
         * @return		the byte array
         *
         */
        public static final byte[] getBytes(long l,int size) {
            byte[] b = new byte[size];
            getBytes(l,b,0,size);
            return b;
        }
        public static final void getBytes(long l,byte[] b,int offset,int size) {
            for(int i=0;i<size;++i) b[offset+i] = (byte)(l>>((size-i-1)<<3));
        }
        public static final byte[] getBytes(int i) {
            return new byte[] { (byte)(i>>24), (byte)(i>>16), (byte)(i>>8), (byte)i };
        }
        public static final byte[] getBytes(short i) {
            return new byte[] { (byte)(i>>8), (byte)i };
        }
        @SuppressWarnings("java:S1854")
        public static final void getBytes(short i,byte[] b,int offset) {
            b[offset++] = (byte)(i>>8);
            b[offset++] = (byte)i;
        }
        
        @SuppressWarnings("java:S1854")
        public static final void getBytes(int i,byte[] b,int offset) {
            b[offset++] = (byte)(i>>24);
            b[offset++] = (byte)(i>>16);
            b[offset++] = (byte)(i>>8);
            b[offset++] = (byte)i;
        }
}

