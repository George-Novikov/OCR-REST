package com.fatemorgan.ocr.constants;

public enum DPI {
    STANDARD(300),
    HIGH(600);

    private int dpi;

    DPI (int dpi){
        this.dpi = dpi;
    }

    public int getDPI(){ return dpi; }
}
