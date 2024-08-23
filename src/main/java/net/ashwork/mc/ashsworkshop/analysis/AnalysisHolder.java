package net.ashwork.mc.ashsworkshop.analysis;

public class AnalysisHolder {

    private Analyzable analyzing;
    private Analyzable.Context context;

    public void analyze(Analyzable analyzing, Analyzable.Context context) {
        this.analyzing = analyzing;
        this.context = context;
    }

    public void finishAnalyzing() {
        // Handle edge case
        if (this.analyzing == null || this.context == null) {
            this.stopAnalyzing();
            return;
        }

        if (this.context.validate()) {
            // TODO: Apply this unlocked value somewhere
            System.out.println(this.analyzing.unlock());
        }
        this.stopAnalyzing();
    }

    public void stopAnalyzing() {
        this.analyzing = null;
        this.context = null;
    }
}
