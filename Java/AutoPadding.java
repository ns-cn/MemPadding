import sun.misc.Contended;

public class AutoPadding implements Runnable {
    public static int NUM_THREADS = 10; // 并发线程数
    public final static long ITERATIONS = 500L * 1000L * 1000L;
    private final int arrayIndex;
    private static AutoPaddingData[] longs;

    public AutoPadding(final int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public static void main(final String[] args) throws Exception {
        if (args.length >= 1) {
            NUM_THREADS = Integer.parseInt(args[0]);
        }
        longs = new AutoPaddingData[NUM_THREADS];
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new AutoPaddingData();
        }
        final long start = System.currentTimeMillis();
        runTest();
        System.out.println("duration = " + (System.currentTimeMillis() - start));
    }

    private static void runTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new AutoPadding(i));
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }

    // 模拟并发场景
    public void run() {
        long i = ITERATIONS + 1;
        while (0 != --i) {
            longs[arrayIndex].value = i;
        }
    }



    /**
     * -XX:+UseCompressedOops 压缩对象头，减少4个字节
     * -XX:-RestrictContended 添加自动填充注解所需要的参数
     */
    @Contended
    public final static class AutoPaddingData {
        public volatile long value = 0L;
    }
}