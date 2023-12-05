public class GrantData {
    private String companyName;
    private String streetName;
    private double grantSize;
    private int fiscalYear;
    private String businessType;
    private int numberOfJobs;

    // конструктор
    public GrantData(String companyName, String streetName, double grantSize,
                     int fiscalYear, String businessType, int numberOfJobs) {
        this.companyName = companyName;
        this.streetName = streetName;
        this.grantSize = grantSize;
        this.fiscalYear = fiscalYear;
        this.businessType = businessType;
        this.numberOfJobs = numberOfJobs;
    }

    // геттеры и сеттеры
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public double getGrantSize() {
        return grantSize;
    }

    public void setGrantSize(double grantSize) {
        this.grantSize = grantSize;
    }

    public int getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(int fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public int getNumberOfJobs() {
        return numberOfJobs;
    }

    public void setNumberOfJobs(int numberOfJobs) {
        this.numberOfJobs = numberOfJobs;
    }

    public void printGrantData (GrantData grantData) {
        System.out.println(grantData.companyName + ", " + grantData.streetName + ", " +
                grantData.grantSize + ", " + grantData.fiscalYear + ", " +
                grantData.businessType + ", " + grantData.numberOfJobs + ";");
    }
}