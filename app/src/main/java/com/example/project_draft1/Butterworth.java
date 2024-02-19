package com.example.project_draft1;

public class Butterworth {
    private double[] b;
    private double[] a;
    private double[] in;
    private double[] out;

    public void bandPass(int order, double sampleRate, double lowCutoff, double highCutoff) {
        int numCoeffs = order + 1;

        b = new double[numCoeffs];
        a = new double[numCoeffs];
        in = new double[numCoeffs];
        out = new double[numCoeffs];

        // Design a bandpass filter using Butterworth filter coefficients
        double wc1 = 2.0 * Math.PI * lowCutoff / sampleRate;
        double wc2 = 2.0 * Math.PI * highCutoff / sampleRate;

        double k = Math.tan((wc2 - wc1) / 2.0) / Math.tan((wc2 + wc1) / 2.0);

        double[] aCoeffs = new double[numCoeffs];
        aCoeffs[0] = 1.0;

        double[] bCoeffs = new double[numCoeffs];
        bCoeffs[0] = k;

        for (int m = 1; m < numCoeffs; m++) {
            double binom = 1.0;

            for (int i = 1; i <= m; i++) {
                binom *= (double) (m - i + 1) / (double) i;
            }

            aCoeffs[m] = binom * Math.pow(-1.0, m) * Math.pow(k, m);
            bCoeffs[m] = binom * Math.pow(k, m);
        }

        for (int i = 0; i < numCoeffs; i++) {
            a[i] = aCoeffs[i];
            b[i] = bCoeffs[i];
            in[i] = 0.0;
            out[i] = 0.0;
        }
    }

    public double filter(double x) {
        in[0] = x;

        double y = 0.0;

        for (int i = 0; i < a.length; i++) {
            y += b[i] * in[i] - a[i] * out[i];
        }

        System.arraycopy(in, 0, in, 1, in.length - 1);
        System.arraycopy(out, 0, out, 1, out.length - 1);

        in[0] = 0.0;
        out[0] = y;

        return y;
    }
}

