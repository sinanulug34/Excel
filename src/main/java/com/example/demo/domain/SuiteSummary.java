package com.example.demo.domain;

public class SuiteSummary {

    int nok = 0;

    int ok = 0;

    int skipped = 0;

    int notRun = 0;

    public int getNok() {
        return nok;
    }

    public void setNok(int nok) {
        this.nok = nok;
    }

    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }

    public SuiteSummary() {
        super();
    }


    public void increateOk() {
        this.ok += 1;
    }

    public void increateNok() {
        this.nok += 1;
    }

    public void increateSkipped() {
        this.skipped += 1;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public int getNotRun() {
        return notRun;
    }

    public void setNotRun(int notRun) {
        this.notRun = notRun;
    }

    public void increateNotRun() {
        this.notRun += 1;
    }

    public int getTotalCount() {
        return this.nok + this.ok +this.skipped + this.notRun;
    }


}
