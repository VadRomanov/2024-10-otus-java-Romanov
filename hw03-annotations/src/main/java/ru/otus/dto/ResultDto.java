package ru.otus.dto;

public class ResultDto {
    private Integer passed;
    private Integer failed;

    public ResultDto(Integer passed, Integer failed) {
        this.passed = passed;
        this.failed = failed;
    }

    public Integer getTotal() {
        return passed + failed;
    }

    public Integer getPassed() {
        return passed;
    }

    public void setPassed(Integer passed) {
        this.passed = passed;
    }

    public Integer getFailed() {
        return failed;
    }

    public void setFailed(Integer failed) {
        this.failed = failed;
    }
}
