package com.example.demo.domain;

public class TestCase {

    private String uid;

    private String name;

    private String status;

    private TestStage testStage;

    private String scenarioId;

    private String failingStep;

    private String mergedSteps;

    private LoginType.Type loginType;

    public TestCase() {
        super();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.scenarioId = name.split(" ")[0].trim();
        if (this.scenarioId.length() > 5) {
            this.scenarioId = scenarioId.split("\\t")[0].trim();
        }
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TestStage getTestStage() {
        return testStage;
    }

    public void setTestStage(TestStage testStage) {
        this.testStage = testStage;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getFailingStep() {
        return failingStep;
    }

    public void setFailingStep(String failingStep) {
        this.failingStep = failingStep;
    }

    public String getMergedSteps() {
        return mergedSteps;
    }

    public void setMergedSteps(String mergedSteps) {
        this.mergedSteps = mergedSteps;
    }

    public LoginType.Type getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType.Type loginType) {
        this.loginType = loginType;
    }

    @Override
    public String toString() {
        return "TestCase [scenarioId=" + scenarioId + ", status=" + status + ", failingStep=" + failingStep + "]";
    }


}
